package com.cse3310;

import org.java_websocket.WebSocket;

public class User
{
    public String username;
    public int wordCount;
    public int gameWon;
    public WebSocket conn;

    public User(String username, WebSocket conn)
    {
        this.username = username;
        this.conn = conn;
        this.wordCount = 0;
        this.gameWon = 0;
    }

    public void setName(String newUsername){

        this.username = newUsername;
        
    }

    public String getName()
    {
        return username;
    }

}
