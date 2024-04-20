package com.cse3310;

class Coordinate {
    public int row;
    public int col;

    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int[] getCoordinate(){
        int[] location = new int[] {this.row, this.col};
        return location;
    }
}