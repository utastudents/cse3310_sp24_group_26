package com.cse3310;
//import static org.junit.Assert.assertArrayEquals;

import org.java_websocket.WebSocket;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public WebSocket conn;
    User user1 = new User(" ", conn);
    User user2 = new User(" ", conn);
    Vector<Game> ActiveGames = new Vector<Game>();
    Vector<User> ActiveUsers = new Vector<User>();

    public User findWinner(int GameId) {
        int totalWordCount = 0;
        Game g = ActiveGames.get(GameId);
        int most = 0;
        User winner = new User();

        for (User u : ActiveUsers) {
            if (GameId == u.GameId) {
                if (u.wordCount > most) {
                    most = u.wordCount; // Track user with highest score
                    winner = u;
                }

                totalWordCount += u.wordCount;
            }
        }

        if (totalWordCount == g.wordBank.size()) {
            return winner;
        }

        return null;
    }

    @Test
    public void testFindWinner() {
        // Create game
        Game g = new Game();
        g.GameId = 0;
        g.wordBank.add("A");
        g.wordBank.add("B");

        assertEquals(2, g.wordBank.size());

        ActiveGames.add(g);

        // Set stats
        user1.username = "user1";
        user1.GameId = 0;
        user1.wordCount = 2;

        user2.username = "user2";
        user2.GameId = 0;
        user2.wordCount = 0;

        ActiveUsers.add(user1);
        ActiveUsers.add(user2);

        // Test for winner of the game
        assertEquals(user1, findWinner(g.GameId));
    }

    public void gameWon(User winner) {
        ArrayList<User> disconnectUsers = new ArrayList<>();

        int wonGameId = winner.GameId;

        for (User u : ActiveUsers) {
            if (u.GameId == wonGameId) {
                Winner e = new Winner(winner);
                disconnectUsers.add(u);
            }
        }

        for (User u : disconnectUsers) {
            ActiveUsers.remove(u);
        }
    }

    @Test
    public void testGameWon() {
        ActiveUsers.clear();
        ActiveGames.clear();

        User winner = new User();
        winner.GameId = 0;
        user1.GameId = 0;
        user2.GameId = 0;

        ActiveUsers.add(user1);
        ActiveUsers.add(user2);
        ActiveUsers.add(winner);

        gameWon(winner);

        // Active users in the game should be cleared out
        assertEquals(0, ActiveUsers.size());
    }
}
