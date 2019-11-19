package com.adrian.tan_ty_ng_androidchal2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PetActivity extends AppCompatActivity {

    TextView statusTextView, messageTextView;
    Button snackButton, mealButton, kingButton, releaseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        statusTextView = findViewById(R.id.statusTextView);
        messageTextView = findViewById(R.id.messageTextView);
        snackButton = findViewById(R.id.snackButton);
        mealButton = findViewById(R.id.mealButton);
        kingButton = findViewById(R.id.kingButton);
        releaseButton = findViewById(R.id.releaseButton);
    }
}
