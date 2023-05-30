package com.cemcoma.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.listings.chat;
import com.cemcoma.myapplication.listings.chatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private FirebaseUser mUser;

    private RecyclerView mRecycleView;
    private EditText editMessage;
    private String txtMessage, DocID;
    private CircleImageView targetProfile;
    private TextView targetName;
    private Intent receivedIntent;
    private String targetID, channelID, StargetProfile;
    private User targetUser;
    private DocumentReference targetRef;
    private FirebaseFirestore mFireStore;
    private Query chatQuery;
    private ArrayList<chat> mChatList;
    private chat mChat;
    private chatAdapter chatAdapter;
    private HashMap<String, Object> mData;


    private void init(){
        mRecycleView = findViewById(R.id.chat_activity_recycleView);
        editMessage = findViewById(R.id.chat_activity_editMessage);
        targetProfile = findViewById(R.id.chat_activity_imgTargetProfie);
        targetName = findViewById(R.id.chat_activity_txtTargetName);

        mFireStore = FirebaseFirestore.getInstance();
        receivedIntent = getIntent();
        targetID = receivedIntent.getStringExtra("targetID");
        channelID = receivedIntent.getStringExtra("channelID");

        mChatList = new ArrayList<>();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        StargetProfile = receivedIntent.getStringExtra("targetProfile");

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
                            Picasso.with(ChatActivity.this).load(targetUser.getProfileUrl()).resize(76,76).into(targetProfile);
                    }
                }
            }
        });

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        chatQuery = mFireStore.collection("chatChannels").document(channelID).collection("Messages")
                .orderBy("MessageDate", Query.Direction.ASCENDING);
        chatQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null) {
                    mChatList.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        mChat = snapshot.toObject(chat.class);

                        if (mChat != null) {
                            mChatList.add(mChat);
                        }
                    }
                    chatAdapter = new chatAdapter(mChatList, ChatActivity.this, mUser.getUid(), StargetProfile);
                    mRecycleView.setAdapter(chatAdapter);
                }

            }
        });
    }

    public void btnSendMessage(View v){
        txtMessage = editMessage.getText().toString();

        if (!TextUtils.isEmpty(txtMessage)){
            DocID = UUID.randomUUID().toString();
            mData = new HashMap<>();
            mData.put("messageContent", txtMessage);
            mData.put("sender", mUser.getUid());
            mData.put("receiver", targetID);
            mData.put("messageType", "text");
            mData.put("MessageDate", FieldValue.serverTimestamp());
            mData.put("docId", DocID);

            mFireStore.collection("chatChannels").document(channelID).collection("Messages").document(DocID)
                    .set(mData).addOnCompleteListener(ChatActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) 
                                editMessage.setText("");
                            else
                                Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            
                        }
                    });
        }else
            Toast.makeText(ChatActivity.this, "Write something to send a message.", Toast.LENGTH_SHORT).show();


    }

    public void btnCloseChat(View v){
        finish();
    }
}