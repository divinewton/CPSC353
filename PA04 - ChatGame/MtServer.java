import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * MTServer.java
 * This program implements a simple multithreaded chat server.  Every client that
 * connects to the server can broadcast data to all other clients.
 * The server stores an ArrayList of Client objects.
 * The MTServer uses a ClientHandler whose code is in a separate file.
 * When a client connects, the MTServer starts a ClientHandler in a separate thread
 * to receive messages from the client, as well as a new Client object.
 * To test, start the server first, then start multiple clients and type messages
 * in the client windows.
 */

public class MtServer {
  // Maintain list of all client Objects for broadcast
  private ArrayList<Client> clientList;

  public MtServer() {
    this.clientList = new ArrayList<Client>();
  }

  private void getConnection() {
    // Wait for a connection from the client
    try {
      System.out.println("Waiting for client connections on port 9016.");
      ServerSocket serverSock = new ServerSocket(9016);
      // This is an infinite loop, the user will have to shut it down
      // using control-c
      while (true) {
        Socket connectionSock = serverSock.accept();
        // Create a Client object (username will be set by handler)
        Client newClient = new Client(connectionSock, ""); // Pass socket, empty username initially
        clientList.add(newClient);
        // Create and start the handler in a new thread
        // Pass the Client object and the list
        ClientHandler handler = new ClientHandler(newClient, this.clientList); 
        Thread theThread = new Thread(handler);
        theThread.start();
      }
      // Will never get here, but if the above loop is given
      // an exit condition then we'll go ahead and close the socket
      //serverSock.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    MtServer server = new MtServer();
    server.getConnection();
  }
}