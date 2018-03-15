package com.example.kuba.repulsev001;

import android.graphics.Rect;

/**
 * Keeps our own constants and static functions
 */

public abstract class DataBank {


    /**
     * Coordinates of the part of the screen for playing the match
     */
    private static Rect gameField;
    /**
     * An unit of measure, useful for adjusting the size of elements for a particular device
     * TEMP
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

    /**
     * Checks when will obj1 collide with obj2 (time given in ticks)
     * Not finished (doesn't consider bouncing off walls)  (todo)
     * -1 means never
     * @param
     * @return
     */

}
