package com.cemcoma.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cemcoma.myapplication.R;

public class userLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button userEnterButton;
    private EditText usernameEditView, passwordEditView;
    private TextView usernameTextView;
    private String username, password;
    private ImageView bookmateLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createObjects();
    }

    private void createObjects() {


        passwordEditView = (EditText) findViewById(R.id.editTextPassword);
        usernameEditView = (EditText) findViewById(R.id.editTextName);

        userEnterButton = (Button) findViewById(R.id.userEntryButton);
        userEnterButton.setOnClickListener(this);
        bookmateLogo = (ImageView) findViewById(R.id.logoView);
        bookmateLogo.setBackgroundResource(R.drawable.new_piskel_1_png);
    }

    @Override
    public void onClick (View view) {
        username = usernameEditView.getText().toString();
        password = passwordEditView.getText().toString();

        Intent intent  = new Intent(userLoginActivity.this, dashboardActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}