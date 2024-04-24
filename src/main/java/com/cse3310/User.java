package com.cse3310;


public class User
{
    public String username;
    public int wordCount;
    public int gameWon;
    public int gamesLost;
    public int totalGamesPlayed;

    public User(String username)
    {
        this.username = username;
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
