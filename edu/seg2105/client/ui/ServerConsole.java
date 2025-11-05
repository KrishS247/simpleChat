package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;


public class ServerConsole implements ChatIF
{
  
  final public static int DEFAULT_PORT = 5555;
  
 
  EchoServer server;
  
  BufferedReader fromConsole;
 
  
  public ServerConsole() {
	  
	  //server = new EchoServer(port);
	  
	  try {
	      //server.listen();
		  
		  fromConsole = new BufferedReader(new InputStreamReader(System.in));
	      
	    } catch(Exception exception) {
	    	
	      System.out.println("Error: Can't setup connection!" + " Terminating server");
	      System.exit(1);
	    }
    
  }
  
  public void setServer(EchoServer server) {
	  this.server = server;
  }
   

  
  public void accept() 
  {
    try{
    	
    	BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
    

      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        
        server.handleServerConsole(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  
  public void display(String message) 
  {
	  if(message.startsWith("#")){
	      return;
	    }
	    else{
	          System.out.println("SERVER MSG> " + message);
	    }
  }
  

  
 /* 
  public static void main(String[] args) {
	  
    int port = 0;

    try{  
      port = Integer.parseInt(args[0]);
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT;
    }
    
    ServerConsole chat = new ServerConsole(port);
    
    chat.accept();
  }*/
}

