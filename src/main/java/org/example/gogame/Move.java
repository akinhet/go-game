package org.example.gogame;

public class Move {
    private int x;
    private int y;
    private StoneColor color;

    public Move(int x, int y, StoneColor color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public StoneColor getColor(){
        return color;
    }
}
