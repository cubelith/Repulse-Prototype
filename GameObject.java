package com.example.kuba.repulsev001;


import android.graphics.Canvas;

/**
 * A simple interface, that states the basic functions of each object in the game
 * @author Kuba
 * @since 0.0.1
 */
public interface GameObject {

    /**
     * Draws the object on a given Canvas
     *
     * @param canvas   the Canvas to draw on
     */
    public void draw(Canvas canvas);


    /**
     * Updates the object's state by one frame
     */
    public void update();
}
