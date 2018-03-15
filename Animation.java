package com.example.kuba.repulsev001;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.Settings;

/**
 * Created by Kuba on 2018-02-19.
 * Basically allows animated objects. Kinda copied from a tutorial, but works fine for now
 */

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;

    private boolean isPlaying = false;

    public boolean isPlaying(){
        return isPlaying;
    }

    public void play(){
        isPlaying=true;
        frameIndex=0;
        lastFrame = System.currentTimeMillis();
    }
    public void stop(){
        isPlaying=false;
        frameIndex=0;
    }

    private float frameTime;

    private long lastFrame;

    public Animation(Bitmap[] frames, float animationTime){
        this.frames=frames;
        frameIndex=0;

        frameTime=animationTime/frames.length;
        lastFrame = System.currentTimeMillis();
    }

    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying){
            return;
        }

        canvas.drawBitmap(frames[frameIndex],null,destination,new Paint());
    }

    public void update(){
        if(!isPlaying){
            return;
        }

        if(System.currentTimeMillis() - lastFrame > frameTime*1000){
            frameIndex++;
            if(frameIndex >= frames.length){
                frameIndex=0;
            }
            lastFrame=System.currentTimeMillis();
        }
    }

}
