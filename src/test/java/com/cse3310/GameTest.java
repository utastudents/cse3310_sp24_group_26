package com.cse3310;
//import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GameTest {

    @Test
    public void densityIsWithinRange(){
         
        String filename = "words.txt";
        ArrayList<String> wordList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file:" + e.getMessage());
        }
        
        Game g = new Game(wordList, 0);
        
        System.out.println("DENSITY IS " + g.gridDensity);
        assertTrue(g.gridDensity >= 0.67);
    }

    @Test
    public void timeIsWithinRange(){
        
        String filename = "words.txt";
        ArrayList<String> wordList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file:" + e.getMessage());
        }

        Game g = new Game(wordList, 0);
        
        System.out.println("TIME IS " + g.gridTime);
        assertTrue(g.gridTime <= 1000);
    }

}
