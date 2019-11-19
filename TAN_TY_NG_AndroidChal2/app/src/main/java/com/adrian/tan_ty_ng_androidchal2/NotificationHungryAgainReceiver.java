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
        Toast.makeText(context, "Hungry Notif", Toast.LENGTH_SHORT).show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        //TODO Play Sound
        Uri notificationsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notificationsound);
        r.play();

        //TODO Create Notification
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);



        // TODO Set to Shared preferences full to "NO"
        sharedPreferences = context.getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("full", "no");
        editor.apply();

        // TODO Set timer or whatever for 2 minutes, if not, release pet
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Bye Bye Pet");
        builder.setContentText("Wandered to Feed Myself");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        Notification newNotification = builder.build();
        Intent notificationIntent = new Intent(context, SelfReleaseReceiver.class);
        notificationIntent.putExtra(SelfReleaseReceiver.SELFRELEASE_ID, 2);
        notificationIntent.putExtra(SelfReleaseReceiver.SELFRELEASE, newNotification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PetActivity.REQ_CODE_SELF_RELEASE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        Toast.makeText(context, "Ight Imma Head Out in 5", Toast.LENGTH_SHORT).show();


        // This broadcast shall tell the pet acitivity to start another broadcast
        context.sendBroadcast(new Intent("HUNGRY_BALLISTIC"));
    }
}
