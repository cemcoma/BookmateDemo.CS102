package com.cemcoma.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.cemcoma.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ratingActivity extends AppCompatActivity {
    private ImageButton goBack, done;
    private RatingBar ratingBar;
    private String who, chanelID, targetID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        done = findViewById(R.id.doneButton);
        goBack = findViewById(R.id.goback4);
        ratingBar = findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        who = intent.getStringExtra("who");
        chanelID = intent.getStringExtra("channelID");
        targetID = intent.getStringExtra("targetID");

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whichRating = "rating1";
                if (ratingBar.getNumStars() == 1) whichRating = "rating1";
                else if(ratingBar.getNumStars() == 2) whichRating = "rating2";
                else if(ratingBar.getNumStars() == 3) whichRating = "rating3";
                else if(ratingBar.getNumStars() == 4) whichRating = "rating4";
                else if(ratingBar.getNumStars() == 5) whichRating = "rating5";

                FirebaseFirestore.getInstance().collection("users").document(targetID).update(whichRating, FieldValue.increment(1));
                if(who.equals("receiver")) {
                    FirebaseFirestore.getInstance().collection("chatChannels").document(chanelID).update("hasGivenRatingReceiver", true);
                    finish();
                } else {
                    FirebaseFirestore.getInstance().collection("chatChannels").document(chanelID).update("hasGivenRatingSender", true);
                    finish();
                }
            }
        });
    }
}