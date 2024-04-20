package com.cse3310;

public class UserEvent {
    int request;
    int GameId;
    String UserId;
    //int Button[][];

    public UserEvent(){

    }

    public UserEvent(int request, int GameId, String UserId)
    {
        this.request = request;
        this.GameId = GameId;
        this.UserId = UserId;
    }
}
