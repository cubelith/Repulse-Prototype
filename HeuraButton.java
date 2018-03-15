package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * A temporary button
 */

public class HeuraButton {
    private boolean val;
    private float r,g,b;
    String name;
    float y;
    float dy;
    private float w;

    public HeuraButton(String n,boolean v,float yy,float dyy,float rr,float gg,float bb){
        name=n;
        val=v;

        y=yy;
        dy=dyy;
        r=rr;
        g=gg;
        b=bb;
        //gameField=DataBank.getGameField();
        w=Constants.screenWidth;
    }

    void draw(Canvas canvas){
        Paint paint = new Paint();

        if(val)
            paint.setColor(Color.rgb((int)(r/3),(int)(g/3),(int)(b/3)));
        else
            paint.setColor(Color.rgb((int)(r/5),(int)(g/5),(int)(b/5)));
        canvas.drawRect(0,y-dy,w,y+dy,paint);
        /*paint.setColor(Color.rgb((int)(r/2),(int)(g/2),(int)(b/2)));
        canvas.drawRect(0+50,y-5,w-50,y+5,paint);
        paint.setColor(Color.rgb((int)(r),(int)(g),(int)(b)));
        canvas.drawRect(0+50,y-5,(float)(0+50 + (w-100.0)/(top-bot)*(val-bot)),y+5,paint);
        canvas.drawCircle((float)(50+(w-100.0)/(top-bot)*(val-bot)),y,15,paint);*/
        //textAlign(CENTER,TOP);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.rgb((int)(r),(int)(g),(int)(b)));
        paint.setTextSize((float)(dy*4.0/7));
        canvas.drawText(name+": "+val,w/2,(float)(y+dy*3.0/5),paint);
    }
    boolean in(float mx,float my){
        return my>=y-dy && my<=y+dy;
    }

    void act(float mx,float my){

        if(in(mx,my)){
            val=!val;
        }
    }

    boolean getVal(){
        return val;
    }
    void setVal(boolean v){
        val=v;
    }

}
