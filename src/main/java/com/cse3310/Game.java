package com.cse3310;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Game {

    public int GameId;
    public char[][] grid;
    public ArrayList<String> wordBank;
    public ArrayList<Integer> startIds;
    public ArrayList<Integer> endIds;
    public ArrayList<Integer> ActiveButtons;
    public ArrayList<Integer> CompletedButtons;

    public Game(ArrayList<String> words, int GameId) {
        this.wordBank = new ArrayList<String>();
        this.startIds = new ArrayList<Integer>();
        this.endIds = new ArrayList<Integer>();
        this.ActiveButtons = new ArrayList<Integer>();
        this.CompletedButtons = new ArrayList<Integer>();

        this.GameId = GameId;
        this.grid = generateGrid(words, wordBank);
    }

    public int isEnd(int id) {
        for (int endId : endIds) {
            if (id == endId) {
                return 0;
            }
        }

        return -1;
    }

    public ArrayList<Integer> getCompletedButtons(int startId, int endId) {
        ArrayList<Integer> completedButtons = new ArrayList<>();

        if (Math.abs((endId - startId)) < 20) { // Horizontal Check
            int i = startId;
            while (i != endId) {
                completedButtons.add(i);
                i++;
            }
            completedButtons.add(i);
        } else if ((endId % 10) == (startId % 10)) // Vertical Check
        {
            if (startId < endId) { // From top to bottom
                int i = startId;
                while (i != endId) {
                    completedButtons.add(i);
                    i += 20;
                }
                completedButtons.add(i);
            } else { // From bottom to top
                int i = startId;
                while (i != endId) {
                    completedButtons.add(i);
                    i -= 20;
                }
                completedButtons.add(i);
            }
        } else { // Diagonal Check
            if (startId < endId) {
                int i = startId;
                while (i != endId) {
                    completedButtons.add(i);
                    i += 21;
                }
                completedButtons.add(i);
            } else {
                int i = startId;
                while (i != endId) {
                    completedButtons.add(i);
                    i -= 19;
                }
                completedButtons.add(i);
            }
        }

        return completedButtons;
    }

    public char[][] generateGrid(ArrayList<String> words, ArrayList<String> wordbank) {

        Random rand = new Random();
        int length = 20;
        int width = 20;
        int areaPortion = 5;
        char[][] grid = new char[width][length];
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String insertWord;
        int orientation = 0; // Integer to determine what direction the word will be printed
        int startY, startX, finalY, finalX;
        finalY = finalX = 0;
        int dX = 0;
        int dY = 0;
        int tries = 0;
        boolean fits;
        int validWordsLetters = 0; // For calculating word density
        int[][] directions = { { 1, 1 }, { -1, 1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
        double density = .67; // density of grid
        int maxLength = 10;
        // Input words into the array
        // Take random words from the word list to put inside a word bank
        int index = 0;
        while (((double) validWordsLetters / (length * width)) < density) {

            String word = words.get(rand.nextInt(words.size())).toUpperCase();
            while (word.length() > maxLength) {
                word = words.get(rand.nextInt(words.size())).toUpperCase();
            }
            wordBank.add(word);
            tries = 0;
            insertWord = wordBank.get(index);

            do {
                fits = true;
                startX = rand.nextInt(length);
                startY = rand.nextInt(width);
                orientation = rand.nextInt(4);
                dX = directions[orientation][1];
                dY = directions[orientation][0];
                int secondCheck = 0;

                for (int d = 0; d < insertWord.length(); d++) {
                    // Check letter by letter if word goes out of bounds of array or writes over an
                    // existing one
                    if (startX + (d * dX) > length - 1 || startX + (d * dX) < 0 || startY + (d * dY) > width - 1
                            || startY + (d * dY) < 0 || grid[startY + (d * dY)][startX + (d * dX)] != 0) {

                        if (secondCheck == 5) {
                            fits = false;
                            tries++;
                            break;

                        } else // check every possible direction from starting coordinate
                        {
                            d = -1;
                            dX = directions[secondCheck][1];
                            dY = directions[secondCheck][0];
                            secondCheck++;
                        }
                    }
                }
            } while (fits == false && tries < 100);
            if (tries >= 100) {
                fits = false;
                wordBank.remove(index);
            }

            if (fits == true) {
                for (int c = 0; c < insertWord.length(); c++) {
                    grid[startY + (c * dY)][startX + (c * dX)] = insertWord.charAt(c);
                    finalX = startX + (c * dX);
                    finalY = startY + (c * dY);
                    // System.out.println("Putting letter " + insertWord.charAt(c) + " of word " +
                    // insertWord + " at index " + y + " " + x);
                }
                int startId = (startY * length) + startX;
                int endId = (finalY * length) + finalX;

                startIds.add(startId);
                endIds.add(endId);

                System.out.println("Start: " + startId + "\n" + "End: " + endId);

                System.out.println(index + "." + wordBank.get(index));
                validWordsLetters = validWordsLetters + wordBank.get(index).length();
                index++;
            }

        }
        System.out.println(" Actual Density: " + (double) validWordsLetters / (length * width));
        // Fill in rest of the grid with random letters
        // Print grid
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                if (grid[i][j] == 0) {

                    grid[i][j] = alphabet.charAt(rand.nextInt(alphabet.length()));
                }

                System.out.printf("" + grid[i][j] + "|");

            }
            System.out.println();
        }

        return grid;

    }

    public void checkWin(User user) {

    }

    public String keepScore(ArrayList<User> score) {
        return "";
    }

    public void Tick() {

    }
}
