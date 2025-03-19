# Spotify
This repository contains a spotify.py program.
This program is based on the spotifyexample.py program except it is expanded with more information and user interaction.

The spotify.py program does the following:
+ Uses client ID and secret to get Spotify token to access API.
+ Calls the API to search for an artist and shows the user their top ten songs.
+ Shows the user the number of the artist's albums and asks if they want to see a list. If so, it shows them.
+ Gives the user the option to search for another artist. 
+ Tells the user who has more albums.

## Identifying Information

* Name: Divi Newton
* Student ID: 2440117
* Email: dnewton@chapman.edu
* Course: CPSC 353
* Assignment: PA03 Spotify

## Source Files

* .gitignore
* README.md
* spotify.py
* spotify-input

## References

* spotifyexample.py program
* [Spofity API documentation](https://developer.spotify.com/documentation/web-api/concepts/api-calls)

## Known Errors

* N/A

## Build and Execution Instructions

First, replace "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" with actual client ID and secret in spotify.py. Then, use either of the following:

* python3 spotify.py <  spotify-input
* python3 spotify.py