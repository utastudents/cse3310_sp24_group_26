package com.cse3310;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.java_websocket.WebSocket;
import org.junit.Test;

public class GameTest {
    public WebSocket conn;
    User user = new User("user", conn);
    ArrayList<User> users = new ArrayList<>();
    /**
     * if a player's score equals 4
     */
    @Test
    public void addFourToScoreAndWins()
    {
        users.add(user);
        user.gameWon+=4;
        //game.checkWin(user);
        assertEquals(4, user.gameWon);
        
    }
}
