package com.adrian.tan_ty_ng_androidchal2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PetActivity extends AppCompatActivity {

    public static final int REQ_CODE_EATEN = 0;
    public static final int REQ_CODE_SELF_RELEASE = 1;

    TextView statusTextView, messageTextView;
    Button snackButton, mealButton, kingButton, releaseButton;

    SharedPreferences sharedPreferences;

    //https://stackoverflow.com/questions/22241705/calling-a-activity-method-from-broadcastreceiver-in-android
    // Has its own Broadcast Receiver To DO When the Seperate Broadcast is Done
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("HUNGRY_BALLISTIC")) {
                statusTextView.setText("Hungry");
            } else if (intent.getAction().equals("RELEASE_THE_KRAKEN")){
                Intent resetintent = new Intent( PetActivity.this, MainActivity.class);
                startActivity(resetintent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        //Register the broadcast receiver
        registerReceiver(broadcastReceiver, new IntentFilter("HUNGRY_BALLISTIC"));
        registerReceiver(broadcastReceiver, new IntentFilter("RELEASE_THE_KRAKEN"));

        statusTextView = findViewById(R.id.statusTextView);
        messageTextView = findViewById(R.id.messageTextView);
        snackButton = findViewById(R.id.snackButton);
        mealButton = findViewById(R.id.mealButton);
        kingButton = findViewById(R.id.kingButton);
        releaseButton = findViewById(R.id.releaseButton);

        // Set in the beginning if hungry or not
        if (sharedPreferences.getString("full", "no").equals("yes")){
            statusTextView.setText("Full");
        } else {
            statusTextView.setText("Hungry");
        }

        releaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("has_pet", "none");
                editor.putString("new_pet", "yes");
                editor.putString("full", "no");
                editor.apply();

                Intent intent = new Intent( PetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        snackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("full", "no").equals("yes")){
                    Toast.makeText(getApplicationContext(), "Full", Toast.LENGTH_LONG).show();
                } else {
                    statusTextView.setText("Full");
                    Toast.makeText(getApplicationContext(), "Fed Snack, wait 30 Secs", Toast.LENGTH_SHORT).show();
                    scheduleFullnessNotification(5000);
                }
            }
        });

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("full", "no").equals("yes")){
                    Toast.makeText(getApplicationContext(), "Full", Toast.LENGTH_LONG).show();
                } else {
                    statusTextView.setText("Very Full");
                    Toast.makeText(getApplicationContext(), "Fed Meal, wait 60 Secs", Toast.LENGTH_SHORT).show();
                    scheduleFullnessNotification(10000);
                }
            }
        });

        kingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("full", "no").equals("yes")){
                    Toast.makeText(getApplicationContext(), "Full", Toast.LENGTH_LONG).show();
                } else {
                    statusTextView.setText("Bloated");
                    Toast.makeText(getApplicationContext(), "Fed King, wait 2 Minutes", Toast.LENGTH_SHORT).show();
                    scheduleFullnessNotification(15000);
                }
            }
        });

        // Check if new pet
        if (sharedPreferences.getString("new_pet", "no").equals("yes")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("new_pet", "no");
            editor.apply();
            snackButton.performClick();
        }
    }


    // https://gist.github.com/BrandonSmith/6679223
    // Schedule the notif
    private void scheduleFullnessNotification(int delay) {
        sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("full", "yes");
        editor.apply();

        Intent notificationIntent = new Intent(this, NotificationHungryAgainReceiver.class);
        notificationIntent.putExtra(NotificationHungryAgainReceiver.NOTIFICATION_ID, 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQ_CODE_EATEN, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        // Cancel releasing the kraken
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Bye Bye Pet");
        builder.setContentText("Wandered to Feed Myself");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        Notification newNotification = builder.build();
        Intent notifIntent = new Intent(this, SelfReleaseReceiver.class);
        notificationIntent.putExtra(SelfReleaseReceiver.SELFRELEASE_ID, 2);
        notificationIntent.putExtra(SelfReleaseReceiver.SELFRELEASE, newNotification);
        PendingIntent.getBroadcast(this, PetActivity.REQ_CODE_SELF_RELEASE, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
    }



}
