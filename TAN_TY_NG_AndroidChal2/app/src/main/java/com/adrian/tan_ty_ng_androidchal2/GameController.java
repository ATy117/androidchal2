package com.adrian.tan_ty_ng_androidchal2;

import android.content.Context;
import android.content.Intent;
import android.widget.MediaController;

public class GameController {

    Context c;
    private int seconds;

    public GameController(Context c){
        this.c = c;
    }

    public void updateTimer (int seconds) {
        this.seconds = seconds;
        // send broadcast
        Intent intent = new Intent("UPDATE");
        intent.putExtra("number", seconds);
        c.sendBroadcast(intent);

    }

    public int getSeconds() {
        return seconds;
    }

    public void initialize(){
        c.sendBroadcast(new Intent("FIRST"));
    }



}
