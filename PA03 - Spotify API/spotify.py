import base64
import json
from requests import post, get

# Spotify Artist Stats Program
# Uses client ID and secret to get Spotify token to access API.
# Calls the API to search for an artist and shows the user their top ten songs.
# Shows the user the number of the artist's albums and asks if they want to see a list. If so, it shows them.
# Gives the user the option to search for another artist. 
# Tells the user who has more albums.
# Author: Divi Newton
# Email:  dnewton@chapman.edu
# Date:  3/18/2025

# The documentation for the Spotify API can be cound at
# https://developer.spotify.com/documentation/web-api/concepts/api-calls

#  Spotify developer information
client_id = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
client_secret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

# get Spotify token to access the Spofity API (valid for 10 minutes)
def get_token():
    auth_string = client_id + ":" + client_secret
    auth_bytes = auth_string.encode("utf-8")
    auth_base64 = str(base64.b64encode(auth_bytes), "utf-8")

    url = "https://accounts.spotify.com/api/token"
    headers = {
        "Authorization": "Basic " + auth_base64,
        "Content-Type": "application/x-www-form-urlencoded",
    }
    data = {"grant_type": "client_credentials"}
    result = post(url, headers=headers, data=data)
    json_result = json.loads(result.content)
    token = json_result["access_token"]
    return token


# header to use in functions that retrieve info from the API
def get_auth_header(token):
    return {"Authorization": "Bearer " + token}


# this function returns an artists record that includes the artist_id
def search_for_artist(token, artist_name):
    url = "https://api.spotify.com/v1/search"
    query = f"?q={artist_name}&type=artist&limit=1"
    query_url = url + query
    headers = get_auth_header(token)
    result = get(query_url, headers=headers)
    json_result = json.loads(result.content)["artists"]["items"]

    if len(json_result) == 0:
        print("no artist with this name exists...")
        return None

    return json_result[0]


# this function returns a record that includes the the artist's top ten songs
def get_songs_by_artist(token, artist_id):
    url = f"https://api.spotify.com/v1/artists/{artist_id}/top-tracks?country=US"
    headers = get_auth_header(token)
    result = get(url, headers=headers)
    json_result = json.loads(result.content)["tracks"]
    return json_result


# this function returns a record that includes the the artist's albums
# note: only albums are returned, not singles or features
def get_albums_by_artist(token, artist_id):
    url = f"https://api.spotify.com/v1/artists/{artist_id}/albums?limit=50&include_groups=album"
    headers = get_auth_header(token)
    result = get(url, headers=headers)
    json_result = json.loads(result.content)["items"]
    return json_result


# this function returns the number of albums an artist has
def get_number_albums(token, artist_id):
    json_answer = get_albums_by_artist(token, artist_id)
    if len(json_answer) == 50:
        return "over 50"
    return len(json_answer)


# this function determines who out of two artists has more albums
def more_albums(current_artist_name, last_artist_name, current_num_albums, old_num_albums):
    if last_artist != "none":
        print(" ")
        if current_num_albums > old_num_albums:
            print(current_artist_name + " has more albums than " + last_artist_name + "!")
        elif current_num_albums < old_num_albums:
            print(last_artist_name + " has more albums than " + current_artist_name + "!")
        else:
            print(current_artist_name + " and " + last_artist_name + " have the same number of albums!")


# the main program starts here
print(" ")
print("SPOTIFY ARTIST STATS")
print(" ")
print("Welcome to Spotify Artist Stats! Get all the info on your favorite artists here.")
print(" ")

# additonal variables
last_artist = "none"
last_artist_num = 0

# get the token
token = get_token()

# main loop
while True:
    # find the id of an artist
    artist_name = input("Enter the name of an artist: ")
    result = search_for_artist(token, artist_name)
    artist_id = result["id"]
    print(" ")

    # display top tracks
    print("Here are " + artist_name + "'s top 10 tracks right now:")
    songs = get_songs_by_artist(token, artist_id)
    for idx, song in enumerate(songs):
        print(f"{idx + 1}. {song['name']}")
    print(" ")

    # display albums and amount if requested
    num_albums = get_number_albums(token, artist_id)
    print(artist_name, "has", num_albums, "albums!")
    album_question = input("Would you like to see albums by " + artist_name + "? (type yes or no) ")
    if album_question == "yes":
        albums = get_albums_by_artist(token, artist_id)
        print(" ")
        print("Here are " + artist_name + "'s albums:")
        for idx, album in enumerate(albums):
            print(f"{idx + 1}. {album['name']}")

    # print who has more albums
    more_albums(artist_name, last_artist, num_albums, last_artist_num)
    last_artist = artist_name
    last_artist_num = num_albums

    # prompt for another artist
    print(" ")
    continue_ans = input("Would you like to see stats for another artist? (type yes or no) ")
    if continue_ans == "no":
        break
    print(" ")

# ending messages
print(" ")
print("Thank you for visiting Spotify Artist Stats! Have a great day!")