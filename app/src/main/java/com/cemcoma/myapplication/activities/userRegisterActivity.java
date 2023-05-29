package com.cemcoma.myapplication.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userRegisterActivity extends AppCompatActivity {

    EditText editEmailText , editPasswordText, editUsernameText;
    private static final String TAG = "EmailPassword";
    Button buttonReg;

    FirebaseAuth mAuth;

    ProgressBar progressBar;

    TextView register_to_login;

    //Checking if a user is currently logged in --
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        editUsernameText = findViewById(R.id.usernameText);
        editEmailText = findViewById(R.id.email);
        editPasswordText = findViewById(R.id.password);
        buttonReg = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.registerProgressBar);
        register_to_login = findViewById(R.id.loginNow);

        mAuth = FirebaseAuth.getInstance();


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email , password;

                email = String.valueOf(editEmailText.getText());
                password = String.valueOf(editPasswordText.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(userRegisterActivity.this , "Email cannot be empty!" , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(userRegisterActivity.this , "Password cannot be empty!" , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                //Registering a new user -- From Firebase documentation https://firebase.google.com/docs/auth/android/password-auth?hl=en&authuser=0#java_1
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(userRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(userRegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(userRegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        register_to_login.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event){

                register_to_login.setTextColor(getResources().getColor(R.color.purple_200 , getTheme()));
                return true;
            }
        });
        register_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , userLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            return;
        }

        Intent intent = new Intent(userRegisterActivity.this, preferencesActivity.class);
        intent.putExtra("user" , user);
        intent.putExtra("username" , editUsernameText.getText().toString());
        intent.putExtra("password" , editPasswordText.getText().toString());
        startActivity(intent);
    }


}