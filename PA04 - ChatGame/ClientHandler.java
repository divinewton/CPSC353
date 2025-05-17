import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * ClientHandler.java
 * This class handles communication between the client
 * and the server. It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast.
 * When a new ClientHandler for a new client is created,
 * the user is asked for a username which will then be included
 * in any messages sent to the other clients.
 * This class also handles broadcast messages to other clients
 * indicating users have joined and left the clat.
 */
public class ClientHandler implements Runnable {
  private Client client;
  private ArrayList<Client> clientList;
  private BufferedReader clientInput;
  private DataOutputStream clientDataOutput;
  private static Map<String, String> questionMap;
  private static String currentQuestion;
  private static String currentAnswer;
  private static Set<Client> hasAnswered;
  

  ClientHandler(Client client, ArrayList<Client> clientList) {
    this.client = client;
    this.clientList = clientList;
    this.questionMap = new LinkedHashMap<>();
    this.currentQuestion = "";
    this.currentAnswer = "";
    this.hasAnswered = new HashSet<>();


    try {
      this.clientInput = new BufferedReader(
          new InputStreamReader(client.connectionSock.getInputStream()));
      this.clientDataOutput = new DataOutputStream(client.connectionSock.getOutputStream());
    } catch (IOException e) {
      System.out.println("Error initializing streams for client: " + e.getMessage());
    }

    // List of True/ False questions in a hash map to be asked by the host
    questionMap.put(
        "A computer has IP address 206.211.141.93 with subnet mask 255.255.255.192 "
        + "\nTrue or False? \nThe address 206.211.141.79 is also on this network.", "True");
    questionMap.put(
        "OSPF uses BGP for routing among areas.", "False");
    questionMap.put(
        "In hot potato routing, the route chosen is the route with the least "
        + "cost to the NEXT-HOP router", "True");
    questionMap.put(
        "In a distance-vector routing algorithm, each node has a map of the entire network and "
        + "determines the shortest path from itself to all other nodes in the network.", "False");
    questionMap.put(
        "The Internet's OSPF routing protocol is often accomplished by a "
        + "link-state broadcast algorithm", "True");
    questionMap.put(
        "Exchanges between OSPF routers can be authenticated", "True");
    questionMap.put(
        "The distance-vector algorithm is a link-state algorithm", "False");
    questionMap.put(
        "Link-layer switches make forwarding decisions based on values in "
        + "fields in the link layer frame", "True");
    questionMap.put(
        "Routing refers to the router-local action of transferring a packet "
        + "from an input link interface to the appropriate output link interface", "False");
  }

  String getNextQuestion() {
    if (questionMap.isEmpty()) {
      System.out.println("No more questions :(");
      return null;
    } else {
      String nextq = questionMap.keySet().iterator().next();
      currentAnswer = questionMap.get(nextq);
      return nextq;
    }
  }


  
  /**
   * Handles welcome, username prompt, join broadcast, and subsequent messages.
   */
  public void run() {
    try {

      if (clientList.size() == 1) {
        client.username  = "host";
        System.out.println(client.username + " connected.");
        clientDataOutput.writeBytes("You are the game host!\n");
        clientDataOutput.writeBytes("Host Commands:\n");
        clientDataOutput.writeBytes("1. Question (host only. Produces a new question)\n");
        clientDataOutput.writeBytes(
            "2. SCORES (host only. Displays the scores of every Client to the whole chat)\n"
        );
        clientDataOutput.flush();
      } else {
        clientDataOutput.writeBytes("Welcome! Please enter your username:\n");
        clientDataOutput.flush();
        String name = "";
        
        while (true) {
          name = clientInput.readLine();
          boolean isNameTaken = false;
          for (Client usernames : clientList) {
            if (usernames.username.equals(name)) {
              clientDataOutput.writeBytes("Username taken. Enter a differnt username:\n ");
              clientDataOutput.flush();
              isNameTaken = true;
              break;
            } 
          }
          if (isNameTaken) {
            continue;
          } else {
            client.username = name.trim();
            System.out.println(client.username + " connected.");
            broadcastMessage(client.username + " has joined the chat", client);
            break;
          }
        }
      }
      
      

      while (true) {
        // Get data sent from a client
        String clientText = clientInput.readLine();
        //swtich statment for different client inputs
        switch (clientText) {
          case "Goodbye":
            System.out.println("Closing connection for " + client.username);
            Client leavingClient = client; // Keep reference for broadcast
            clientList.remove(client); // Remove before closing socket
            broadcastMessage(leavingClient.username + " has left the chat", leavingClient);
            if (client.connectionSock != null && !client.connectionSock.isClosed()) {
              client.connectionSock.close();
            }
            return;

          case "Who?":
            clientDataOutput.writeBytes("Online users: \n");
            for (Client c : clientList) {
              if (c.username != null && !c.username.isEmpty()) {
                clientDataOutput.writeBytes(c.username + "\n");
              }
            }
            clientDataOutput.flush();
            break; 
          
          case "Question":
            if (client.username.equals("host")) {
              currentQuestion = getNextQuestion();
            } else {
              System.out.println("Received from " + client.username + ": " + clientText);
              broadcastMessage(client.username + ": " + clientText, client);
              break;
            }
            if (currentQuestion == null) {
              clientDataOutput.writeBytes("No more questions.\n");
              clientDataOutput.flush();
              break;
            }
            currentAnswer = questionMap.get(currentQuestion);
            questionMap.remove(currentQuestion);
            hasAnswered.clear();
            broadcastMessage("QUESTION: " + currentQuestion, null);
            break;

          case "SCORES":
            //display score of all clients
            if (client.username.equals("host")) {
              broadcastMessage("Scores:", null);
              for (Client c : clientList) {
                if (c.username.equals("host")) {
                  continue;
                }
                broadcastMessage(c.username + " has " + c.score + " points", null);
              }
            } else {
              System.out.println("Received from " + client.username + ": " + clientText);
              broadcastMessage(client.username + ": " + clientText, client);
              break;
            }
            break;
            
          // default:
          //   System.out.println("Received from " + client.username + ": " + clientText);
          //   broadcastMessage(client.username + ": " + clientText, client);
          //   break;
          default:
            if (currentQuestion != null
                && (clientText.equalsIgnoreCase("True") || clientText.equalsIgnoreCase("False"))) {
              if (client.username.equals("host")) {
                clientDataOutput.writeBytes("Host cannot answer questions.\n");
                clientDataOutput.flush();
                break;
              }
              if (hasAnswered.contains(client)) {
                clientDataOutput.writeBytes("You already answered this question.\n");
                clientDataOutput.flush();
                break;
              }
              hasAnswered.add(client);
              if (clientText.equalsIgnoreCase(currentAnswer)) {
                client.score++;
                clientDataOutput.writeBytes("Correct! Your score: " + client.score + "\n");
              } else {
                clientDataOutput.writeBytes(
                    "Incorrect. Correct answer was: " + currentAnswer + "\n"
                );
              }
              clientDataOutput.flush();
            } else {
              System.out.println("Received from " + client.username + ": " + clientText);
              broadcastMessage(client.username + ": " + clientText, client);
            }
            break;

        }
        
      }
      
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      // Remove from arraylist
      clientList.remove(client);
    }
  }

  /**
   * Sends broadcast messages to all clients except the sender.
   */
  private void broadcastMessage(String message, Client sender) {
    // Create a temporary list to iterate over to avoid errors
    ArrayList<Client> currentClients = new ArrayList<>(clientList);
    for (Client c : currentClients) {
      // Don't send the message back to the sender
      if (c == sender) {
        continue;
      }
      try {
        if (c.connectionSock != null && !c.connectionSock.isClosed()) {
          DataOutputStream clientOutput = new DataOutputStream(c.connectionSock.getOutputStream());
          clientOutput.writeBytes(message + "\n");
        } 
      } catch (IOException e) {
        System.out.println("Error: " + e.toString());
      }
    }
  }
}