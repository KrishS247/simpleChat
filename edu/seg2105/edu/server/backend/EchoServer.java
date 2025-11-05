package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.ui.ServerConsole;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
	
	String loginKey = "loginID";
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  private ChatIF serverUI;
  
  //Constructors ****************************************************
  
  public EchoServer(int port, ChatIF serverUI) {
	  super(port);
	  this.serverUI = serverUI;
  }
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  
  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
	  
    //System.out.println("Message received: " + msg + " from " + client);
    
    String msgStr = (String) msg;
    
    if(msgStr.startsWith("#login")) {
    	
    	if (client.getInfo(loginKey) != null) {
    		try {
    			client.sendToClient("ERROR - You are already logged in. Terminating connection.");
    			client.close();
    		} catch (IOException e) {
    			
    		}
    		return;
    	}
    	
    	String loginID = "";
    	loginID = msgStr.substring(msgStr.indexOf(" ") + 1);
    	
    	System.out.println("Message recieved: #login " + loginID + " from " + client);
    	
    	if(loginID.isEmpty()) {
    		try {
    			client.sendToClient("ERROR - No login ID provided! Terminating connection.");
    			client.close();
    		} catch (IOException e) {
    			
    		}
    		
    		return;
    	}
    	
    	client.setInfo(loginKey, loginID);
    	
    	try {
    		this.sendToAllClients(loginID + " has logged on.");
    		
    		System.out.println(loginID + " has logged on.");
    	} catch (Exception e) {}
    	
    	
    } else {
    
    	
    	Object clientObj = client.getInfo(loginKey);
    	
    	if(clientObj == null) {
    		try {
    			client.sendToClient("Error - Log in first! Terminiating connection.");
    			client.close();
    		} catch (IOException e) {}
    		
    		return;
    	}
    	
    	String clientID = (String) clientObj;
    	
    	String showMsg = clientID + "> " + msgStr;
    	
    	System.out.println("Message received: " + msgStr + " from " + clientID);
    	
    	this.sendToAllClients(showMsg);
    	
    }
  }
  
  public void handleServerConsole(String message) {
	  
	  if (message.startsWith("#")) {
		  
		  String[] splits = message.split(" ");
		  String conCommand = splits[0];
		  
		  if (conCommand.equals("#quit")) {
	            try {
	                close();
	            } catch (IOException b) {
	                System.out.println("Could not quit the server!");
	            }

	        } else if (conCommand.equals("#stop")) {
	            stopListening();

	        } else if (conCommand.equals("#close")) {
	            try {
	                close();
	            } catch (IOException b) {
	                System.out.println("Not able to close!");
	            }

	        } else if (conCommand.equals("#setport")) {
	            if (!isListening()) {
	                this.setPort(Integer.parseInt(splits[1]));
	            } else {
	                System.out.println("Cannot perform the command since the server is not closed!");
	            }

	        } else if (conCommand.equals("#start")) {
	            if (isListening()) {
	                System.out.println("Cannot perform the command since you are already connected!");
	            } else {
	                try {
	                    this.listen();
	                } catch (IOException b) {
	                    System.out.println("Connection was not made!");
	                }
	            }

	        } else if (conCommand.equals("#getport")) {
	            System.out.println("The Port is " + this.getPort());

	        } else {
	            System.out.println("Invalid input");
	        }
		  
	  }
  }
  
 
  
 /* 
  public void handleMessageFromServerUI(String message) {
	  
	  if (message.startsWith("#")) {
		  handleServerCommand(message);
	  } else {
		  
		  String showMessage = "SERVER MSG> " + message;
		  
		  if(serverUI != null) {
			  serverUI.display(showMessage);
		  }
		  
		  this.sendToAllClients(showMessage);
	  }
  }
  */
  /*
  private void handleServerCommand(String command) {
	  try {
		  if (command.equals("#quit")) {
			  close();
			  System.exit(0);
		  } else if (command.equals("#stop")){
			  if (isListening()) {
				  stopListening();
			  }
		  } else if (command.startsWith("#setport")) {
			  if(!isListening()) {
				 String newPort = command.substring(command.indexOf(" ") + 1);
				 setPort(Integer.parseInt(newPort));
			  } else {
				  serverUI.display("*ERROR* Cannot change port while server is listening.");
			  }
		  } else if (command.equals("#start")) {
			  if(!isListening()) {
				  listen();
				  serverUI.display("Server listening for connections on port " + getPort());
			  }
		  } else if (command.equals("#getport")) {
			  serverUI.display("Port: " + getPort());
		  }
	  } catch (Exception e) {}
  }
  */
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected to the server.");
  }
  
  @Override
  protected void clientDisconnected(ConnectionToClient client) {
	  
	  Object clientObj = client.getInfo(loginKey);
	  
	  System.out.println(clientObj + " has disconnected.");
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]);
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT;
    }
	
    EchoServer sv = new EchoServer(port);
    
  
    try 
    {
      sv.listen();
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
