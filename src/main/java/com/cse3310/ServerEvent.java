package com.cse3310;

import java.util.Vector;

public class ServerEvent {
    int request;
    Vector<Lobby> LobbyUsers;
    // int Button[][];


    public ServerEvent(int request, Vector<Lobby> LobbyUsers) {
        this.request = request;
        this.LobbyUsers = LobbyUsers;
    }
}
