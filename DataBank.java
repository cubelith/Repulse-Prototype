package com.example.kuba.repulsev001;

import android.graphics.Rect;

/**
 * Created by Kuba on 2017-10-16.
 */

public abstract class DataBank {


    /**
     * Coordinates of the part of the screen for playing the match
     */
    private static Rect gameField;
    /**
     * An unit of measure, useful for adjusting the size of elements for a particular device
     */
    private static float unit;



    public static float getUnit(){
        return unit;
    }

    public static void setUnit(float u){
        unit=u;
    }

    public static Rect getGameField(){
        return new Rect(gameField);
    }

    public static void setGameField(Rect r) {
        gameField=r;
    }

    /**
     * Checks when will obj1 collide with obj2 (time given in ticks)
     * Not finished (doesn't consider bouncing off walls)  (todo)
     * @param
     * @return
     */
    public static float collide(GameObjectCircle obj1,GameObjectCircle obj2){

            float dr=obj1.getRadius()+obj2.getRadius();
            float dx=obj1.getX()-obj2.getX();
            float dy=obj1.getY()-obj2.getY();
            float dvx=obj1.getVx()-obj2.getVx();
            float dvy=obj1.getVy()-obj2.getVy();
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
    }
}
