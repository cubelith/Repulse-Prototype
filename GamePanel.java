package com.example.kuba.repulsev001;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.JsonReader;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The main class that manages the whole game (sets up, updates and draws everything).
 * Basically the main menu, running everything else inside
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Context context;

    /**
     * Says where in the app the player is (menu, game, etc.).
     * Leave 0 free as the "test state".
     */
    private int state;
    private static final int STATE_PLAYING=1;
    private static final int STATE_HEURA_MENU=12345;     //heuraMenu (temp)
    private static final int STATE_HEURA_SETTINGS=12346;     //heuraMenu (temp)
    private GameMode match;

    private int highscore;    //temp

    private HeuraSlider[] sliders;     //heuraMenu (temp)
    private HeuraButton[] buttons;
    private int sliderHold;     //heuraMenu (temp)
    private int t,t2,w;     //heuraMenu (temp)

    private byte[] heuraLoader;

    public void setRunning(boolean b){
        thread.setRunning(b);
    }

    public GamePanel(Context context){
        super(context);
        this.context=context;
        getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT = context;
        thread=new MainThread(getHolder(), this);
        setRunning(true);
        setFocusable(true);

        sliderHold = -1;     //heuraMenu (temp)
        sliders=new HeuraSlider[11];     //heuraMenu (temp)
        buttons=new HeuraButton[1];
        heuraLoader=new byte[sliders.length];
        t=(int) (Constants.screenHeight/(sliders.length*2.0+1.0));     //heuraMenu (temp)
        w=Constants.screenWidth;     //heuraMenu (temp)
        //here be setup
        DataBank.setUnit((float)(Math.sqrt((Constants.screenWidth*Constants.screenHeight)/(720.0*1280))));                    //TEMP
        DataBank.setGameField(new Rect(0,t,Constants.screenWidth,Constants.screenHeight)); //TEMP
        state=STATE_PLAYING;                                            //TEMP


        //Looper.prepare(); //no idea why I need this, but it makes toasts work



        //float unit=DataBank.getUnit();




       resetHeuraSliders();     //heuraMenu (temp)
        resetHeuraButtons();



        match=new GMBouncer();                                          //TEMP
    }

    //heuraMenu (temp)
    void resetHeuraSliders(){
        sliders[ 0]=new HeuraSlider("Killing walls (1)"      ,(byte)1 ,(byte)0,(byte)1  ,2*t ,t,0,255,255);
        sliders[ 1]=new HeuraSlider("Player size (22)"       ,(byte)22,(byte)1,(byte)60 ,4*t ,t,255,0,127);
        sliders[ 2]=new HeuraSlider("Recoil speed (5)"       ,(byte)5 ,(byte)1,(byte)30 ,6*t ,t,255,0,127);
        sliders[ 3]=new HeuraSlider("Bullet speed (10)"      ,(byte)10,(byte)1,(byte)60 ,8*t ,t,255,191,0);
        sliders[ 4]=new HeuraSlider("Bullet size (7)"        ,(byte)7 ,(byte)1,(byte)40 ,10*t,t,255,191,0);
        sliders[ 5]=new HeuraSlider("Cooldown (25)"          ,(byte)25,(byte)0,(byte)100,12*t,t,127,0,255);
        sliders[ 6]=new HeuraSlider("Player gravity (0 or 5)",(byte)0 ,(byte)0,(byte)50 ,14*t,t,255,0,127);
        sliders[ 7]=new HeuraSlider("Bullet gravity (0 or 5)",(byte)0 ,(byte)0,(byte)50 ,16*t,t,255,191,0);
        sliders[ 8]=new HeuraSlider("Bullet lifetime (30)"   ,(byte)30,(byte)5,(byte)40 ,18*t,t,127,0,255);
        sliders[ 9]=new HeuraSlider("Killing bullets (1)"    ,(byte)1 ,(byte)0,(byte)1  ,20*t,t,0,255,255);
        sliders[10]=new HeuraSlider("Save/load file number"  ,(byte)1 ,(byte)1,(byte)30 ,22*t,t,255,127,0);
        heuraGetHighscore();
    }
    void resetHeuraButtons(){
        buttons[ 0]=new HeuraButton("Scaling recoil with click distance" ,false,2*t ,t,0,255,255);

        heuraGetHighscore();
    }

    void heuraGetHighscore(){
        highscore=0;
        try {
            heuraLoadFile("highscore-"+sliders[0].getVal()+"-"+sliders[1].getVal()
                    +"-"+sliders[2].getVal()+"-"+sliders[3].getVal()
                    +"-"+sliders[4].getVal()+"-"+sliders[5].getVal()
                    +"-"+sliders[6].getVal()+"-"+sliders[7].getVal()
                    +"-"+sliders[8].getVal()+"-"+sliders[9].getVal());
            highscore=heuraLoader[0]*128+heuraLoader[1];
        } catch (IOException e) {

        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread=new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        //removing this fixed the minimasation issue, and yielded no new problems so far
        /*boolean retry = true;
        while(true){
            try{
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            retry = false;
        }*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_UP: {
                mouseUp(event);
                return false;
            }
            case MotionEvent.ACTION_DOWN: {
                mouseDown(event);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                mouseMoved(event);
                return false;
            }
            default:{
                //sliderHold=-1;
            }

        }

        return true;
        //return super.onTouchEvent(event);
    }



    public void update(){

        if(state==STATE_PLAYING){
            match.update();
            if(!match.getPlayer().getAlive()) {
                if (match.getScore() > highscore) {

                    highscore=match.getScore();
                    heuraLoader[0] = (byte) (highscore / 128);
                    heuraLoader[1] = (byte) (highscore % 128);
                    heuraSaveFile("highscore-" + sliders[0].getVal() + "-" + sliders[1].getVal()
                            + "-" + sliders[2].getVal() + "-" + sliders[3].getVal()
                            + "-" + sliders[4].getVal() + "-" + sliders[5].getVal()
                            + "-" + sliders[6].getVal() + "-" + sliders[7].getVal()
                            + "-" + sliders[8].getVal() + "-" + sliders[9].getVal());


                    System.out.println("New highscore!");
                    Constants.showShortToast("New highscore!");
                }
            }
        }
        else if(state==STATE_HEURA_MENU){
            //heuraMenu (temp)
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if(state==STATE_PLAYING){
            match.draw(canvas);
            Paint paint=new Paint();
            paint.setColor(Color.rgb(0,63,63));
            canvas.drawRect(0,0,w,t-1,paint);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float)(t*5.0/7));
            paint.setColor(Color.rgb(0,255,255));
            canvas.drawText(match.getScore()+"/"+match.getLastScore()+"/"+highscore,w/2,t/4*3,paint);
        }
        else if(state==STATE_HEURA_MENU || state==STATE_HEURA_SETTINGS){      //heuraMenu (temp)

            Paint paint=new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float)(t*5.0/7));

            paint.setColor(Color.rgb(31,63,95));
            canvas.drawRect(w/5*0,0,w/5*1,t,paint);
            paint.setColor(Color.rgb(0,255,255));
            canvas.drawText("Play",w/10,t/4*3,paint);

            paint.setColor(Color.rgb(95,63,31));
            canvas.drawRect(w/5*1,0,w/5*2,t,paint);
            paint.setColor(Color.rgb(255,127,0));
            canvas.drawText("Load",w/10*3,t/4*3,paint);

            paint.setColor(Color.rgb(31,95,31));
            canvas.drawRect(w/5*2,0,w/5*3,t,paint);
            paint.setColor(Color.rgb(0,255,0));
            canvas.drawText("Save",w/10*5,t/4*3,paint);

            paint.setColor(Color.rgb(95,31,31));
            canvas.drawRect(w/5*3,0,w/5*4,t,paint);
            paint.setColor(Color.rgb(255,0,0));
            canvas.drawText("Reset",w/10*7,t/4*3,paint);

            paint.setColor(Color.rgb(63,31,95));
            canvas.drawRect(w/5*4,0,w/5*5,t,paint);
            paint.setColor(Color.rgb(191,0,255));
            canvas.drawText("Settings",w/10*9,t/4*3,paint);

            //if(sliderHold>-1)
                //System.out.println("SliderHold: "+sliderHold);

            if(state==STATE_HEURA_MENU) {
                for (int i = 0; i < sliders.length; i++) {
                    sliders[i].draw(canvas, sliderHold == i);
                }
            }
            else{
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].draw(canvas);
                }
            }
        }

        //System.out.println("GamePanel");


    }

    void heuraSaveFile(String fileName){
        try{
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(heuraLoader);
            fos.close();
        }catch(Exception e){
            //System.out.println("save failed: "+ "\n"+ e.getStackTrace());
        }
    }

    byte[] heuraLoadFile(String fileName) throws IOException {
        FileInputStream fis = context.openFileInput(fileName);
        fis.read(heuraLoader,0,sliders.length);
        fis.close();
        return heuraLoader;
    }

    void mouseDown(MotionEvent event){
        if(state==STATE_PLAYING){
            if(event.getY()>t) {
                match.mouseDown(event);
            }
            else{
                state=STATE_HEURA_MENU;     //heuraMenu (temp)
            }
        }
        else if(state==STATE_HEURA_MENU || state==STATE_HEURA_SETTINGS){     //heuraMenu (temp)
            if(event.getY()<t){
                if(event.getX()<w/5){
                    float recoilScaling = (buttons[0].getVal() ? w : 0);
                    Player p=new Player(Constants.screenWidth/2,Constants.screenHeight/2,sliders[1].getVal(),sliders[5].getVal(),(float)(sliders[6].getVal()/50.0),sliders[0].getVal()==1,sliders[9].getVal()==1,recoilScaling);
                    p.setShotParams(sliders[2].getVal(),sliders[3].getVal(),sliders[4].getVal(),(float)(sliders[7].getVal()/50.0));

                    match=new GMBouncer(p);
                    match.setBulletLifetime((float)(sliders[8].getVal()/40.0));
                    state=STATE_PLAYING;
                    heuraGetHighscore();
                }
                else if(event.getX()<2*w/5) {

                    try {
                        heuraLoadFile("heuraSettings"+sliders[sliders.length-1].getVal());
                        for(int i=0;i<sliders.length-1;i++){
                            sliders[i].setVal(heuraLoader[i]);
                        }
                    } catch (IOException e) {

                    }

                    Constants.showShortToast("Loaded settings from: " + sliders[sliders.length-1].getVal());
                    //System.out.println("LOAD");
                }
                else if(event.getX()<3*w/5){

                    //String FILENAME = "heuraSettings"+sliders[sliders.length-1].getVal();
                    for(int i=0;i<sliders.length-1;i++){
                        heuraLoader[i]=(byte)sliders[i].getVal();
                    }
                    heuraSaveFile("heuraSettings"+sliders[sliders.length-1].getVal());

                    Constants.showShortToast("Saved settings at: " + sliders[sliders.length-1].getVal());
                    //System.out.println("SAVE");
                }
                else if(event.getX()<w/5*4){
                    if(state==STATE_HEURA_MENU) {
                        resetHeuraSliders();
                    }
                    else{
                        resetHeuraButtons();
                    }
                    Constants.showShortToast("Restored default parameters");
                }
                else{
                    if(state == STATE_HEURA_SETTINGS){
                        state = STATE_HEURA_MENU;
                    }
                    else {
                        state = STATE_HEURA_SETTINGS;
                    }
                }
            }
            else {   //somewhere in the field
                if(state == STATE_HEURA_MENU) {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliderHold < 0) {
                            if (sliders[i].in(event.getX(), event.getY())) {
                                sliderHold = i;

                                break;
                            }
                        }
                    }
                }
                else{
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].act(event.getX(), event.getY());
                    }
                    if(buttons[0].getVal()){
                        match.setRecoilScaling(w);
                        System.out.println("Scaling ON: "+w);
                    }
                    else{
                        match.setRecoilScaling(0);
                        System.out.println("Scaling OFF");
                    }

                }
            }
        }
    }
//    void mouseMove(){
//
//    }
    void mouseUp(MotionEvent event){
        if(state==STATE_PLAYING){
            match.mouseUp(event);
        }
        else if(state==STATE_HEURA_MENU){     //heuraMenu (temp)
            sliderHold=-1;
            System.out.println("mouseUp!");
        }
    }
    private void mouseMoved(MotionEvent event) {
        if(state==STATE_PLAYING){

        }
        else if(state==STATE_HEURA_MENU){     //heuraMenu (temp)
            if(sliderHold>=0) {
                sliders[sliderHold].act(event.getX(), event.getY());
            }
        }
    }

    public void pause() {
        thread.setRunning(false);
    }
    public void resume() {
        thread.setRunning(true);
    }
}
