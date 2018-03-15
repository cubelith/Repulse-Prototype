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
     * Deprecated, since it does not allow for time scaling.
     * @param canvas   the Canvas to draw on
     */
    /*@Deprecated
    public void draw(Canvas canvas);*/

    /**
     * Draws the object on a given Canvas, with the given time scaling
     * @param canvas   the Canvas to draw on
     */
    public void draw(Canvas canvas,float timeScale);


    /**
     * Updates the object's state by one frame.
     * Deprecated, since it does not allow for time scaling.
     */
    /*@Deprecated
    public void update();*/

    /**
     * Updates the object's state by one frame, with the given time scaling
     */
    public void update(float timeScale);

    /**
     * Checks whether the object is alive (exists)
     */
    public boolean isAlive();



    //float getVx();

    //float getVy();

    //float getGx();

    //float getGy();
}
