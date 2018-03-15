package com.example.kuba.repulsev001;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /**
     * the pregame pause - true means paused
     */
    private boolean preGamePause;

    private Player player;
    private ArrayList<Bullet> bullets;
    /**
     * Counts the bullets shot
     */
    private int bulletsShot;
    /**
     * States approximately how long a bullet stays on the screen.
     * The formula for the amount of bullets on screen is:
     * bulletsOnScreen=bulletsShot^bulletLifetime
     * So bulletLifetime may not be less than 0 or greater than 1.
     */
    private float bulletLifetime;
    /**
     * Whether walls/bullets should kill the player or not.
     * Setting bulletsKill to false makes sense only for debugging.
     */
    private boolean wallsKill, bulletsKill;

    /**
     * Basically just DataBank.unit, renamed here for easier accesss
     */
    private final float unit;

    private Rect gameField;
    /**
     * Score achieved in the last game
     */
    private int lastScore;    //temp

    Bitmap background;

    private float recoilScaling;

    /**
     * The rate at which time flows (1 - normal, 2 - faster, 0.5 - slower, 0 - stop etc.)
     * Negative values would turn back the time with some unexpected results - don't use in real game
     */
    private float timeScale;

    /**
     * For now, initialises a default game
     */
    public GMBouncer() {
        //initialisation
        unit = DataBank.getUnit();
        gameField = DataBank.getGameField();

        wallsKill = true;
        bulletsKill = true;

        bulletLifetime = (float) 0.75;      //TEMP
        player = new Player(Constants.screenWidth / 2, Constants.screenHeight / 2, 22 * unit, 25, 0 * unit, wallsKill, bulletsKill,recoilScaling);              //TEMP i to bardzo
        player.setShotParams(bullets, 5 * unit, 10 * unit, 7 * unit, 0 * unit);        //TEMP i to bardzo
        resetMatch();
    }

    @Deprecated   //honestly no idea why this is temporary, I'll try to work this out
    public GMBouncer(Player player) {   ///TEMP
        this();

        this.player = player;

        bullets = new ArrayList<Bullet>();
        //bullets.add(new Bullet());
        player.setBullets(bullets);

        bulletsShot = 0;
        //resetMatch();
    }

    public void setRecoilScaling(float s){
        recoilScaling=s;
        player.setRecoilScaling(s);
    }

    /**
     * Resets the positions etc. to starting values
     */
    public void resetMatch() {
        //System.out.println("UNIT: "+unit);
        //TEMP
        preGamePause = true;
        lastScore = getScore();


        try {  ///temp
            bullets.clear();
        } catch (NullPointerException e) {
            bullets = new ArrayList<Bullet>();
            player.setBullets(bullets);
        }

        player.reset((gameField.left + gameField.right) / 2, (gameField.top + gameField.bottom) / 2);


        //bullets.add(new Bullet());

        bulletsShot = 0;

        timeScale = 1;


        BitmapFactory bf = new BitmapFactory();
        background = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.tempbg);

       // background = BitmapFactory.decodeRso("pictures/tempbg.png", options );
    }

    /**
     * Updates the match by one frame
     */
    public void update() {
        if(!preGamePause) {
            bulletsShot += player.retrieveShotCounter();
            if (bulletsShot > 0) {


                if (bullets.size() > Math.pow(bulletsShot + 1, bulletLifetime)) {
                    bullets.get(0).setDying(true);
                }
                if (bullets.size() - 1 > Math.pow(bulletsShot, bulletLifetime)) {
                    bullets.remove(0);
                }

                if (!player.getAlive()) {
                    resetMatch();
                }
                for (Bullet bullet : bullets) {
                    bullet.affect(timeScale);
                }
                player.affect(timeScale);

                for (Bullet bullet : bullets) {
                    bullet.update(timeScale);
                }
                player.update(timeScale);

            }
        }
    }

    /**
     * Draws the match on the canvas
     */
    public void draw(Canvas canvas) {
        //canvas.drawColor(Color.rgb(0, 0, 0));
        canvas.drawBitmap(background,null,gameField,new Paint());

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //paint.setColor(Color.YELLOW);
        if (wallsKill) {
            paint.setColor(Color.RED);
            //System.out.println("RED: "+wallsKill);
        } else {
            paint.setColor(Color.CYAN);
            //System.out.println("CYAN"+wallsKill);
        }
        paint.setStrokeWidth(3);
        canvas.drawRect(DataBank.getGameField().left + 3, DataBank.getGameField().top + 3, DataBank.getGameField().right - 3, DataBank.getGameField().bottom - 3, paint);

        player.draw(canvas,timeScale);
        for (Bullet bullet : bullets) {
            bullet.draw(canvas,timeScale);
        }
        if (!player.getAlive()) {
            canvas.drawColor(Color.rgb(127, 0, 0));
        }
    }

    /**
     * Returns the score achieved in the match so far.
     */
    public int getScore() {
        return bulletsShot;
    }

    /**
     * Called on a click (just at the moment of touching the screen)
     */
    public void mouseDown(MotionEvent event) {
        preGamePause=false;
        player.mouseDown(event);
    }

    /**
     * Called after releasing the finger
     */
    public void mouseUp(MotionEvent event) {

    }

    @Override
    public void setBulletLifetime(float v) {
        bulletLifetime = v;
    }

    @Override
    public int getLastScore() {
        return lastScore;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public static float adamcollide(GameEntityCircle obj1,GameEntityCircle obj2){
        //according to the wikipedia page for quartic equation
        float dsx = obj1.getX()-obj2.getX();
        float dsy = obj1.getY()-obj2.getY();
        float dvx = obj1.getVx()-obj2.getVx();
        float dvy = obj1.getVy()-obj2.getVy();
        float dgx = obj1.getGravityX()-obj2.getGravityX();
        float dgy = obj1.getGravityY()-obj2.getGravityY();
        float dist = obj1.getRadius() + obj2.getRadius();

        float a = (dgx*dgx + dgy*dgy)/4;
        float b = dgx*dvx + dgy*dvy;
        float c = dvx*dvx + dgx*dsx + dvy*dvy + dgy*dsy;
        float d = 2*dvx*dsx + 2*dvy*dsy;
        float e = dsx*dsx + dsy*dsy - dist;

       Polynomial poly = new Polynomial(e,d,c,b,a);

        float ret = poly.firstRootFrom(0,5);
        return ret;

        //return -999;  ///does not collide
    }

    public static float newCollide(GameEntityCircle obj1, GameEntityCircle obj2) {
        //float o1=Math.min(obj1.getX())
        //float mt=Math.min()


        float dr = obj1.getRadius() + obj2.getRadius();
        float dx = obj1.getX() - obj2.getX();
        float dy = obj1.getY() - obj2.getY();
        float dvx = obj1.getVx() - obj2.getVx();
        float dvy = obj1.getVy() - obj2.getVy();
        float delta = (float) Math.pow(2 * (dx * dvx + dy * dvy), 2) - 4 * (dvx * dvx + dvy * dvy) * (dx * dx + dy * dy - dr * dr);

        Polynomial poly = new Polynomial((dx * dx + dy * dy - dr * dr),2 * (dx * dvx + dy * dvy),(dvx * dvx + dvy* dvy));

        return poly.firstRootFrom(0,5);

        //this should be unreachable anyway
        //return -1;

    }

    //this one at least works
    public static float collide(GameEntityCircle obj1, GameEntityCircle obj2) {
        //float o1=Math.min(obj1.getX())
        //float mt=Math.min()



        float dr = obj1.getRadius() + obj2.getRadius();
        float dx = obj1.getX() - obj2.getX();
        float dy = obj1.getY() - obj2.getY();
        float dvx = obj1.getVx() - obj2.getVx();
        float dvy = obj1.getVy() - obj2.getVy();
        float delta = (float) Math.pow(2 * (dx * dvx + dy * dvy), 2) - 4 * (dvx * dvx + dvy * dvy) * (dx * dx + dy * dy - dr * dr);
        if (delta < 0) {
            return -1;
        } else {
            float t1 = (float) (-2 * (dx * dvx + dy * dvy) - Math.sqrt(delta)) / (2 * (dvx * dvx + dvy * dvy));
            if (t1 < 0) {
                float t2 = (float) (-2 * (dx * dvx + dy * dvy) + Math.sqrt(delta)) / (2 * (dvx * dvx + dvy * dvy));
                if (t2 < 0) {
                    return -1;
                } else {
                    return 0;
                }
            } else if (t1 == 0) {
                return 0;
            } else if (t1 > 0) {
                return t1;
            }
        }
        //this should be unreachable anyway
        return -1;

    }


    public static float newcollide(GameEntityCircle obj1,GameEntityCircle obj2){
        //according to the wikipedia page for quartic equation
        float dsx = obj1.getX()-obj2.getX();
        float dsy = obj1.getY()-obj2.getY();
        float dvx = obj1.getVx()-obj2.getVx();
        float dvy = obj1.getVy()-obj2.getVy();
        float dgx = obj1.getGravityX()-obj2.getGravityX();
        float dgy = obj1.getGravityY()-obj2.getGravityY();
        float dist = obj1.getRadius() + obj2.getRadius();

        float a = (dgx*dgx + dgy*dgy)/4;
        float b = dgx*dvx + dgy*dvy;
        float c = dvx*dvx + dgx*dsx + dvy*dvy + dgy*dsy;
        float d = 2*dvx*dsx + 2*dvy*dsy;
        float e = dsx*dsx + dsy*dsy - dist;

        float delta = (float)(256.0*a*a*a*e*e*e - 192.0*a*a*b*d*e*e - 128.0*a*a*c*c*e*e + 144.0*a*a*c*d*d*e - 27.0*a*a*d*d*d*d + 144.0*a*b*b*c*e*e - 6.0*a*b*b*d*d*e - 80.0*a*b*c*c*d*e + 18.0*a*b*c*d*d*d + 16.0*a*c*c*c*c*e - 4.0*a*c*c*c*d*d - 27.0*b*b*b*b*e*e + 18.0*b*b*b*c*d*e - 4.0*b*b*b*d*d*d - 4.0*b*b*c*c*c*e + 1.0*b*b*c*c*d*d);
        float P = 8*a*c - 3*b*b;
        float R = b*b*b + 8*d*a*a - 4*a*b*c;
        float d0 = c*c - 3*b*d + 12*a*e;
        float D = 64*a*a*a*e - 16*a*a*c*c + 16*a*b*b*c - 3*b*b*b*b;

        float p = (8*a*c - 3*b*b)/(8*a*a);
        float q = (b*b*b - 4*a*b*c + 8*a*a*d)/(8*a*a*a);
        float d1 = 2*c*c*c - 9*b*c*d + 27*b*b*e + 27*a*d*d - 72*a*c*e;
        float Q = (float)(  Math.cbrt(d1 + Math.sqrt( d1*d1 - 4*d0*d0*d0 )/2)  );
        float S = (float)(  Math.sqrt(-p*2/3 + (Q+d0/Q)/(3-a))/2  );

        float ret;
        float[] t = new float[4];
        int event=0;
        float sthpos = (float)(  Math.sqrt(-4*S*S - 2*p + q/S)/2  );
        float sthneg=0;
        try {
            sthneg = (float) (Math.sqrt(-4 * S * S - 2 * p - q / S) / 2);
        }catch(Exception dzsfjhbdas){}

        if(delta<0){   //two real roots
            q=q*Math.signum(q);
            event=1;
            t[0]=-b/(4*a) - S - sthpos;
            t[1]=-b/(4*a) - S + sthpos;
        }
        else if(delta > 0){  //4 or none
            if(P<0 && D<0){   //4
                event=2;
                t[0]=-b/(4*a) - S - sthpos;
                t[1]=-b/(4*a) - S + sthpos;
                t[2]=-b/(4*a) + S - sthneg;
                t[3]=-b/(4*a) + S + sthneg;
            }
            else{    //0
                return -1;
            }
        }
        else if(delta == 0){   ///multiple root
            if(P<0 && D<0 && d0!=0){   // one double, two normal
                event=3;
                t[0]=-b/(4*a) - S - sthpos;
                t[1]=-b/(4*a) - S + sthpos;
                t[2]=-b/(4*a) + S - sthneg;
                t[3]=-b/(4*a) + S + sthneg;
            }
            else if(D>0 || (P>0 && (D!=0 || R!=0))){  //one double
                return -2;
            }
            else if(d0==0 && D!=0){  //one triple, one normal
                event=4;
                t[0]=-b/(4*a) - S - sthpos;
                t[1]=-b/(4*a) - S + sthpos;
                t[2]=-b/(4*a) + S - sthneg;
                t[3]=-b/(4*a) + S + sthneg;
            }
            else if(D==0){
                if(P<0){  //two double
                    return -3;
                }
                else if(P>0 && R!=0){   ///none
                    return -4;
                }
                else if(d0==0){   //all are -b/(4a)
                    return -5;
                }
                else{
                    return -23;  ///shouldn't be here
                }
            }
            else {
                return -22; ///shouldn't be here
            }
        }
        else{
            return -24;  ///shouldn't be here
        }

        if(event==1){
            oneUseCollideBubbleSort(t,2);
            if(t[0]>0) {  //2
                return t[0];
            }
            else if(t[1]>0){
                return 0;
            }
            return -11;
        }
        else if(event==2){  //4
            oneUseCollideBubbleSort(t,4);
            if(t[0]>0){
                return t[0];
            }
            else if(t[1]>0){
                return 0;
            }
            else if(t[2]>0){
                return t[2];
            }
            else if(t[3]>0){
                return 0;
            }
            return -12;
        }
        else if(event==3){  //1 double, 2 normal
            oneUseCollideBubbleSort(t,4);
            if(t[0]==t[1]){
                t[0]=t[2];
                t[1]=t[3];
            }
            if(t[1]==t[2]){
                t[1]=t[3];
            }

            if(t[0]>0) {  //2
                return t[0];
            }
            else if(t[1]>0){
                return 0;
            }
            return -13;
        }
        else if(event==4){  // 1 triple, 1 normal
            oneUseCollideBubbleSort(t,4);
            oneUseCollideFlattener(t,4);
            if(t[0]>0) {  //2
                return t[0];
            }
            else if(t[1]>0){
                return 0;
            }
            return -14;
        }

       return -999;  ///does not collide
    }

    @Deprecated  // this is kinda temporary, only for the collide functions
    private static void oneUseCollideBubbleSort(float[] tab,int n){
        //int n = tab.length;
        float temp = 0;

        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){

                if(tab[j-1] > tab[j]){
                    //swap the elements!
                    temp = tab[j-1];
                    tab[j-1] = tab[j];
                    tab[j] = temp;
                }

            }
        }
    }

    @Deprecated  // this is kinda temporary, only for the collide functions
    private static void oneUseCollideFlattener(float[] tab,int n){
        for(int i=0;i<n-1;i++){
            if(tab[i]==tab[i+1]){
                for(int j=i+1;j<n-1;j++){
                    tab[j]=tab[j+1];
                }
            }
        }
    }




}