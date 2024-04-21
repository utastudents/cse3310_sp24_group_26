package com.cse3310;

public class Lobby {
    public String user;
    public boolean ready;
    public Lobby(User user){
        this.user = user.username;
        this.ready = false;
    }
}
