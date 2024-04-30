package com.cse3310;

import java.util.Vector;
import java.util.ArrayList;

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
    ArrayList<Integer> completedButtons;
    // int Button[][];

    public UserEvent() {

    }

    public UserEvent(int request, int GameId, String UserId) {
        this.request = request;
        this.GameId = GameId;
        this.UserId = UserId;
    }
}
