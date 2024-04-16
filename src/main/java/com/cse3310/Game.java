package com.cse3310;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Game {
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

        Random rand = new Random();
        int length = 25;
        int correctWords = 15;
        int width = 25;
        char[][] grid = new char[50][50];
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String insertWord; 
        int orientation; //Integer to determine what direction the word will be printed
        int startY;
        int startX;
        int dX;
        int dY;
        boolean fits;
       //Input words into the array
        wordBank = new ArrayList<String>();

         for(int a = 0; a < correctWords; a ++)
        {
            wordBank.add(words.get(rand.nextInt(words.size())).toUpperCase());
            System.out.println(wordBank.get(a));
        }

        for(int b = 0; b < correctWords; b++)
        {
            insertWord = wordBank.get(b);
            do
            {
                fits = true;
                startX = rand.nextInt(length - 1);
                startY = rand.nextInt(width - 1);
                orientation = rand.nextInt(8);
                dX = orientation / 3 - 1;
                dY = orientation % 3 - 1;
                for(int d = 0; d < insertWord.length(); d++ )
                {
                    if(startX + (d * dX) > length - 1 || startX + (d * dX) < 0 || startY + (d * dY) > width - 1 || startY + (d * dY) < 0 || grid[startY + (d * dY)][startX + (d * dX)] != 0)
                    {
                        fits = false;
                        
                    }
                }
            }while(fits == false);


            for(int c = 0; c < insertWord.length() ; c++)
                {
                    grid[startY + (c * dY)][startX + (c * dX)] = insertWord.charAt(c);
                }

                
            

            
            
        }

        for(int i = 0;i < width; i++)
        {
            for(int j = 0; j < length; j++)
            {   
                if(grid[i][j] == 0)
                {
                    System.out.printf("| |");
                    //grid[i][j] = alphabet.charAt(rand.nextInt(alphabet.length()));
                }
                else
                {
                    System.out.printf("|" + grid[i][j] + "|");

                }
                
            }
            System.out.println();
        }
        //System.out.println(words.size());
        //Fill in wordbank
        

        ///Write over board with chosen words in random orientation
        

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
