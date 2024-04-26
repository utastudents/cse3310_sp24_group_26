package com.cse3310;

import java.util.Vector;

public class UserEvent {
    int request;
    int GameId;
    String UserId;
    String chatMessage;
    String color;
    int gamesWon;
    int gamesLost;
    int buttonId;
    Vector<Lobby> LobbyUsers;
    // int Button[][];

    public UserEvent() {

    }

    public UserEvent(int request, int GameId, String UserId) {
        this.request = request;
        this.GameId = GameId;
        this.UserId = UserId;
    }
}
