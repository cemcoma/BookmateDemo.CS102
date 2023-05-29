package com.cemcoma.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class preferencesActivity extends AppCompatActivity {

    Button button;
    CheckBox scifi , fantasy , humor , crime
            , thriller , romance , history , dystopian;
    HashMap<String , Boolean> preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        button = findViewById(R.id.preferencesButton);
        scifi = findViewById(R.id.checkBox2);
        fantasy = findViewById(R.id.checkBox3);
        humor = findViewById(R.id.checkBox6);
        crime = findViewById(R.id.checkBox7);
        thriller = findViewById(R.id.checkBox9);
        history = findViewById(R.id.checkBox10);
        romance = findViewById(R.id.checkBox11);
        dystopian = findViewById(R.id.checkBox12);

        preferences = new HashMap<>();

        preferences.put("scifi" , false);
        preferences.put("fantasy" , false);
        preferences.put("humor" , false);
        preferences.put("crime" , false);
        preferences.put("thriller" , false);
        preferences.put("history" , false);
        preferences.put("romance" , false);
        preferences.put("dystopian" , false);

        scifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("scifi" , true);
                }
                else {
                    preferences.replace("scifi" , false);
                }
            }
        });
        fantasy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("fantasy" , true);
                }
                else {
                    preferences.replace("fantasy" , false);
                }
            }
        });
        humor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("humor" , true);
                }
                else {
                    preferences.replace("humor" , false);
                }
            }
        });
        crime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("crime" , true);
                }
                else {
                    preferences.replace("crime" , false);
                }
            }
        });
        thriller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("thriller" , true);
                }
                else {
                    preferences.replace("thriller" , false);
                }
            }
        });
        history.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("history" , true);
                }
                else {
                    preferences.replace("history" , false);
                }
            }
        });
        romance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("romance" , true);
                }
                else {
                    preferences.replace("romance" , false);
                }
            }
        });
        dystopian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.replace("dystopian" , true);
                }
                else {
                    preferences.replace("dystopian" , false);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                FirebaseUser user = (FirebaseUser) extras.get("user");
                String username = extras.getString("username");
                String password = extras.getString("password");

                User.storeUser(user, username, password , preferences);

                Intent intent = new Intent(preferencesActivity.this , dashboardActivity.class);
                startActivity(intent);
            }
        });


    }

    public HashMap<String , Boolean> getPreferences() {
        return preferences;
    }
}