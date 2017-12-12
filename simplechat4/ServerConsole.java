import java.io.*;

import server.*;
import common.*;

public class ServerConsole implements ChatIF {
	
	//Attributes ****************************************************
	EchoServer server;
	
	 /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the ServerConsole UI.
	 *
	 * @param port The port to connect on.
	 */
	public ServerConsole(int port) {
		server = new EchoServer(port, this);
	}
	
	//Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {
	      BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	      String message;

	      while (true) 
	      {
	        message = fromConsole.readLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	  @Override
	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	  public void display(String message) {
	    System.out.println("> " + message);
	  }
	  
	  /**
		 * This method is responsible for the creation of 
		 * the server instance (there is no UI in this phase).
		 *
		 * @param args[0] The port number to listen on.  Defaults to 5555 
		 *          if no argument is entered.
		 */
	  
		public static void main(String[] args) {
			int port = 0; //Port to listen on

			try {
				port = Integer.parseInt(args[0]); //Get port from command line
			}
			catch(Throwable t) {
				port = DEFAULT_PORT; //Set port to 5555
			}

			ServerConsole sv = new ServerConsole(port);
			
			try {
				System.out.println("Ready to obey!");
				sv.accept();
			}
			catch (Exception ex) {
				System.out.println("ERROR - Could not listen to admin commands!");
			}
		}	

}
