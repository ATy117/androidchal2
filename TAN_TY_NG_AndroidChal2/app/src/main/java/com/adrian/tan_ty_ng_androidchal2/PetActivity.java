package com.adrian.tan_ty_ng_androidchal2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PetActivity extends AppCompatActivity {

    TextView statusTextView, messageTextView;
    Button snackButton, mealButton, kingButton, releaseButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);

        statusTextView = findViewById(R.id.statusTextView);
        messageTextView = findViewById(R.id.messageTextView);
        snackButton = findViewById(R.id.snackButton);
        mealButton = findViewById(R.id.mealButton);
        kingButton = findViewById(R.id.kingButton);
        releaseButton = findViewById(R.id.releaseButton);

        releaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("has_pet", "none");
                editor.apply();

                Intent intent = new Intent( PetActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void startAlert(int seconds) {
        EditText text = (EditText) findViewById(R.id.time);
        int i = Integer.parseInt(text.getText().toString());
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (seconds * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",
                Toast.LENGTH_LONG).show();
    }
}
