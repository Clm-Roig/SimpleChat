import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import client.*;
import common.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.BorderLayout;
import java.awt.Color;

public class ClientFenetre extends JFrame implements ChatIF, ActionListener, KeyListener {

	//Class variables *************************************************
	final public static int DEFAULT_PORT = 5555;

	//Instance variables **********************************************

	// Sizes
	final private int WINDOW_WIDTH = 600;
	final private int WINDOW_HEIGHT = 500;
	final private int MSG_FONT_SIZE = 14;
	final private int JTEXT_HEIGHT = 22;
	final private int MARGIN = 10;

	// UI
	private JPanel 		mainPanel;
	private JTextArea 	displayArea;
	private JScrollPane scrollPane;
	private JPanel 		chatPanel;
	private JPanel		interactPanel;
	private JPanel 		setPanel;
	private JTextField 	textFChat;
	private JButton		buttonLogin;
	private JButton		buttonLogoff;
	private JButton 	buttonSend;
	private JTextField 	textFPort;
	private JTextField 	textFHost;

	// Color
	private final Color red = new Color(255,90,87);
	private final Color green = new Color(151,255,155);
	private final Color blue = new Color(205,208,255);


	// Misc
	final private static String APP_NAME = "Simple Chat 4";
	ChatClient 			client;
	
	private long dateLastModif = System.currentTimeMillis();
	private int DELAY_BEFORE_SAVING = 2000;
	
	//Constructors ****************************************************

	/**
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientFenetre(String host, int port) {
		super();
		try {
			client = new ChatClient(host, port, this);
		} 
		catch(IOException exception) {
			System.out.println("Error: Can't create the Client !"
					+ " Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 * @param idClient The id choosen by the user (will be used to loggin).
	 */
	public ClientFenetre(String host, int port, String idClient) {
		super();

		// Chat Panel
		this.textFChat = new JTextField();
		textFChat.setPreferredSize(new Dimension(WINDOW_WIDTH - 80, JTEXT_HEIGHT));
		textFChat.addKeyListener(this);

		this.buttonSend = new JButton("Send");
		buttonSend.setPreferredSize(new Dimension(80 - MARGIN, JTEXT_HEIGHT));
		buttonSend.addActionListener(this);

		this.chatPanel = new JPanel();
		chatPanel.add(textFChat);
		chatPanel.add(buttonSend);

		// Interact Panel (Login + logoff + setPort + setHost)
		this.buttonLogoff = new JButton("Logoff");
		buttonLogoff.setBackground(this.red);
		buttonLogoff.setForeground(Color.WHITE);
		buttonLogoff.setPreferredSize(new Dimension(90 - MARGIN, JTEXT_HEIGHT));
		buttonLogoff.addActionListener(this);

		this.buttonLogin = new JButton("Login");
		buttonLogin.setBackground(this.green);
		buttonLogin.setPreferredSize(new Dimension(90 - MARGIN, JTEXT_HEIGHT));
		buttonLogin.addActionListener(this);

		this.interactPanel = new JPanel();
		interactPanel.add(buttonLogin);
		interactPanel.add(buttonLogoff);

		// Set Panel
		this.textFPort = new JTextField(Integer.toString(port));
		textFPort.setColumns(6);
		textFPort.getDocument().addDocumentListener(
			new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					
				}
				public void removeUpdate(DocumentEvent e) {

				}
				public void insertUpdate(DocumentEvent e) {
					long curentTime = System.currentTimeMillis();
					System.out.println(curentTime - dateLastModif);
					if (curentTime - dateLastModif > DELAY_BEFORE_SAVING) {
						client.handleMessageFromClientUI("#setport "+Integer.parseInt(textFPort.getText()));
					}
					dateLastModif = System.currentTimeMillis();		
				}
			}
		);
		JLabel labelPort = new JLabel("Port:");

		this.textFHost = new JTextField(host);
		textFHost.setColumns(10);
		textFHost.getDocument().addDocumentListener(
			new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					
				}
				public void removeUpdate(DocumentEvent e) {
					
				}
				public void insertUpdate(DocumentEvent e) {
					client.handleMessageFromClientUI("#sethost "+textFHost.getText());
				}
			}
		);
		JLabel labelHost = new JLabel("Host:");

		this.setPanel = new JPanel();
		setPanel.add(labelHost);
		setPanel.add(textFHost);
		setPanel.add(labelPort);
		setPanel.add(textFPort);

		// South Panel (Interact + Chat Panels + setPanel)
		JPanel southPanel = new JPanel();
		BoxLayout bl = new BoxLayout(southPanel, BoxLayout.PAGE_AXIS);
		southPanel.setLayout(bl);
		southPanel.add(chatPanel);
		southPanel.add(interactPanel);
		southPanel.add(setPanel);

		// Displaying messages Area
		this.displayArea = new JTextArea();
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
		displayArea.setBackground(this.blue);
		displayArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		displayArea.setFont(new Font("Copperplate", Font.PLAIN, MSG_FONT_SIZE));

		// Scroll Panel
		this.scrollPane = new JScrollPane(displayArea);
		scrollPane.setPreferredSize(new Dimension(WINDOW_WIDTH - MARGIN, WINDOW_HEIGHT - MSG_FONT_SIZE - 2*MARGIN));;

		// MainPanel Configuration
		this.mainPanel = new JPanel(new BorderLayout());	 
		mainPanel.add(scrollPane, BorderLayout.CENTER);


		mainPanel.add(southPanel, BorderLayout.SOUTH);

		// Window Configuration	   
		this.setTitle(APP_NAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.setContentPane(mainPanel); 	
		this.pack();
		this.setLocationRelativeTo(null);

		display("Welcome to "+APP_NAME+"!\n");

		// Constructing Client
		try {
			client = new ChatClient(host, port, idClient, this);
		} 
		catch(IOException exception) {
			System.out.println("Error: Can't create client!"
					+ " Terminating client.");
			exception.printStackTrace();

			System.exit(1);
		}

		this.setVisible(true);
		textFChat.grabFocus();
				}

				@Override
				public void display(String message) {
					this.displayArea.append(message + "\n");
					this.displayArea.setCaretPosition(displayArea.getDocument().getLength());
				}

				@Override
				public void actionPerformed(ActionEvent event) {
					String text = textFChat.getText();

					if(event.getSource() == this.buttonSend) {
						if(text != null && !text.trim().isEmpty()) {		
							client.handleMessageFromClientUI(text);
							textFChat.setText("");
							textFChat.grabFocus();
						}
					}
					if(event.getSource() == this.buttonLogoff) {
						client.handleMessageFromClientUI("#logoff");
						textFChat.grabFocus();			
					}
					if(event.getSource() == this.buttonLogin) {
						client.handleMessageFromClientUI("#login");
						textFChat.grabFocus();			
					}
					if(event.getSource() == this.textFHost) {
						// After 1sec, we save the new hostname
						text = textFHost.getText();
						client.handleMessageFromClientUI("#sethost "+text);
					}

				}


				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_ENTER) {
						String text = textFChat.getText();	 	
						if(text != null && !text.trim().isEmpty()) {		
							client.handleMessageFromClientUI(text);
							textFChat.setText("");
							textFChat.grabFocus();
						}
					}		
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
				}


				// *********** MAIN ************** //
				public static void main(String[] args){
					String host = "localhost";
					String idClient = "JustForTest";
					int port = DEFAULT_PORT; 

					ClientFenetre chat = new ClientFenetre(host, port, idClient);
				}


	}
