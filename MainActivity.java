package com.example.kuba.repulsev001;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;


/**
 * This probably launches the whole game, do not remove or change anything.
 * We will probably have to add more constants to be generated here, but for now do not touch.
 */
public class MainActivity extends Activity {


    View decorView;
    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("MainActivity started");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();

        onWindowFocusChanged(true);

            generateScreenSize();



        //System.out.println("MainActivity 2");
        setContentView(new GamePanel(this));
        //System.out.println("MainActivity ended");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

    }

    public void generateScreenSize(){
        Display display = this.getWindowManager().getDefaultDisplay();
        int realWidth;
        int realHeight;

        if (Build.VERSION.SDK_INT >= 17){
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;
            realHeight = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realWidth = display.getWidth();
                realHeight = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realWidth = display.getWidth();
            realHeight = display.getHeight();
        }
        Constants.screenWidth=realWidth;
        Constants.screenHeight=realHeight;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
