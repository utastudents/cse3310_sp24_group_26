package com.cse3310;

class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int[] getCoordinate(){
        int[] location = new int[] {this.x, this.y};
        return location;
    }
}