package com.adrian.tan_ty_ng_androidchal2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

public class SelfReleaseReceiver extends BroadcastReceiver {

    public static String SELFRELEASE_ID = "selfrelease-id";
    public static String SELFRELEASE = "selfrelease";
    private SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Self Release Receiver", Toast.LENGTH_SHORT).show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        //TODO Play Sound
        Uri notificationsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notificationsound);
        r.play();

        //TODO Create Notification
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(SELFRELEASE);
        int id = intent.getIntExtra(SELFRELEASE_ID, 0);
        notificationManager.notify(id, notification);

        sharedPreferences = context.getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("has_pet", "none");
        editor.putString("new_pet", "yes");
        editor.putString("full", "no");
        editor.apply();
        // This broadcast shall tell the pet acitivity to start another broadcast
        context.sendBroadcast(new Intent("RELEASE_THE_KRAKEN"));
    }
}
