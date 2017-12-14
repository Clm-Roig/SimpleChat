import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import client.*;
import common.*;
import javax.swing.*;
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
	private JPanel 		interactPanel;
	private JTextField 	textFChat;
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

		// Interaction Panel
		this.textFChat = new JTextField();
		textFChat.setPreferredSize(new Dimension(WINDOW_WIDTH - 80 - MARGIN, MSG_FONT_SIZE));
		textFChat.addKeyListener(this);

		this.buttonSend = new JButton("Send");
		buttonSend.setPreferredSize(new Dimension(80 - MARGIN, MSG_FONT_SIZE));
		buttonSend.addActionListener(this);

		this.interactPanel = new JPanel();
		interactPanel.add(textFChat);
		interactPanel.add(buttonSend);

		// Displaying messages Area
		this.displayArea = new JTextArea();
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
		displayArea.setPreferredSize(new Dimension(WINDOW_WIDTH - MARGIN, WINDOW_HEIGHT - MSG_FONT_SIZE - 2*MARGIN));
		displayArea.setBackground(Color.lightGray);

		// MainPanel Configuration
		this.mainPanel = new JPanel();	 
		mainPanel.add(displayArea);
		mainPanel.add(interactPanel);	    

		// Window Configuration	   
		this.setTitle(APP_NAME);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setContentPane(mainPanel); 	    
		this.setLocationRelativeTo(null);
		
		display("Welcome to "+APP_NAME+"!");

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
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ENTER) {
			String text = textFChat.getText();	 	
			if(text != null && !text.trim().isEmpty()) {		
				client.handleMessageFromClientUI(text);
				textFChat.setText("");
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
