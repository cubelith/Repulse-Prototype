package com.example.kuba.repulsev001;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;


/**
 * This probably launches the whole game, do not remove or change anything.
 * We will probably have to add more constants to be generated here, but for now do not touch.
 */
public class MainActivity extends Activity {

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity started");

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        System.out.println("MainActivity 1");
        //Constants generation/getting
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.screenWidth=dm.widthPixels;
        Constants.screenHeight=dm.heightPixels;

        System.out.println("MainActivity 2");
        setContentView(new GamePanel(this));
        System.out.println("MainActivity ended");
    }
}
