package com.example.kuba.repulsev001;

import android.graphics.Canvas;

/**
 * Created by Kuba on 2017-10-29.
 * A circular entity
 */

public class GameEntityCircle implements GameObject {

    /**
     * Says whether the object actually exists
     */
    protected boolean alive;

    protected float x,y,vx,vy;
    protected float radius;

    protected float gravityX,gravityY;

    //protected boolean bouncing;




    public float getRadius(){
        return radius;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public boolean isAlive() {
        return alive;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getGravityX() {
        return gravityX;
    }

    public float getGravityY() {
        return gravityY;
    }

    /*public boolean isBouncing() {
        return bouncing;
    }*/

    /**
     * Draws the object on a given Canvas
     *
     * @param canvas the Canvas to draw on
     */
    @Override
    public void draw(Canvas canvas, float timeScale) {
        throw new UnsupportedOperationException("You have to override the run and update methods of GameObjectCircle in each subclass");
    }

    /**
     * Updates the object's state by one frame
     */
    @Override
    public void update(float timeScale) {
        throw new UnsupportedOperationException("You have to override the run and update methods of GameObjectCircle in each subclass");
    }
}
