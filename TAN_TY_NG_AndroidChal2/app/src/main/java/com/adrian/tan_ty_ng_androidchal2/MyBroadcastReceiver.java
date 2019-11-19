package com.adrian.tan_ty_ng_androidchal2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm is alarming", Toast.LENGTH_LONG).show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50000);

        //TODO Play Sound

        //TODO Create Notification


        // TODO Set to Shared preferences full to "NO"

        // TODO Set timer or whatever for 2 minutes, if not, release pet
    }
}
