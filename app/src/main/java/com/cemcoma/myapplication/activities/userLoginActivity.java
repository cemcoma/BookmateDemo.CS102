package com.cemcoma.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userLoginActivity extends AppCompatActivity {

    private TextInputEditText editEmailText , editPasswordText;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView login_to_register;
    private String name;
    private static final String TAG = "EmailPassword";

    //Checking if a user is currently logged in --
    // from Firebase documentation https://firebase.google.com/docs/auth/android/password-auth?hl=en&authuser=0#java_6
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
        setContentView(R.layout.activity_user_login);

        editEmailText = findViewById(R.id.email);
        editPasswordText = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgressBar);
        login_to_register = findViewById(R.id.registerNow);
        mAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email , password;

                email = String.valueOf(editEmailText.getText());
                password = String.valueOf(editPasswordText.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(userLoginActivity.this , "Email cannot be empty!" , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(userLoginActivity.this , "Password cannot be empty!" , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                //From Firebase documentation -- https://firebase.google.com/docs/auth/android/password-auth?hl=en&authuser=0#java_6
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(userLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(userLoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(userLoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        login_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , userRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateUI(FirebaseUser mUser) {
        if (mUser == null) {
            return;
        }
        Intent intent = new Intent(userLoginActivity.this, dashboardActivity.class);
        startActivity(intent);

    }
}