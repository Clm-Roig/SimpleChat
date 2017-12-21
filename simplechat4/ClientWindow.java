import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;
import java.io.*;
import client.*;
import common.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class ClientWindow extends JFrame implements ChatIF, ActionListener, KeyListener {

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
	
	private JTextArea 	displayArea;
	private JScrollPane scrollPane;
	
	private JPanel 		mainPanel;
	private JPanel 		chatPanel;
	private JPanel		interactPanel;
	private JPanel 		setPanel;
	private JPanel 		southPanel;
	
	private JButton		buttonLogin;
	private JButton		buttonLogoff;
	private JButton 	buttonSend;
	private JButton		buttonSetPort;
	private JButton		buttonSetHost;
	private JButton		buttonHide;
	
	private JTextField 	textFChat;
	private JTextField 	textFPort;
	private JTextField 	textFHost;
	private JTextField 	textFLogin;

	// Color
	private final Color red = new Color(255,90,87);
	private final Color green = new Color(151,255,155);
	private final Color blue = new Color(205,208,255);
	private final Color displayAreaColor = new Color(220,220,220);

	// Misc
	final private static String APP_NAME = "Simple Chat 4";
	
	ChatClient client;

	//Constructors ****************************************************

	/**
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 * @param idClient The id choosen by the user (will be used to loggin).
	 */
	public ClientWindow(String host, int port, String idClient) {
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
		chatPanel.setBackground(Color.BLACK);

		// Interact Panel (Login + logoff)
		JLabel labelLogin = new JLabel("Login:");
		labelLogin.setForeground(Color.RED);
		
		this.textFLogin = new JTextField();
		textFLogin.setPreferredSize(new Dimension(200 ,JTEXT_HEIGHT));
		textFLogin.addKeyListener(this);		
		
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
		interactPanel.add(textFLogin);
		interactPanel.add(buttonLogin);
		interactPanel.add(buttonLogoff);
		interactPanel.setBackground(Color.BLACK);


		// Set Panel
		this.textFPort = new JTextField(Integer.toString(port));
		textFPort.setColumns(6);
		textFPort.addKeyListener(this);

		JLabel labelPort = new JLabel("Port:");
		labelPort.setForeground(Color.RED);
		
		this.buttonSetPort = new JButton("Set");
		buttonSetPort.setPreferredSize(new Dimension(60, 18));
		buttonSetPort.addActionListener(this);

		
		this.textFHost = new JTextField(host);
		textFHost.setColumns(10);
		textFHost.addKeyListener(this);
		
		JLabel labelHost = new JLabel("Host:");
		labelHost.setForeground(Color.RED);

		this.buttonSetHost = new JButton("Set");
		buttonSetHost.setPreferredSize(new Dimension(60, 18));
		buttonSetHost.addActionListener(this);
		

		this.setPanel = new JPanel();
		setPanel.add(labelHost);
		setPanel.add(textFHost);
		setPanel.add(buttonSetHost);
		setPanel.add(labelPort);
		setPanel.add(textFPort);
		setPanel.add(buttonSetPort);
		setPanel.setBackground(Color.BLACK);
		
		// Button Hide
		this.buttonHide = new JButton("-");
		buttonHide.addActionListener(this);
		
		// South Panel (interactPanel + chatPanel + setPanel)
		this.southPanel = new JPanel();
		BoxLayout bl = new BoxLayout(southPanel, BoxLayout.PAGE_AXIS);
		southPanel.setLayout(bl);
		southPanel.add(chatPanel);
		southPanel.add(interactPanel);
		southPanel.add(setPanel);
		southPanel.add(buttonHide);
		southPanel.setBackground(Color.BLACK);

		// Displaying messages Area
		this.displayArea = new JTextArea();
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
		displayArea.setBackground(this.displayAreaColor);
		displayArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
        Font font = new Font("Ubuntu", Font.PLAIN, MSG_FONT_SIZE);
		displayArea.setFont(font);

		// Scroll Panel
		this.scrollPane = new JScrollPane(displayArea);
		scrollPane.setPreferredSize(new Dimension(WINDOW_WIDTH - MARGIN, WINDOW_HEIGHT - MSG_FONT_SIZE - 2*MARGIN));;

		// MainPanel Configuration
		this.mainPanel = new JPanel(new BorderLayout());	 
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.setBackground(new Color(0,0,0));

		
		// Window Configuration	   
		this.setTitle(APP_NAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
	
	// ***** Communication with Client Business *****
	
	// Try to set the port with the value in the JTextField of the port
	private void setPort() {
		String newPortStr = textFPort.getText();
		if(newPortStr.matches("^-?\\d+$")) {		
			int newPort = Integer.parseInt(newPortStr);
			client.handleMessageFromClientUI("#setport "+newPort);
		}
		else {
			display("Please, provide a number for port.");
		}
	}
	
	// Try to set the host with the value in the JTextField of the host
	private void setHost() {
		String newHost = textFHost.getText();
		if(newHost != null && !newHost.trim().isEmpty()) {
			client.handleMessageFromClientUI("#sethost "+newHost);	
		}
	}
	
	// Send message to the Client with the value in the JtextField of the chat
	private void sendMessage() {
		String text = textFChat.getText();
		if(text != null && !text.trim().isEmpty()) {		
			client.handleMessageFromClientUI(text);
			textFChat.setText("");
			textFChat.grabFocus();
		}
	}
	
	private void login() {
		String id = textFLogin.getText();
		if(id != null && !id.trim().isEmpty()) {	
			client.setId(id);
			client.handleMessageFromClientUI("#login");
			textFChat.grabFocus();	
		}
		else {
			display("Please, provide a login to connect to the server!");
		}
	}
	
	
	// ***** Listeners *****

	@Override
	public void actionPerformed(ActionEvent event) {

		if(event.getSource() == this.buttonSend) {
			sendMessage();
		}
		if(event.getSource() == this.buttonLogoff) {
			client.handleMessageFromClientUI("#logoff");
			textFChat.grabFocus();			
		}
		if(event.getSource() == this.buttonLogin) {
			login();
		}
		if(event.getSource() == this.buttonSetHost) {
			setHost();
		}
		if(event.getSource() == this.buttonSetPort) {
			setPort();
		}
		if(event.getSource() == this.buttonHide) {
			if(interactPanel.isVisible()) {
				interactPanel.setVisible(false);
				setPanel.setVisible(false);
				buttonHide.setText("+");
			}
			else {
				interactPanel.setVisible(true);
				setPanel.setVisible(true);
				buttonHide.setText("-");
			}
			
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		Component compFocus = this.getFocusOwner();

		if(compFocus instanceof JTextField) {
			if (key == KeyEvent.VK_ENTER) {
				if(compFocus == this.textFChat) {					
					sendMessage();
				}
				if(compFocus == this.textFHost) {				
					setHost();
				}
				if(compFocus == this.textFPort) {					
					setPort();
				}
				if(compFocus == this.textFLogin) {					
					login();
				}
				
			}		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	// *********** MAIN ************** //
	public static void main(String[] args){
		String host = "localhost";
		String idClient = "";
		int port = DEFAULT_PORT; 

		ClientWindow chat = new ClientWindow(host, port, idClient);
	}


}
