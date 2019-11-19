package com.adrian.tan_ty_ng_androidchal2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button newPetButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);

        if (!sharedPreferences.contains("has_pet")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("has_pet", "none");
            editor.apply();
        }

        //Bypass
        // int user_id = sharedPreferences.getInt(SharedPrefConstants.PREF_USER_ID, 1);
        if (sharedPreferences.getString("has_pet", "none").equals("yes")){
            Intent intent = new Intent(MainActivity.this, PetActivity.class);
            startActivity(intent);
            finish();
        }

        newPetButton = findViewById(R.id.newPetButton);
        newPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("android_chal_2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("has_pet", "yes");
                editor.putString("new_pet", "yes");
                editor.putString("full", "no");
                editor.apply();

                Intent intent = new Intent(MainActivity.this, PetActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
