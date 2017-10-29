package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;



/**
 *      heuraMenu (temp)
 */

public class HeuraSlider {
    private byte val;
    private byte bot,top;
    private float r,g,b;
    String name;
    float y;
    float dy;
    //Rect gameField;

    private float w;

    public HeuraSlider(String n,byte v,byte bo,byte to,float yy,float dyy,float rr,float gg,float bb){
        name=n;
        val=v;
        bot=bo;
        top=to;
        y=yy;
        dy=dyy;
        r=rr;
        g=gg;
        b=bb;
        //gameField=DataBank.getGameField();
        w=Constants.screenWidth;
    }

    void draw(Canvas canvas, boolean bool){
        Paint paint = new Paint();

        if(bool)
            paint.setColor(Color.rgb((int)(r/3),(int)(g/3),(int)(b/3)));
        else
            paint.setColor(Color.rgb((int)(r/5),(int)(g/5),(int)(b/5)));
        canvas.drawRect(0,y-dy,w,y+dy,paint);
        paint.setColor(Color.rgb((int)(r/2),(int)(g/2),(int)(b/2)));
        canvas.drawRect(0+50,y-5,w-50,y+5,paint);
        paint.setColor(Color.rgb((int)(r),(int)(g),(int)(b)));
        canvas.drawRect(0+50,y-5,(float)(0+50 + (w-100.0)/(top-bot)*(val-bot)),y+5,paint);
        canvas.drawCircle((float)(50+(w-100.0)/(top-bot)*(val-bot)),y,15,paint);
        //textAlign(CENTER,TOP);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.rgb((int)(r),(int)(g),(int)(b)));
        paint.setTextSize((float)(dy*3.0/7));
        canvas.drawText(name+": "+val,w/2,(float)(y+dy*3.0/5),paint);
    }
    boolean in(float mx,float my){
        return my>=y-dy && my<=y+dy;
    }

    void act(float mx,float my){

        if(mx<0+50){
            val=bot;
        }
        else if(mx>w-50){
            val=top;
        }
        else{
            val=(byte) (bot+(top-bot)*(mx-50+(w-100)/(top-bot)/2)/(w-100));
        }
        if(val<bot){
            val=bot;
        }
        if(val>top){
            val=top;
        }
    }

    byte getVal(){
        return val;
    }
    void setVal(float v){
        val=(byte)v;
    }
}


/*class Slider{
  int val;
  int bot,top;
  float r,g,b;
  String name;
  float y;
  float dy;

  public Slider(String n,float v,int bo,int t,float yy,float dyy,float rr,float gg,float bb){
    val=(int)v;
    bot=bo;
    top=t;
    r=rr;
    g=gg;
    b=bb;
    name=n;
    y=yy;
    dy=dyy;
  }
  void show(boolean bool){
    noStroke();
    if(bool){
      stroke(r,g,b);
      strokeWeight(6);
    }
    fill(r/4,g/4,b/4);
    rect(3,y-dy,width-6,dy*2);
    noStroke();
    fill(r/2,g/2,b/2);
    rect(50,y-5,width-100,10);
    fill(r,g,b);
    rect(50,y-5,(width-100.0)/(top-bot)*(val-bot),10);
    ellipse(50+(width-100.0)/(top-bot)*(val-bot),y,30,30);
    textAlign(CENTER,TOP);
    fill(r,g,b);
    textSize(dy*3.0/7);
    text(name+": "+val,width/2,y+dy*3.0/14);
  }
  boolean in(){
    return mouseY>=y-dy && mouseY<=y+dy;
  }
  void act(){
      if(mouseX<50){
        val=bot;
      }
      else if(mouseX>width-50){
        val=top;
      }
      else{
        val=bot+(top-bot)*(mouseX-50+(width-100)/(top-bot)/2)/(width-100);
      }
      if(val<bot)
        val=bot;
      if(val>top)
        val=top;
    }

  int getVal(){
    return val;
  }
  void setVal(float v){
    val=(int)v;
  }
}*/