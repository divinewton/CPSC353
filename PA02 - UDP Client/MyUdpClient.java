import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
*  UDP Client Program
*  Connects to a UDP Server
*  Receives a line of input from the keyboard and sends it to the server
*  Receives a response from the server and displays it.
*  Repeats this process until the user types "Goodbye" to quit the interaction.
*
*  @author: Divi Newton
*     email: dnewton@chapman.edu
*     date: 2/26/2025
*  @version: 3.1
*
*/


class MyUdpClient {
  public static void main(String[] args) throws Exception {

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    DatagramSocket clientSocket = new DatagramSocket();

    InetAddress ipAddress = InetAddress.getByName("localhost");

    System.out.println("Type a Sentence (or type \"Goodbye\" to quit)");
    String sentence = inFromUser.readLine();

    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String modifiedSentence;
    DatagramPacket sendPacket;
    DatagramPacket receivePacket;

    while (true) {
      sendData = sentence.getBytes();
      
      sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 9876);

      clientSocket.send(sendPacket);

      receivePacket = new DatagramPacket(receiveData, receiveData.length);

      clientSocket.receive(receivePacket);

      modifiedSentence = new String(receivePacket.getData());
      System.out.println("FROM SERVER: " + modifiedSentence);

      if (sentence.toLowerCase().equals("goodbye")) {
        break;
      }

      System.out.println("Type another Sentence (or type \"Goodbye\" to quit)");
      sentence = inFromUser.readLine();
    }
    
    clientSocket.close();
  }
}
