package com.cse3310;

import java.util.HashMap;

class Locations {
    public HashMap <String, Coordinate[]> locationMap;

    public int[] getStart(String s){
        return (locationMap.get(s)[0]).getCoordinate();
    }

    public int[] getEnd(String s){
        return (locationMap.get(s)[1]).getCoordinate();
    }
}