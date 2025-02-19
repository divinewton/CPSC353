import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
*  SMTP Client Program.
*  Recieves input from the keyboard of important information and stores in seperate variables.
*  Connects to a SMTP Server.
*  Waits for a Welcome message from the server.
*  Sends the first sentence to the server.
*  Receives and sends messages to the server and displays them until program completes.
*  Closes the socket and exits.
*  Author: Divi Newton
*  Email:  dnewton@chapman.edu
*  Date:  2/19/2025
*/

class Email {

  public static void main(String[] argv) throws Exception {
    // Get user input
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Enter sender's email address: ");
    final String senderEmail = inFromUser.readLine();

    System.out.print("Enter receiver's email address: ");
    final String receiverEmail = inFromUser.readLine();

    System.out.print("Enter sender's name: ");
    final String senderName = inFromUser.readLine();

    System.out.print("Enter receiver's name: ");
    final String receiverName = inFromUser.readLine();

    System.out.print("Enter email subject: ");
    final String subject = inFromUser.readLine();

    // Captures the user's message
    System.out.println("Enter email message (end with a single period on a new line): ");
    String messageBuilder = "";
    String line;
    while (!(line = inFromUser.readLine()).equals(".")) {
      messageBuilder += (line + "\n");
    }
    final String message = messageBuilder;
    // Finished getting user input

    // Connect to the server
    Socket clientSocket = null;

    try {
      clientSocket = new Socket("smtp.chapman.edu", 25);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }
    PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader inFromServer =  new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));

    // Exchange messages with the server
    // Recive and display the server's messages
    String serverMessage = inFromServer.readLine();
    System.out.println("SERVER:" + serverMessage);

    System.out.println("CLIENT: HELO students.chapman.edu");
    outToServer.println("HELO students.chapman.edu");
    serverMessage = inFromServer.readLine();
    System.out.println("SERVER: " + serverMessage);

    System.out.println("CLIENT: MAIL FROM: " + senderEmail);
    outToServer.println("MAIL FROM: " + senderEmail);
    serverMessage = inFromServer.readLine();
    System.out.println("SERVER: " + serverMessage);

    System.out.println("CLIENT: RCPT TO: " + receiverEmail);
    outToServer.println("RCPT TO: " + receiverEmail);
    serverMessage = inFromServer.readLine();
    System.out.println("SERVER: " + serverMessage);

    System.out.println("CLIENT: DATA");
    outToServer.println("DATA");
    serverMessage = inFromServer.readLine();
    System.out.println("SERVER: " + serverMessage);

    System.out.println("CLIENT: From: " + senderName);
    System.out.println("CLIENT: To: " + receiverName);
    System.out.println("CLIENT: Subject: " + subject);
    for (String lineMessage : message.split("\n")) {
      System.out.println("CLIENT: " + lineMessage);
    }
    System.out.println("CLIENT: .");
    outToServer.println("From: " + senderName);
    outToServer.println("To: " + receiverName);
    outToServer.println("Subject: " + subject);
    for (String lineMessage : message.split("\n")) {
      outToServer.println(lineMessage);
    }
    outToServer.println(".");
    serverMessage = inFromServer.readLine();
    System.out.println("SERVER: " + serverMessage);

    System.out.println("CLIENT: QUIT");
    outToServer.println("QUIT");
    serverMessage = inFromServer.readLine();
    System.out.println("SERVER: " + serverMessage);

    // Close the socket connection
    clientSocket.close();
  }
}
