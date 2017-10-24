package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
//import android.provider.ContactsContract;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * The first game mode, with bullets bouncing of the walls and nothing more.
 * In the basic version you may not touch the walls or be hit by a bullet,
 * and the score is the number of bullets shot before death.
 */

public class GMBouncer implements GameMode {

    private Player player;
    private ArrayList<Bullet> bullets;
    private int bulletsShot;
    /**
     * States approximatelly how long a bullet stays on the screen.
     * The formula for the amount of bullets on screen is:
     * bulletsOnScreen=bulletsShot^bulletLifetime
     * So bulletLifetime may not be less than 0 or greater than 1.
     */
    private float bulletLifetime;
    /**
     * Whether walls/bullets should kill the player or not.
     * Setting bulletsKill to false makes sense only for debugging.
     */
    private boolean wallsKill,bulletsKill;

    private float unit;

    private Rect gameField;
    private int lastScore;    //temp


    /**
     * For now, initialises a default game
     */
    public GMBouncer(){
        //initialisation
        unit = DataBank.getUnit();
        gameField= DataBank.getGameField();

        wallsKill = true;
        bulletsKill = true;

        bulletLifetime = (float)0.75;      //TEMP
        player = new Player(Constants.screenWidth/2,Constants.screenHeight/2,22*unit,25,0*unit,wallsKill,bulletsKill);              //TEMP i to bardzo
        player.setShotParams(5*unit,10*unit,7*unit,0*unit);        //TEMP i to bardzo
        resetMatch();
    }
    @Deprecated
    public GMBouncer(Player player){   ///TEMP
        //initialisation
        unit = DataBank.getUnit();
        gameField= DataBank.getGameField();

        wallsKill = player.getWallsKill();
        bulletsKill = player.getBulletsKill();

        bulletLifetime = (float)0.75;      //TEMP
        this.player=player;
        bullets=new ArrayList<Bullet>();
        //bullets.add(new Bullet());

        bulletsShot=0;
    }

    /**
     * This may later be removed, we'll see. It resets the positions etc. to starting values
     */
    public void resetMatch(){
        //System.out.println("UNIT: "+unit);
                             //TEMP
        lastScore=getScore();
        player.reset((gameField.left+gameField.right)/2,(gameField.top+gameField.bottom)/2);




        bullets=new ArrayList<Bullet>();
        //bullets.add(new Bullet());

        bulletsShot=0;
    }

    /**
     * Updates the match by one frame
     */
    public void update() {

        bulletsShot+=player.retrieveShotCounter();
        if(bullets.size() > Math.pow(bulletsShot+1,bulletLifetime)){
            bullets.get(0).setDying(true);
        }
         if(bullets.size() - 1 > Math.pow(bulletsShot,bulletLifetime)){
            bullets.remove(0);
        }

        if(!player.getAlive()){
            resetMatch();
        }
        for(Bullet bullet:bullets) {
            bullet.affect();
        }
        player.affect(bullets);

        for(Bullet bullet:bullets) {
            bullet.update();
        }
        player.update();
    }

    /**
     * Draws the match on the canvas
     *
     * @param canvas the canvas to draw on
     */
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.rgb(0,0,0));

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //paint.setColor(Color.YELLOW);
        if(wallsKill) {
            paint.setColor(Color.RED);
            //System.out.println("RED: "+wallsKill);
        }
        else {
            paint.setColor(Color.CYAN);
            //System.out.println("CYAN"+wallsKill);
        }
        paint.setStrokeWidth(3);
        canvas.drawRect(DataBank.getGameField().left+3,DataBank.getGameField().top+3,DataBank.getGameField().right-3,DataBank.getGameField().bottom-3,paint);

        player.draw(canvas);
        for(Bullet bullet:bullets) {
            bullet.draw(canvas);
        }
        if(!player.getAlive()){
            canvas.drawColor(Color.rgb(127,0,0));
        }
    }

    /**
     * Returns the score achieved in the match so far.
     * @return the score
     */
    public int getScore() {
        return bulletsShot;
    }

    /**
     * Called on a click (just at the moment of touching the screen)
     */
    public void mouseDown(MotionEvent event){
        player.mouseDown(event);
    }

    /**
     * Called after releasing the finger
     */
    public void mouseUp(MotionEvent event) {

    }

    @Override
    public void setBulletLifetime(float v) {
        bulletLifetime=v;
    }

    @Override
    public int getLastScore() {
        return lastScore;
    }


}
