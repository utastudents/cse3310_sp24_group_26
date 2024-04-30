package com.cse3310;

import java.util.Vector;

public class PlayerList {
    public Vector<String> players;
    public Vector<Integer> playerScores;
    public Vector<String> playerColors;

    public PlayerList() {
        this.players = new Vector<String>();
        this.playerScores = new Vector<Integer>();
        this.playerColors = new Vector<String>();
    }
}
