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
        int correctWords = 40;
        int width = 25;
        char[][] grid = new char[width][length];
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String insertWord; 
        int orientation; //Integer to determine what direction the word will be printed
        int startY;
        int startX;
        int dX;
        int dY;
        boolean fits;
        int validWordsLetters = 0; // For calculating word density
        int[][] directions = {{1,1} , {-1,1} , {0,1} ,{-1,0}, {1,0}};
       //Input words into the array
        wordBank = new ArrayList<String>();
        //Take random words from the word list to put inside a word bank
         for(int a = 0; a < correctWords; a ++)
        {
            wordBank.add(words.get(rand.nextInt(words.size())).toUpperCase());
            System.out.println(a+"." + wordBank.get(a));
            validWordsLetters = validWordsLetters + wordBank.get(a).length();
        }
        //For every word in the wordbank
        for(int b = 0; b < correctWords; b++)
        {
            insertWord = wordBank.get(b);
            /*Algorithm is slow and cannot meet a word density of .67. 
            Consistent density is around .4 - .5, any higher can cause program 
            to be stuck*/
            do
            {
                fits = true;
              
                    startX = rand.nextInt(length - 1);
                    startY = rand.nextInt(width - 1);
                    orientation = rand.nextInt(4);
                    dX = directions[orientation][1];
                    dY = directions[orientation][0];
                
                
                for(int d = 0; d < insertWord.length(); d++ )
                {
                    //Check letter by letter if word goes out of bounds of array or writes over an existing one
                    if(startX + (d * dX) > length - 1 || startX + (d * dX) < 0 || startY + (d * dY) > width - 1 || startY + (d * dY) < 0 || grid[startY + (d * dY)][startX + (d * dX)] != 0)
                    {
                        fits = false;
                        d = insertWord.length();
                    }
                }
            }while(fits == false);


            for(int c = 0; c < insertWord.length() ; c++)
                {
                    grid[startY + (c * dY)][startX + (c * dX)] = insertWord.charAt(c);
                    int x = startX + (c * dX);
                    int y = startY + (c * dY);
                    //System.out.println("Putting letter " + insertWord.charAt(c) + " of word " + insertWord + " at index " + y + " " + x);
                }

                
            

            
            
        }
        //Fill in rest of the grid with random letters
        for(int i = 0;i < width; i++)
        {
            for(int j = 0; j < length; j++)
            {   
                if(grid[i][j] == 0)
                {
                    
                    grid[i][j] = alphabet.charAt(rand.nextInt(alphabet.length()));
                }
                
                System.out.printf("|" + grid[i][j] + "|");

                
            }
            System.out.println();
        }
        System.out.println("Validwordletters:" + validWordsLetters);
        float density = (float)validWordsLetters / (length * width);
        System.out.println("Density is " + density);
        
        

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
