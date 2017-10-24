package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * here be javadoc
 */

public class Bullet implements GameObject {

    /**
     * Says whether the bullet actually exists
     */
    private boolean alive;

    private float x,y,vx,vy;
    private float radius;

    private float gravity;

    private Rect gameField;

    private boolean dying;

    /**
     * If true, the bullet doesn't kill the player.
     * Stays true until the bullet gets outside of the player for the first time.
     */
    private boolean inPlayer;

    /**
     * Initialises a non-existent bullet
     */
    public Bullet(){
        alive=false;
    }

    public Bullet(float x,float y,float vx,float vy,float radius,float gravity){
        alive=true;
        inPlayer=true;
        this.x=x;
        this.y=y;
        this.vx=vx;
        this.vy=vy;
        this.radius=radius;
        this.gravity=gravity;
        gameField=DataBank.getGameField();
        dying=false;
    }

    /**
     * Draws the object on a given Canvas
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        if(dying){
            paint.setColor(Color.rgb(255, 63, 0));
        }
        else {
            paint.setColor(Color.rgb(255, 191, 0));
        }
        canvas.drawCircle(x,y,radius,paint);
    }

    /**
     * Applies all kinds of effects, such as gravity or bullet collisions
     * Use before <code>update()</code>
     */
    public void affect(){
        vy += gravity;
    }

    /**
     * Updates the object's state by one frame
     * Returns true if alive
     */
    @Override
    public void update() {
        x+=vx;
        y+=vy;
        while(x-radius<=gameField.left || x+radius>=gameField.right){
            if(x-radius<=gameField.left){
                x += 2*(gameField.left - (x-radius));
                vx=-vx;
            }
            if(x+radius>=gameField.right){
                x -= 2*((x+radius) - gameField.right);
                vx=-vx;
            }
        }
        while(y-radius<=gameField.top || y+radius>=gameField.bottom){
            if(y-radius<=gameField.top){
                y += 2*(gameField.top - (y-radius));
                vy=-vy;
            }
            if(y+radius>=gameField.bottom){
                y -= 2*((y+radius) - gameField.bottom);
                vy=-vy;
            }
        }
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public void setInPlayer(boolean inPlayer) {
        this.inPlayer = inPlayer;
    }

    public boolean getInPlayer() {
        return inPlayer;
    }

    public void setDying(boolean d){
        dying=d;
    }
}
