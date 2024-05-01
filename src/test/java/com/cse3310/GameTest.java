package com.cse3310;
//import static org.junit.Assert.assertArrayEquals;

import static org.junit.Assert.assertArrayEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GameTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GameTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testGame() {
        assertTrue(true);
    }

    public void EndIdCase(Game G) {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            array.add(1);
        }

        G.endIds = array;

        assertTrue(G.isEnd(1) == 0);
    }

    public void testEndIdCase() {
        ArrayList<String> stringList = new ArrayList<>();

        // Add elements to the ArrayList
        stringList.add("Apple");
        stringList.add("Banana");
        stringList.add("Orange");

        Game B = new Game(stringList, 1);

        EndIdCase(B);
    }

    public void getCompletedButtonsCase(Game G) {
        ArrayList<Integer> array = new ArrayList<>();
        array.add(6);
        array.add(7);
        array.add(8);
        array.add(9);
        array.add(10);
        array.add(11);

        ArrayList<Integer> result = G.getCompletedButtons(6, 11);

        assertTrue(result.containsAll(array) == array.containsAll(result));
    }

    public void testGetCompletedButtons() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("Apple");
        Game B = new Game(stringList, 1);

        getCompletedButtonsCase(B);
    }

    public void GenerateGrid(Game G) {
        // Create a Game object
        ArrayList<String> stringList = new ArrayList<>();

        // Add elements to the ArrayList
        stringList.add("CAT");
        char[][] result = G.generateGrid(stringList, new ArrayList<>());
        assertEquals(20, result.length);
    }

    public void testGenerateGrid() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("App");
        Game B = new Game(stringList, 1);
        GenerateGrid(B);
    }

    
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