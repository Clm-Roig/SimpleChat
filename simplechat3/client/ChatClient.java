// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import com.lloseng.ocsf.client.*;

import common.*;

import java.io.*;
import java.util.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient implements Observer
{
	//Instance variables **********************************************

	/**
	 * The interface type variable.  It allows the implementation of 
	 * the display method in the client.
	 */
	ChatIF clientUI; 
	ObservableClient obsClient;
	String idClient;


	//Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	public ChatClient(String host, int port, ChatIF clientUI) 
			throws IOException 
	{
		this.obsClient = new ObservableClient(host, port); 
		this.obsClient.addObserver(this);
		this.clientUI = clientUI;
	}

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @param clientUI The interface type variable.
	 * @param idClient The id chosen by the user.
	 */	
	public ChatClient(String host, int port, String idClient, ChatIF clientUI) 
			throws IOException 
	{
		this.obsClient = new ObservableClient(host, port); 
		this.obsClient.addObserver(this);
		this.clientUI = clientUI;
		this.idClient = idClient;
	}


	//Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg)  {
		clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI            
	 *
	 * @param message The message from the UI.    
	 */
	public void handleMessageFromClientUI(String message) {

		if(message.charAt(0) == '#') {
			// Get the action 
			char[] actionChar = new char[message.length() - 1];
			message.getChars(1, message.length(), actionChar, 0);
			String action = String.valueOf(actionChar);

			performAction(action);		
		}
		else {
			try
			{
				obsClient.sendToServer(message);
			}
			catch(IOException e)
			{
				clientUI.display
				("Could not send message to server. Would you like to connect to a server maybe ?");
				//quit();
			}
		}
	}

	private void performAction(String action){
		//Testing if there is a parameter
		boolean withParameter = false;
		String parameter = "";
		StringTokenizer st = new StringTokenizer(action);
		if(st.countTokens() > 1) {
			withParameter = true;
			action = st.nextToken();
			parameter = st.nextToken();
		}

		if (!withParameter) {
			//if one word
			switch(action){

			case("quit"):
				if(obsClient.isConnected()) {
					clientUI.display("Disconnecting from the server :(");
					try {
						obsClient.closeConnection();
					} catch (IOException e) {
						clientUI.display("Error disconnecting from server.");
						e.printStackTrace();
					}
				}
			quit();				

			break;

			case("logoff"):  

				if(obsClient.isConnected()) {
					try {
						obsClient.closeConnection();
					} catch (IOException e) {
						clientUI.display("Error closing connection : ");
						e.printStackTrace();
					}
				}
				else {
					clientUI.display("You're not even logged in!");
				}
			break;

			case("login"):
				if(obsClient.isConnected()) {
					clientUI.display("You are already connected to a server : don't cheat on it!");
				}
				else {
					try {
						obsClient.openConnection();
						obsClient.sendToServer("#login "+this.idClient);
					} catch (IOException e) {
						clientUI.display("Error connecting to server : check your port and host with #getport and #gethost.");
					}
				}
			break;

			case("gethost"):
				clientUI.display("Host: " + obsClient.getHost());
			break;

			case("getport"):
				clientUI.display("Port: "+  obsClient.getPort());				
			break;

			default: 
				clientUI.display("Action not known !");
			}

		}

		else {

			switch(action){

			case("sethost"):
				if(obsClient.isConnected()){
					clientUI.display("Don't you try to cheat on your current host!");
				}
				else {
					obsClient.setHost(parameter);
					clientUI.display("Host set to "+parameter);
				}
			break;

			case("setport"):
				if(obsClient.isConnected()){
					clientUI.display("Don't you try to cheat on your current port!");
				}
				else {
					if(parameter.matches("^\\d+$")){
						obsClient.setPort(Integer.parseInt(parameter));
						clientUI.display("Port set to "+parameter);
					}
					else{
						clientUI.display("Int expected you dummy, how do you expect me to do my job if you give me a string? Seriously I just had the worst day ever and you have to do THIS?");
					}
				}
			break;

			default: 
				clientUI.display("Action not known !");
			}
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		System.out.println("Bye bye !");
		System.exit(0);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg == obsClient.CONNECTION_CLOSED) {
			clientUI.display("Disconnecting from the server :(");	
		}
		else if(arg == obsClient.CONNECTION_ESTABLISHED){
				clientUI.display("Congratulations, " +  this.idClient + " you are connected!" );
		}
		else if(arg instanceof Exception) {
			clientUI.display("The connection was closed violently...");
		}
		else {
			clientUI.display(arg.toString());
		}
	}
}
	//End of ChatClient class
