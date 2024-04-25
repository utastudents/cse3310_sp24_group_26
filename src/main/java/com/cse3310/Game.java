package com.cse3310;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Game {
    //public ArrayList<User> users;
    public int GameId;
    public char[][] grid;
    public ArrayList<String> wordBank;

    public Game(ArrayList<String> words)
    {
        //users = new ArrayList<User>();
        wordBank = new ArrayList<String>();
        this.GameId = 0;
        this.grid = generateGrid(words,wordBank);
    }
    
    public boolean checkVertical(ArrayList<String> selectedLetters,int startX, int startY, int length){
        return true;
    }

    public boolean checkDiagonal(ArrayList<String> selectedLetters,int startX, int startY, int length){
        return true;
    }

    public boolean checkHorizontal(ArrayList<String> selectedLetters,int startX, int startY, int length){
        return true;
    }

    public char[][] generateGrid(ArrayList<String> words,ArrayList<String> wordbank){

        Random rand = new Random();
        int length = 20;
        int width = 20;
        char[][] grid = new char[width][length];
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String insertWord; 
        int orientation; //Integer to determine what direction the word will be printed
        int startY, startX, dX, dY,finalY,finalX;
        int tries;
        boolean fits;
        int validWordsLetters = 0; // For calculating word density
        int[][] directions = {{1,1} , {-1,1} , {0,1} ,{-1,0}, {1,0}};
        double density = .67; // density of grid
        int maxLength = 10;
        float diagUp,diagDown,horizontals,vertUp,vertDown; // stats to see orientations
        diagUp = diagDown = horizontals = vertUp = vertDown = 0;
        //Input words into the array
        //Take random words from the word list to put inside a word bank
        int index = 0;
        int overlap = 0;
        while(((double)validWordsLetters / (length * width)) < density)
        {
            //Grab a word and add it to a word length
            String word = words.get(rand.nextInt(words.size())).toUpperCase();
            while(word.length() > maxLength){
                word = words.get(rand.nextInt(words.size())).toUpperCase();
            }
            wordBank.add(word);
            tries = 0;
            insertWord = wordBank.get(index);

             do
            {
                fits = true;
                startX = rand.nextInt(length);
                startY = rand.nextInt(width);
                orientation = rand.nextInt(5);
                dX = directions[orientation][1];
                dY = directions[orientation][0];
                int secondCheck = 0;

                for(int d = 0; d < insertWord.length(); d++ )
                {
                    //Check letter by letter if word goes out of bounds of array or writes over an existing one
                    if(startX + (d * dX) > length - 1|| startX + (d * dX) < 0 || startY + (d * dY) > width - 1
                        || startY + (d * dY) < 0 || grid[startY + (d * dY)][startX + (d * dX)] != 0)
                    {
                        
                        if(secondCheck == 5)
                        {
                            fits = false;
                            tries++;
                            break;

                        }
                        else //check every possible direction from starting coordinate
                        {
                            d = -1;
                            dX = directions[secondCheck][1];
                            dY = directions[secondCheck][0];
                            secondCheck++;
                        }
                    }
                }

            }while(fits == false && tries < 100);
            if(tries >= 100)
            {
                fits = false;
                wordBank.remove(index);
            }

            if(fits == true)
            {
                for(int c = 0; c < insertWord.length() ; c++)
                {
                    grid[startY + (c * dY)][startX + (c * dX)] = insertWord.charAt(c);
                    finalX = startX + (c * dX);
                    finalY = startY + (c * dY);
                    //System.out.println("Putting letter " + insertWord.charAt(c) + " of word " + insertWord + " at index " + y + " " + x);
                }

                if(dY == 0)
                {
                    horizontals++;
                }
                else if(dY == 1)
                {
                    if(dX == 1)
                    diagUp++;
                    else
                    vertUp++;
                }
                else if(dY == -1)
                {   
                    if(dX == 1)
                    diagDown++;
                    else
                    vertDown++;
                }
                System.out.println(index + "." + wordBank.get(index));
                validWordsLetters = validWordsLetters + wordBank.get(index).length();
                index++;
            }
                
        }

        System.out.println("Actual Density: " + (double)validWordsLetters / (length * width));
        System.out.println("Upward Diagonals: " + (diagUp/index));
        System.out.println("Downward Diagonals: " + (diagDown/index));
        System.out.println("Horizontals: " +  (horizontals/index));
        System.out.println("Downward verticals: " + (vertDown/index));
        System.out.println("Upward verticals:" + (vertUp/index));
        //Fill in rest of the grid with random letters
        //Print grid
        for(int i = 0;i < width; i++)
        {
            for(int j = 0; j < length; j++)
            {   
                if(grid[i][j] == 0)
                {
                    
                    grid[i][j] = alphabet.charAt(rand.nextInt(alphabet.length()));
                    System.out.printf(" |");

                }
                else
                {
                    System.out.printf("" + grid[i][j] + "|");
                }
  
            }
            System.out.println();
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
