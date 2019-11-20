package com.adrian.tan_ty_ng_androidchal2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

        Intent notifIntent = new Intent(context, MainActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notifIntent.getIntExtra(SelfReleaseReceiver.SELFRELEASE_ID, 1), notifIntent, 0);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Bye Bye Pet");
        builder.setContentText("Wandered to Feed Myself");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        int id = intent.getIntExtra(SELFRELEASE_ID, 0);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
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
