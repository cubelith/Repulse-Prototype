package com.example.kuba.repulsev001;

import android.graphics.Canvas;
import android.os.Looper;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * This probably launches the whole game, do not remove or change anything.
 * We will probably have to add more constants to be generated here, but for now do not touch.
 * Seriously, this works, so stay away.
 */

public class MainThread extends Thread {
    public static int frames;
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean r){
        running=r;
    }

    public MainThread(SurfaceHolder sH, GamePanel gP){
        super();
        surfaceHolder=sH;
        gamePanel=gP;

    }

    @Override
    public void run(){
        long startTime;
        long timeMillis=1000/MAX_FPS;
        long waitTime;
        int frameCount=0;
        long totalTime=0;
        long targetTime=1000/MAX_FPS;

        while (running){
            startTime= System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();

                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                   // System.out.println("working");
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime()-startTime)/1000000;
            waitTime = targetTime-timeMillis;
            try{
                if(waitTime > 0){
                    this.sleep(waitTime);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("fps: "+averageFPS);
            }
            frames=frameCount;
        }
    }
}
