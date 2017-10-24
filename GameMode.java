package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * An interface for every game (every "match").
 */

public interface GameMode {

    /**
     * Updates the match by one frame
     */
    public void update();

    /**
     * Draws the match on the canvas
     *
     * @param canvas   the canvas to draw on
     */
    public void draw(Canvas canvas);

    /**
     * Returns the score achieved in the match so far.
     * Should mostly be used after death.
     * @return the score
     */
    public int getScore();

    /**
     * Called on a click (just at the moment of touching the screen)
     */
    public void mouseDown(MotionEvent event);

    /**
     * Called after releasing the finger
     */
    public void mouseUp(MotionEvent event);

    @Deprecated  ///TEMP
    void setBulletLifetime(float v);

    @Deprecated ///temp
    int getLastScore();
}
