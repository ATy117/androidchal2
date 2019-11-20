package com.adrian.tan_ty_ng_androidchal2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
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

    Intent startIntent;
    GameService gameSrv;

    static GameController controller;

    private ServiceConnection gameConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GameService.GameBinder binder = (GameService.GameBinder)service;
            //get service
            Log.d("STUFF", "Null");
            gameSrv = binder.getService();
            Log.d("STUFF", "NOT NULL");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            //stop code
        }
    };

    //https://stackoverflow.com/questions/22241705/calling-a-activity-method-from-broadcastreceiver-in-android
    // Has its own Broadcast Receiver To DO When the Seperate Broadcast is Done
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("HUNGRY_BALLISTIC")) {
                statusTextView.setText("Hungry");
                gameSrv.resetTimer();
                gameSrv.initTimer();
                gameSrv.startTimer(120);
            } else if (intent.getAction().equals("RELEASE_THE_KRAKEN")){
                Intent resetintent = new Intent( PetActivity.this, MainActivity.class);
                startActivity(resetintent);
                finish();
            } else if (intent.getAction().equals("UPDATE")){
                messageTextView.setText(intent.getIntExtra("number", 0) + "");
            } else if (intent.getAction().equals("FIRST")){
                statusTextView.setText("Full");
                sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("full", "yes");
                editor.apply();

                Intent notificationIntent = new Intent(PetActivity.this, NotificationHungryAgainReceiver.class);
                notificationIntent.putExtra(NotificationHungryAgainReceiver.NOTIFICATION_ID, 1);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(PetActivity.this, REQ_CODE_EATEN, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                long futureInMillis = SystemClock.elapsedRealtime() + 30000;
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        //Register the broadcast receiver
        controller = new GameController(this);
        registerReceiver(broadcastReceiver, new IntentFilter("UPDATE"));
        registerReceiver(broadcastReceiver, new IntentFilter("HUNGRY_BALLISTIC"));
        registerReceiver(broadcastReceiver, new IntentFilter("RELEASE_THE_KRAKEN"));
        registerReceiver(broadcastReceiver, new IntentFilter("FIRST"));

        startIntent = new Intent(getApplicationContext(), GameService.class);
        bindService(startIntent, gameConnection, Context.BIND_AUTO_CREATE);
        startService(startIntent);

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

//        snackButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sharedPreferences.getString("full", "no").equals("yes")){
//                    Toast.makeText(getApplicationContext(), "Full", Toast.LENGTH_LONG).show();
//                } else {
//                    statusTextView.setText("Full");
//                    scheduleFullnessNotification(5000);
//                    gameSrv.resetTimer();
//                    gameSrv.startTimer(5);
//                }
//            }
//        });
//
//        mealButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sharedPreferences.getString("full", "no").equals("yes")){
//                    Toast.makeText(getApplicationContext(), "Full", Toast.LENGTH_LONG).show();
//                } else {
//                    statusTextView.setText("Very Full");
//                    scheduleFullnessNotification(10000);
//                }
//            }
//        });
//
//        kingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sharedPreferences.getString("full", "no").equals("yes")){
//                    Toast.makeText(getApplicationContext(), "Full", Toast.LENGTH_LONG).show();
//                } else {
//                    statusTextView.setText("Bloated");
//                    scheduleFullnessNotification(15000);
//                }
//            }
//        });

        // Check if new pet
        if (sharedPreferences.getString("new_pet", "no").equals("yes")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("new_pet", "no");
            editor.apply();
        }
    }



    @Override
    protected void onDestroy() {

        unbindService(gameConnection);
        stopService(startIntent);
        unregisterReceiver(broadcastReceiver);
        gameSrv = null;
        super.onDestroy();
    }


    // https://gist.github.com/BrandonSmith/6679223
    // Schedule the notif
    private void scheduleFullnessNotification(int delay) {
        sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("full", "yes");
        editor.apply();
        gameSrv.resetTimer();
        gameSrv.initTimer();
        gameSrv.startTimer(delay/1000);

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


    public void snackPressed(View view) {

        if (sharedPreferences.getString("full", "no").equals("yes")){
            Toast.makeText(getApplicationContext(), "Still Full", Toast.LENGTH_LONG).show();
        } else {
            statusTextView.setText("Full");
            scheduleFullnessNotification(30000);
        }
    }

    public void mealClicked(View view) {
        if (sharedPreferences.getString("full", "no").equals("yes")){
            Toast.makeText(getApplicationContext(), "Still Full", Toast.LENGTH_LONG).show();
        } else {
            statusTextView.setText("Very Full");
            scheduleFullnessNotification(60000);
        }
    }

    public void kingClicked(View view) {
        if (sharedPreferences.getString("full", "no").equals("yes")){
            Toast.makeText(getApplicationContext(), "Still Full", Toast.LENGTH_LONG).show();
        } else {
            statusTextView.setText("Bloated");
            scheduleFullnessNotification(120000);
        }
    }
}
