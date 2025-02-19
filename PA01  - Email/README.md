# email
This repository contains an Email.java program.
This program is based on the TCP client  program except it collects more information from the user and connects to the email server.

The Email.java program does the following:
+ Collects information from the user to send an email message.
+ Sends the message by connecting to the smtp.chapman.edu server and transmitting the message.  

The program displays all the SMTP commands that are sent to the server and all the responses that are received from the server.

## Identifying Information

* Name: Divi Newton
* Student ID: 2440117
* Email: dnewton@chapman.edu
* Course: CPSC 353
* Assignment: PA01 Email

## Source Files

* .gitignore
* Email.java
* README.md
* email.input

## References

* Class notes and TcpClient.java file

## Known Errors

* N/A

## Build Insructions

* javac Email.java

## Execution Instructions

Either of the following:
* java Email < email.input
* java Email