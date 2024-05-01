package com.cse3310;

public class Winner {
    int won;
    String winner;
    int score;

    public Winner(User u) {
        this.won = 1;
        this.winner = u.username;
        this.score = u.wordCount;
    }
}
