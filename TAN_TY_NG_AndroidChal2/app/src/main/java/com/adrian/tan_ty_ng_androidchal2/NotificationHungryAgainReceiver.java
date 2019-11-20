package com.adrian.tan_ty_ng_androidchal2;

import android.app.AlarmManager;
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
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.Toast;

public class NotificationHungryAgainReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {

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
        PendingIntent contentIntent = PendingIntent.getActivity(context, notifIntent.getIntExtra(NotificationHungryAgainReceiver.NOTIFICATION_ID, 1), notifIntent, 0);
        Notification.Builder bbuilder = new Notification.Builder(context);
        bbuilder.setContentTitle("Mind Your Pet");
        bbuilder.setContentText("Feed Me in 2 Minutes");
        bbuilder.setSmallIcon(R.drawable.ic_launcher_background);
        bbuilder.setContentIntent(contentIntent);
        Notification notification = bbuilder.build();
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);



        // TODO Set to Shared preferences full to "NO"
        sharedPreferences = context.getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("full", "no");
        editor.apply();

        // TODO Set timer or whatever for 2 minutes, if not, release pet
        Intent notificationIntent = new Intent(context, SelfReleaseReceiver.class);
        notificationIntent.putExtra(SelfReleaseReceiver.SELFRELEASE_ID, 2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PetActivity.REQ_CODE_SELF_RELEASE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + 120000;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);


        // This broadcast shall tell the pet acitivity to start another broadcast
        context.sendBroadcast(new Intent("HUNGRY_BALLISTIC"));
    }
}
