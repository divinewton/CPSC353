# chatgame

This repository contains a functional chatgame that connects users via command line in a chat where they can answer questions to recieve points. When users join, they will choose a unique username to enter the chat. The first user to join will be the host of the game. The host can ask questions (by typing "Question") and list users' scores (by typing "SCORES"). All chatgame users can type "Who?" for a list of the active users, and "Goodbye" to leave the chat. When users answer questions correct, they will gain points.

* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client
* ClientHandler.java receives messages from a client and handles gameplay and client commands
* Client.java contains client's socket connection, username, and score


## Identifying Information

* Name: Luke Wiljanen, Jake Jameson, Divi Newton
* Student ID: 2448331, 2366882, 2440117
* Email: wiljanen@chapman.edu, jajameson@chapman.edu, dnewton@chapman.edu
* Course: CPSC 353
* Assignment: PA04 - ChatGame - Submission 3

## Source Files

* Client.java
* ClientHandler.java
* ClientListener.java
* MtClient.java
* MtServer.java

## References

* https://www.geeksforgeeks.org/linkedhashmap-class-in-java/

## Known Errors

* n/a

## Contributions

* Divi Newton: Username Handling, Client Commands
* Luke Wiljanen: Question/Score Functionality, Client Commands
* Jake Jameson: Code Cleanup

## Build Insructions

* javac *.java

## Execution Instructions

* In one terminal run, java MtServer
* In seperate terminals run, java MtClient, to connect to the server