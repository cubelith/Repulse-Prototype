package com.example.kuba.repulsev001;

import android.graphics.Rect;

/**
 * Created by Kuba on 2017-10-16.
 */

public abstract class DataBank {


    /**
     * Coordinates of the part of the screen for playing the match
     */
    private static Rect gameField;
    /**
     * An unit of measure, useful for adjusting the size of elements for a particular device
     */
    private static float unit;



    public static float getUnit(){
        return unit;
    }

    public static void setUnit(float u){
        unit=u;
    }

    public static Rect getGameField(){
        return new Rect(gameField);
    }

    public static void setGameField(Rect r) {
        gameField=r;
    }
}
