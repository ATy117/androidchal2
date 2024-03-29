package com.adrian.tan_ty_ng_androidchal2;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class GameService extends Service {

    //timer task
    private final IBinder gameBind = new GameBinder();
    private static final String TAG = "GameService";


    Timer t;
    int countdown = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        return gameBind;
    }

    public void onCreate(){
        super.onCreate();

        initTimer();
        System.out.println("On Create ");

        SharedPreferences sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        System.out.println(sharedPreferences.getString("new_pet", "yes"));
        if (sharedPreferences.getString("new_pet", "yes").equals("yes")){
            System.out.println("New Pet");
            startTimer(30);
            PetActivity.controller.initialize();
        }

    }

    @Override
    public boolean onUnbind(Intent intent){
        resetTimer();
        return false;
    }

    @Override
    public void onDestroy() {
        resetTimer();
        stopForeground(true);
    }


    public void initTimer(){
        t = new Timer();
    }

    public void startTimer(int delay) {

        countdown = delay;

        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                if (countdown > 0) {
                    PetActivity.controller.updateTimer(countdown);
                    countdown--;
                }
                else {

                    resetTimer();
                }

            }
        }, 1000, 1000);
    }

    public void resetTimer() {
        t.cancel();
    }


    public class GameBinder extends Binder {

        GameService getService()
        {
            return GameService.this;
        }
    }






}