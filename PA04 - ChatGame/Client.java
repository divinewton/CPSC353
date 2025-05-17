import java.net.Socket;

/**
 * Client.java
 * This class contains the information for a Client.
 * This inclused its socket and username.
 */

public class Client {
  public Socket connectionSock = null;
  public String username = "";
  public int score = 0;
  
  Client(Socket sock,  String username) {
    this.connectionSock = sock;
    this.username = username;
    this.score = score;
  }
}