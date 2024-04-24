package com.cse3310;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GameTest {
    Game game = new Game();
    User user = new User("user");
    ArrayList<User> users = new ArrayList<>();
    /**
     * if a player's score equals 4 their wins should equal 4
     */
    @Test
    public void addFourToScoreAndWins()
    {
        users.add(user);
        user.gameWon+=4;
        game.checkWin(user);
        //assertTrue(users.contains(user));
        assertEquals(4, user.gameWon);
        
    }
}
