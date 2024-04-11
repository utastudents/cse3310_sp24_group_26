package com.cse3310;


public class User
{
    private String username;
    public int wordCount;
    public int gameWon;

    public User(String username)
    {
        this.username = username;
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
