package com.cse3310;

import java.util.ArrayList;

public class Game {
    private ArrayList<User> users;
    public int GameId;
    public char[][] grid;
    
    public Game() {
        grid = new char[50][50];
    }
    
    public boolean checkVertical(ArrayList<User> selectedLetters, int length){
        return true;
    }

    public boolean checkDiagonal(ArrayList<User> selectedLetters, int length){
        return true;
    }

    public boolean checkHorizontal(ArrayList<User> selectedLetters, int length){
        return true;
    }

    public void Update(UserEvent U){

    }

    public void checkWin(User user){

    }

    public String keepScore(ArrayList<User> score){
        return "";
    }

    public void Tick(){

    }
}
