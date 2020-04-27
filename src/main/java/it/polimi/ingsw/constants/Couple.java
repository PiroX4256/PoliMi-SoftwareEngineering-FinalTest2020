package it.polimi.ingsw.constants;

import java.io.Serializable;

public class Couple implements Serializable {

    private int x;
    private int y;

    public Couple(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
