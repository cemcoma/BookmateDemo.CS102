package com.cemcoma.myapplication.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private EditText editMessage;
    private CircleImageView targetProfile;
    private TextView targetName;
    private Intent receivedIntent;
    private String targetID;
    private User targetUser;
    private DocumentReference targetRef;
    private FirebaseFirestore mFireStore;


    private void init(){
        mRecycleView = findViewById(R.id.chat_activity_recycleView);
        editMessage = findViewById(R.id.chat_activity_editMessage);
        targetProfile = findViewById(R.id.chat_activity_imgTargetProfie);
        targetName = findViewById(R.id.chat_activity_txtTargetName);

        mFireStore = FirebaseFirestore.getInstance();
        receivedIntent = getIntent();
        targetID = receivedIntent.getStringExtra("targetID");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

        targetRef = mFireStore.collection("users").document(targetID);
        targetRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null && value.exists()) {
                    targetUser = value.toObject(User.class);

                    if (targetUser != null){
                        targetName.setText(targetUser.getUsername());

                        if (targetUser.getProfileUrl().equals("default")) {
                            targetProfile.setImageResource(R.drawable.bookmate_logo);
                        }
                        else
                            Picasso.with(ChatActivity.this).load(targetUser.getProfileUrl()).resize(66,66).into(targetProfile);
                    }
                }
            }
        });

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void btnSendMessage(View v){

    }

    public void btnCloseChat(View v){
        finish();
    }
}