package com.cse3310;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Game{
    private ArrayList<User> users;
    public int GameId;
    public char[][] grid;
    public ArrayList<String> wordBank;
    
    public Game(ArrayList<String> words)
    {
        
        grid = generateGrid(words,wordBank);
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

    public char[][] generateGrid(ArrayList<String> words,ArrayList<String> wordbank){
        char[][] grid = new char[50][50];
        Random rand = new Random();
        int length = 25;
        int width = 25;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int orientation; //Integer to determine what 
       //Generate array of random alphabet
        for(int i = 0;i < width; i++)
        {
            for(int j = 0; j < length; j++)
            {
                grid[i][j] = alphabet.charAt(rand.nextInt(alphabet.length()));
            }
        }
        System.out.println(words.size());
        wordBank = new ArrayList<String>();
        //Fill in wordbank
        
        for(int a = 0; a < 15; a ++)
        {
            //wordBank.add(words.get(rand.nextInt(words.size())));
            //System.out.println(wordBank.get(a));
        }
        //Write over board with chosen words in random orientation
        for(int b = 0; b < 15; b++)
        {
            orientation = rand.nextInt(4);
            switch(orientation)
            {
                case 0: //horizontal
                    

            }
        }

        return grid;
        
        
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