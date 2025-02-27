# udpclient
MyUdpClient is a UDP client application that facilitates real-time communication with a UDP server. The program continuously prompts the user for input, sends messages to the server, and displays the server’s responses. This client maintains interaction until the user explicitly terminates the session by typing "Goodbye". It ensures proper socket management by closing the connection upon receiving the "GOODBYE" response from the server. The program supports both interactive user input and automated testing via the udp.input file.

## Identifying Information

* Name: Nolan Chen, Divi Newton
* Student ID: 2443884, 2440117
* Email: nolchen@chapman.edu, dnewton@chapman.edu
* Course: CPSC 353
* Assignment: PA02 UDP Client

## Source Files  
* udp.input
* MyUdpClient.java
* README.md

## References
* none

## Known Errors
* none

## Build Insructions
* Compile the program by typing: 
*  checkstyle MyUdpClient.java
*  javac MyUdpClient.java

## Execution Instructions
*  Run the program by typing:  
*  java MyUdpClient < udp.input
