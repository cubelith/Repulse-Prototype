package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * The basic player class in Repulse.
 * This class displays and moves the player, and also manages all other actions connected to them,
 * such as checking collisions or shooting.
 *
 * @author Kuba
 */

public class Player implements GameObjectCircle{

    private boolean alive;
    /**
     * Position and speed coordinates
     */
    private float x,y,vx,vy;
    private float radius;
    private Rect gameField;
    private int cooldown,cooldownTime;
    private float gravity;

    /**
     * Strength of recoil and the speed of bullets
     */
    private float recoilSpeed,bulletSpeed;
    private float bulletRadius;
    private float bulletGravity;

    //private Bullet nextShot;
    private ArrayList<Bullet> bullets;
    private boolean wallsKill,bulletsKill;

    private int shotCounter;


    public Player(float x,float y,float radius,int cooldownTime,float gravity, boolean wallsKill,boolean bulletsKill){
        this.wallsKill = wallsKill;
        this.bulletsKill = bulletsKill;
        this.alive=true;
        this.x=x;
        this.y=y;
        vx=0;
        vy=0;
        this.gameField=DataBank.getGameField();
        this.radius=radius;
        this.cooldown=0;
        this.cooldownTime=cooldownTime;
        this.gravity=gravity;
        shotCounter=0;
    }

    public void setShotParams(ArrayList<Bullet> bullets,float recoilSpeed,float bulletSpeed,float bulletRadius,float bulletGravity){
        this.recoilSpeed=recoilSpeed;
        this.bulletSpeed=bulletSpeed;
        this.bulletRadius=bulletRadius;
        this.bulletGravity=bulletGravity;
        this.bullets=bullets;
    }
    public void setShotParams(float recoilSpeed,float bulletSpeed,float bulletRadius,float bulletGravity){
        this.recoilSpeed=recoilSpeed;
        this.bulletSpeed=bulletSpeed;
        this.bulletRadius=bulletRadius;
        this.bulletGravity=bulletGravity;
    }


    /**
     * Displays the player.
     *
     * @param canvas     <code>Canvas</code> to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        //System.out.println(x+", "+y+" : "+radius);

        paint.setColor(Color.rgb(0,0,255));
        canvas.drawCircle(x,y,radius,paint);
        paint.setColor(Color.rgb(0,255,255));
        canvas.drawArc(new RectF(x-radius,y-radius,x+radius,y+radius),-90,(float)(360*(1.0-1.0*cooldown/cooldownTime)),true, paint); //could be funny with false
        paint.setColor(Color.rgb(255,0,127));
        canvas.drawCircle(x,y,radius/2,paint);
        //System.out.println("Player");

        //canvas.drawColor(Color.CYAN);
    }

    /**
     * Applies all kinds of effects, such as gravity or bullet collisions
     * Also processes how the player affects the world
     * Use before <code>update()</code>
     */
    public void affect() {
        vy += gravity;

        if (bulletsKill) {
            for (Bullet bullet : bullets) {
                int col=(int)DataBank.collide(this,bullet);
                if (col == 0) {
                    if (!bullet.getInPlayer()) {
                        alive = false;
                    }
                } else {
                    bullet.setInPlayer(false);
                }
            }
        }
//        if(nextShot!=null){
//            bullets.add(nextShot);
//            nextShot=null;
//        }
    }

    /**
     * Checks when the player will collide with the given bullet.
     * Only looks forward one bounce of player and one bounce of bullet
     * @param bullet   the bullet to collide with
     * @return         the time until collision (in ticks). Returns -1 for "never"
     * 0 is the current tick
     */
    /// Attention - heuristic for now, does not include bouncing off walls
    /// The javadoc sucks too
    /*private int collide(Bullet bullet){
        float dr=radius+bullet.getRadius();
        float dx=x-bullet.getX();
        float dy=y-bullet.getY();
        float dvx=vx-bullet.getVx();
        float dvy=vy-bullet.getVy();
        float delta=(float)Math.pow(2*(dx*dvx+dy*dvy),2)-4*(dvx*dvx+dvy*dvy)*(dx*dx+dy*dy-dr*dr);
        if(delta<0){
            return -1;
        }
        else{
            float t1=(float)(-2*(dx*dvx+dy*dvy)-Math.sqrt(delta))/(2*(dvx*dvx+dvy*dvy));
            if(t1<0){
                float t2=(float)(-2*(dx*dvx+dy*dvy)+Math.sqrt(delta))/(2*(dvx*dvx+dvy*dvy));
                if(t2<0){
                    return -1;
                }
                else{
                    return 0;
                }
            }
            else if(t1==0){
                return 0;
            }
            else if(t1>0){
                return (int)Math.floor(t1);
            }
        }
        //this should be unreachable anyway
        return -1;
    }*/

    /**
     * Updates the player's state by one frame
     * Call always after <code>affect</code>
     */
    @Override
    public void update() {
        x+=vx;
        y+=vy;
        if(wallsKill){
            if(x - radius <= gameField.left || x + radius >= gameField.right || y - radius <= gameField.top || y + radius >= gameField.bottom){
                alive=false;
            }
        }
        else {
            while (x - radius <= gameField.left || x + radius >= gameField.right) {
                if (x - radius <= gameField.left) {
                    x += 2 * (gameField.left - (x - radius));
                    vx = -vx;
                }
                if (x + radius >= gameField.right) {
                    x -= 2 * ((x + radius) - gameField.right);
                    vx = -vx;
                }
            }
            while (y - radius <= gameField.top || y + radius >= gameField.bottom) {
                if (y - radius <= gameField.top) {
                    y += 2 * (gameField.top - (y - radius));
                    vy = -vy;
                }
                if (y + radius >= gameField.bottom) {
                    y -= 2 * ((y + radius) - gameField.bottom);
                    vy = -vy;
                }
            }
        }
        cooldown--;
    }


    public float getRadius() {
        return radius;
    }


    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }

    @Override
    public float getVx() {
        return vx;
    }

    @Override
    public float getVy() {
        return vy;
    }

    public void mouseDown(MotionEvent event) {
        if(cooldown<=0){
            shoot(event.getX(),event.getY());
            cooldown=cooldownTime;
        }
    }

    /**
     * Schedules a shot for the next frame
     */
    private void shoot(float mx,float my){
        if(alive) {
            shotCounter++;
            double ang = Math.atan2(y - my, x - mx);
            //ang = -ang;
            ang += Math.PI;
            float dx = (float) (radius * Math.cos(ang) + x);
            float dy = (float) (radius * Math.sin(ang) + y);
            float dvx = (float) (bulletSpeed * Math.cos(ang));
            float dvy = (float) (bulletSpeed * Math.sin(ang));
            bullets.add(new Bullet(dx, dy, dvx, dvy, bulletRadius, bulletGravity));
            vx -= (recoilSpeed * Math.cos(ang));
            vy -= (recoilSpeed * Math.sin(ang));
        }
    }

    public boolean getAlive() {
        return alive;
    }

    public int retrieveShotCounter(){
        int r=shotCounter;
        shotCounter=0;
        return r;
    }

    public void reset(int dx, int dy) {
        x=dx;
        y=dy;
        vx=0;
        vy=0;
        alive=true;
        cooldown=0;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    public boolean getWallsKill() {
        return wallsKill;
    }

    public boolean getBulletsKill() {
        return bulletsKill;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }
}
