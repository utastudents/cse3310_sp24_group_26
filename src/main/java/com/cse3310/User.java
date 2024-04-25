package com.cse3310;

import org.java_websocket.WebSocket;

public class User
{
    public String username;
    public int wordCount;
    public int gameWon;
    public int gamesLost;
    public int totalGamesPlayed;

    public String color;
    public WebSocket conn;


    public User(String username, WebSocket conn)
    {
        this.username = username;
        this.conn = conn;
        this.wordCount = 0;
        this.gameWon = 0;
        this.totalGamesPlayed = 0;
        this.gamesLost = totalGamesPlayed-gameWon;
    }

    public void setName(String newUsername){

        this.username = newUsername;
        
    }

    public String getName()
    {
        return username;
    }

}
