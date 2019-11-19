package com.adrian.tan_ty_ng_androidchal2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer alarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm is alarming", Toast.LENGTH_LONG).show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50000);

        //TODO Play Sound
        alarm = MediaPlayer.create(context, R.raw.alarm);
        alarm.start();
        alarm.setLooping(true);

        //TODO Create Notification
        String message = "Alarm is alarming";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Tamagochi")
                .setContentText(message)
                .setAutoCancel(true);

        Intent intent1 = new Intent(context, NotificationActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }
}
