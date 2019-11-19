package com.adrian.tan_ty_ng_androidchal2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationHandler extends Service {
    private static final int NOTIFY_ID = 2;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    public static void Notify(){
        String message = "Alarm is alarming";

        Intent intent = new Intent(this, PetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Tamagochi")
                .setContentText(message)
                .setAutoCancel(true);

        Notification notification = builder.build();
        startForeground(NOTIFY_ID, notification);
    }
}
