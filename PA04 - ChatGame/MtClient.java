import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


/**
 * MTClient.java
 * This program implements a simple multithreaded chat client. It connects to the
 * server (assumed to be on port 9016) and starts two threads:
 * one for listening for data sent from the server, and another that waits
 * for the user to type something in that will be sent to the server.
 * Anything sent to the server is broadcast to all clients.
 * The MTClient uses a ClientListener whose code is in a separate file.
 * The ClientListener runs in a separate thread, recieves messages form the server,
 * and displays them on the screen.
 * Data received is sent to the output screen, so it is possible that as
 * a user is typing in information a message from the server will be
 * inserted.
 */
public class MtClient {
  /**
   * main method.
   * 
   * @param args (not used).
   */
  public static void main(String[] args) {
    try {
      String hostname = "localhost";
      int port = 9016;

      System.out.println("Connecting to server on port " + port);
      Socket connectionSock = new Socket(hostname, port);

      DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());

      System.out.println("Connection made.");

      // List of commands for the clients
      System.out.println("User Commands:");
      System.out.println("1. Goodbye (To leave the chat)");
      System.out.println("2. Who? (Lists every Client in the chat)");

      // Start a thread to listen and display data sent by the server
      ClientListener listener = new ClientListener(connectionSock);
      Thread theThread = new Thread(listener);
      theThread.start();

      // Read input from the keyboard and send it to everyone else.
      // The only way to quit is to hit control-c, but a quit command
      // could easily be added.
      Scanner keyboard = new Scanner(System.in);
      while (true) {
        String data = keyboard.nextLine();
        serverOutput.writeBytes(data + "\n");

        // Check if the user wants to quit
        if (data.equalsIgnoreCase("Goodbye")) {
          System.out.println("Disconnecting...");
          System.exit(0);
          break; // Exit the loop
        }
      }
      if (connectionSock != null && !connectionSock.isClosed()) {
        connectionSock.close();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}