import java.awt.Dimension;
import java.io.*;
import client.*;
import common.*;
import javax.swing.*;
import java.awt.Color;

public class ClientFenetre extends JFrame implements ChatIF {

	//Class variables *************************************************
	final public static int DEFAULT_PORT = 5555;

	//Instance variables **********************************************
	
	final private static int WINDOW_WIDTH = 500;
	final private static int WINDOW_HEIGHT = 500;
	final private static int MSG_FONT_SIZE = 20;
	final private static int MARGIN = 10;


	ChatClient client;
	private JPanel mainPanel;
	private JPanel displayPanel;
	private JPanel interactPanel;
	private JTextField textFChat;
	private JButton buttonSend;

	//Constructors ****************************************************

	/**
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientFenetre(String host, int port) {
		super();
		try {
			client= new ChatClient(host, port, this);
		} 
		catch(IOException exception) {
			System.out.println("Error: Can't setup connection!"
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
		try {
			client = new ChatClient(host, port, idClient, this);
		} 
		catch(IOException exception) {
			System.out.println("Error: Can't setup connection!"
					+ " Terminating client.");
			exception.printStackTrace();

			System.exit(1);
		}
			    
	    // Interaction Panel
	    this.textFChat = new JTextField();
	    textFChat.setPreferredSize(new Dimension(WINDOW_WIDTH - 80 - MARGIN, MSG_FONT_SIZE));
	    this.buttonSend = new JButton("Send");
	    buttonSend.setPreferredSize(new Dimension(80 - MARGIN, MSG_FONT_SIZE));
	    
	    this.interactPanel = new JPanel();
	    interactPanel.add(textFChat);
	    interactPanel.add(buttonSend);
	    
	    // Display Panel
	    this.displayPanel = new JPanel();
	    displayPanel.setPreferredSize(new Dimension(WINDOW_WIDTH - MARGIN, WINDOW_HEIGHT - MSG_FONT_SIZE - 2*MARGIN));
	    displayPanel.setBackground(Color.GRAY);
	    
	    // MainPanel Configuration
	    this.mainPanel = new JPanel();	 
	    mainPanel.add(displayPanel);
	    mainPanel.add(interactPanel);	    

	    // Window Configuration	   
	    this.setTitle("SimpleChat v4");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	    this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
	    this.setContentPane(mainPanel); 	    
	    this.setLocationRelativeTo(null);
	}

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub

	}


	// *********** MAIN ************** //
	public static void main(String[] args){
		String host = "";
		String idClient = "";
		int port = 0;  //The port number

		try {
			idClient = args[0];
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Please, provide a pseudo to use this client.");
			System.out.println(">> java ClientConsole YourPseudo");
			System.exit(1);
		}

		try {
			host = args[1];
		}
		catch(ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}

		// Port selected. DEFAULT_PORT if not provided or if incorrect format
		try {
			port = Integer.parseInt(args[1]);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		}
		catch(NumberFormatException e) {
			port = DEFAULT_PORT;
		}

		ClientFenetre chat = new ClientFenetre(host, port, idClient);
		chat.setVisible(true);
		System.out.println("Client running !");
	}

}
