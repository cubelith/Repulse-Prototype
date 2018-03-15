package com.example.kuba.repulsev001;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * The class for keeping various constants, like the screen size.
 * That does not include constants we set ourselves, only the independent ones.
 * Also keeps some general static methods
 */
public abstract class Constants {

    public static int screenWidth;
    public static int screenHeight;

    public static Context CURRENT_CONTEXT;

    public static void showShortToast(final String text){

        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            public void run() {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(CURRENT_CONTEXT, text, duration);
                toast.show();
            }
        }, 0 );

        //CharSequence text = "Hello toast!";

    }


}
