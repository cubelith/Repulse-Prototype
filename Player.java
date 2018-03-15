package com.example.kuba.repulsev001;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
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

public class Player extends GameEntityCircle{




    private Rect gameField;
    /**
     * current cooldown
     */
    private int cooldown;
    /**
     * full cooldown time (basically a constant)
     */
    private final int cooldownTime;


    /**
     * Strength of recoil and the speed of bullets
     */
    private float recoilSpeed,bulletSpeed;
    /**
     * Will probably replace those by a bullet template as one object
     */
    private float bulletRadius, bulletGravity;

    /**
     * Needs to take this list from the match in to add bullets to it
     */
    private ArrayList<Bullet> bullets;
    private boolean wallsKill,bulletsKill;

    private int shotCounter;

    /**
     * Temporary animation of the ship
     */
    private Bitmap tempShip0,tempShip1,tempShip2,tempShip3;
    /**
     * The current angle of the ship. 0 means facing straight upwards
     */
    private float flyingAngle;
    /**
     * Determines how quickly the ship adapts its angle to its actual flying direction
     */
    private final float turnSpeed = 0.1f;

    private float recoilScaling; //0 means no scaling, a positive number gives the distance that is 100% of recoilSpeed

    public Player(float x,float y,float radius,int cooldownTime,float gravity, boolean wallsKill,boolean bulletsKill,float recoilScal){
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
        this.gravityY=gravity;
        shotCounter=0;

        //temporary graphics loading
        BitmapFactory bf = new BitmapFactory();
        tempShip0 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.tempship_0);
        tempShip1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.tempship_1);
        tempShip2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.tempship_2);
        tempShip3 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.tempship_3);

        flyingAngle=0;
        recoilScaling=recoilScal;
    }

    /**
     * This sets tho parameters for shooting, along with taking in the list of bullets from the match
     * There was just too much stuff in the constructor
     */
    public void setShotParams(ArrayList<Bullet> bullets,float recoilSpeed,float bulletSpeed,float bulletRadius,float bulletGravity){
        this.recoilSpeed=recoilSpeed;
        this.bulletSpeed=bulletSpeed;
        this.bulletRadius=bulletRadius;
        this.bulletGravity=bulletGravity;
        this.bullets=bullets;
    }
    /**
     * This sets tho parameters for shooting, keeping the same list of bullets
     * There was just too much stuff in the constructor
     */
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
    public void draw(Canvas canvas, float timeScale) {
        Paint paint = new Paint();

        /*paint.setColor(Color.rgb(0,0,255));
        canvas.drawCircle(x,y,radius,paint);
        paint.setColor(Color.rgb(0,255,255));
        canvas.drawArc(new RectF(x-radius,y-radius,x+radius,y+radius),-90,(float)(360*(1.0-1.0*cooldown/cooldownTime)),true, paint); //could be funny with false //yeah, kinda funny
        paint.setColor(Color.rgb(255,0,127));
        canvas.drawCircle(x,y,radius/2,paint);*/

        float ang;
        if(vx==0 && vy==0){
            ang=0;
        }
        else{
            ang = (float) (Math.atan2(vy, vx) / Math.PI / 2 * 360) + 90;
        }

        if(flyingAngle - ang > 180){
            ang+=360;
        }
        if(flyingAngle - ang < -180){
            ang-=360;
        }

        flyingAngle = ((flyingAngle + ang*turnSpeed) / (1 + turnSpeed))%360;

        canvas.translate(x,y);
        canvas.rotate(flyingAngle);
        float tradius=2*radius;
        if(cooldown <=0 ){
            canvas.drawBitmap(tempShip0,null,new Rect((int) ( - tradius), (int) ( - tradius), (int) (tradius), (int) (tradius)),new Paint());
        }
        else if(1.0f*cooldown/cooldownTime <= 1.0f/3.0){
            canvas.drawBitmap(tempShip1,null,new Rect((int) ( - tradius), (int) ( - tradius), (int) (tradius), (int) (tradius)),new Paint());
        }
        else if(1.0f*cooldown/cooldownTime <= 2.0f/3.0){
            canvas.drawBitmap(tempShip2,null,new Rect((int) ( - tradius), (int) ( - tradius), (int) (tradius), (int) (tradius)),new Paint());
        }
        else{
            canvas.drawBitmap(tempShip3,null,new Rect((int) ( - tradius), (int) ( - tradius), (int) (tradius), (int) (tradius)),new Paint());
        }
        canvas.rotate(-flyingAngle);
        canvas.translate(-x,-y);

        paint.setColor(Color.argb(127,0,255,63));
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        //paint.set);
        canvas.drawCircle(x,y,radius,paint);

    }

    /**
     * Applies all kinds of effects, such as gravity or bullet collisions
     * Also processes how the player affects the world
     * Use before <code>update()</code>
     */
    public void affect(float timeScale) {
        vy += gravityY;

        if (bulletsKill) {
            for (Bullet bullet : bullets) {
                float col=GMBouncer.collide(this,bullet);
                //System.out.print(col+" ");
                if (col < 1 && col>=0) {
                    if (!bullet.getInPlayer()) {
                        alive = false;
                    }
                } else {
                    bullet.setInPlayer(false);
                }
            }
        }
        //System.out.println();
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
    public void update(float timeScale) {
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
            float scal=1;
            //System.out.println("Scaling: "+recoilScaling);
            if(recoilScaling != 0){
                scal=(float)(1.0*Math.sqrt((x-mx)*(x-mx) + (y-my)*(y-my))/recoilScaling);
                //System.out.println("Scaled!");
            }
            bullets.add(new Bullet(dx, dy, dvx*scal, dvy*scal, bulletRadius, bulletGravity));
            vx -= (recoilSpeed * Math.cos(ang))*scal;
            vy -= (recoilSpeed * Math.sin(ang))*scal;
        }
    }

    public boolean getAlive() {
        return alive;
    }

    /**
     * Gets the number of shots fired and resets the counter
     */
    public int retrieveShotCounter(){
        int r=shotCounter;
        shotCounter=0;
        return r;
    }


    /**
     * Puts the player in the middle, resets speed etc. Basically call after death
     */
    public void reset(int dx, int dy) {
        x=dx;
        y=dy;
        vx=0;
        vy=0;
        alive=true;
        cooldown=0;
        flyingAngle=0;
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

    public void setRecoilScaling(float recoilScaling) {
        this.recoilScaling = recoilScaling;
    }
}
