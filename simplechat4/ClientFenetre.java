import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import client.*;
import common.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;

public class ClientFenetre extends JFrame implements ChatIF, ActionListener, KeyListener {

	//Class variables *************************************************
	final public static int DEFAULT_PORT = 5555;

	//Instance variables **********************************************

	final private static int WINDOW_WIDTH = 500;
	final private static int WINDOW_HEIGHT = 500;
	final private static int MSG_FONT_SIZE = 20;
	final private static int MARGIN = 10;

	final private static String APP_NAME = "Simple Chat 4";

	ChatClient 			client;
	
	private JPanel 		mainPanel;
	private JTextArea 	displayArea;
	private JScrollPane scrollPane;
	private JPanel 		chatPanel;
	private JPanel		interactPanel;
	private JTextField 	textFChat;
	
	private JButton		buttonLogin;
	private JButton		buttonLogoff;
	private JButton 	buttonSend;

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
		textFChat.setPreferredSize(new Dimension(WINDOW_WIDTH - 80 - MARGIN, MSG_FONT_SIZE));
		textFChat.addKeyListener(this);

		this.buttonSend = new JButton("Send");
		buttonSend.setPreferredSize(new Dimension(80 - MARGIN, MSG_FONT_SIZE));
		buttonSend.addActionListener(this);

		this.chatPanel = new JPanel();
		chatPanel.add(textFChat);
		chatPanel.add(buttonSend);
		
		// Interact Panel
		this.buttonLogoff = new JButton("Logoff");
		buttonLogoff.setPreferredSize(new Dimension(90 - MARGIN, MSG_FONT_SIZE));
		buttonLogoff.addActionListener(this);
		
		this.buttonLogin = new JButton("Login");
		buttonLogin.setPreferredSize(new Dimension(90 - MARGIN, MSG_FONT_SIZE));
		buttonLogin.addActionListener(this);

		this.interactPanel = new JPanel();
		interactPanel.add(buttonLogin);
		interactPanel.add(buttonLogoff);
		
		JPanel southPanel = new JPanel();
		BoxLayout bl = new BoxLayout(southPanel, BoxLayout.PAGE_AXIS);
		southPanel.setLayout(bl);
		southPanel.add(chatPanel);
		southPanel.add(interactPanel);

		// Displaying messages Area
		this.displayArea = new JTextArea();
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
		displayArea.setBackground(Color.lightGray);

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
