package com.example.kuba.repulsev001;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * A simple bullet
 */

public class Bullet extends GameEntityCircle {



    private Rect gameField;

    /**
     * True for the bullet next to disappear
     */
    private boolean dying;

    /**
     * If true, the bullet shouldn't kill the player.
     * Stays true until the bullet gets outside of the player for the first time.
     */
    private boolean inPlayer;

    /**
     * Animates the bullet, should be upgraded slightly soon
     */
    private static Animation tempAnimation,tempDecay;

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
        this.gravityY=gravity;
        gameField=DataBank.getGameField();
        dying=false;

        //loading the pictures for our temporary animations
        BitmapFactory bf = new BitmapFactory();

        Bitmap[] tempAnim = new Bitmap[5];
        tempAnim[0] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempanimation_0);
        tempAnim[1] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempanimation_1);
        tempAnim[2] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempanimation_2);
        tempAnim[3] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempanimation_3);
        tempAnim[4] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempanimation_4);

        tempAnimation = new Animation(tempAnim,0.1f);
        tempAnimation.play();

        Bitmap[] tempDec = new Bitmap[5];
        tempDec[0] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempdecay_0);
        tempDec[1] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempdecay_1);
        tempDec[2] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempdecay_2);
        tempDec[3] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempdecay_3);
        tempDec[4] = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.bullet_tempdecay_4);

        tempDecay = new Animation(tempDec,0.1f);
        tempDecay.play();
    }

    /**
     * Draws the object on a given Canvas
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas, float timeScale) {
        /*Paint paint = new Paint();

        if(dying){
            paint.setColor(Color.rgb(255, 63, 0));
        }
        else {
            paint.setColor(Color.rgb(255, 191, 0));
        }
        canvas.drawCircle(x,y,radius,paint);*/
        if(dying){
            tempDecay.update();
            tempDecay.draw(canvas, new Rect((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius)));
        }
        else {
            tempAnimation.update();
            tempAnimation.draw(canvas, new Rect((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius)));
        }
    }

    /**
     * Applies all kinds of effects, such as gravity or bullet collisions
     * Use before <code>update()</code>
     */
    public void affect(float timeScale){
        vy += gravityY;
    }

    /**
     * Updates the object's state by one frame
     * Returns true if alive
     */
    @Override
    public void update(float timeScale) {
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
