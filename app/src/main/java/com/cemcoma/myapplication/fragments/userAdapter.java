package com.cemcoma.myapplication.fragments;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.listings.MessageRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userHolder> {

    private ArrayList<User> mUserList;
    private Context mContext;
    private View v;
    private User mUser;
    private int kpos;
    private Dialog messageDialog;
    private ImageView imgCancel;
    private LinearLayout sendLinear;
    private CircleImageView imgProfile;
    private EditText editMessage;
    private String textMessage;
    private Window messageWindow;
    private TextView textName;
    private FirebaseFirestore mFireStore;
    private DocumentReference mRef;
    private String mUID, mName, mProfileUrl, channelID, messageDocID;
    private MessageRequest messageRequest;
    private HashMap<String, Object> mData;



    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        v = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new userHolder(v);
    }

    public userAdapter(ArrayList<User> mUserList, Context mContext, String mUID, String mName, String mProfileUrl) {
        this.mUserList = mUserList;
        this.mContext = mContext;
        this.mUID = mUID;
        this.mName = mName;
        this.mProfileUrl = mProfileUrl;

        mFireStore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        mUser = mUserList.get(position);
        holder.userName.setText(mUser.getUsername());

        if (mUser.getProfileUrl().equals("default") )
            holder.userProfile.setImageResource(R.drawable.bookmate_logo);
        else
            Picasso.with(mContext).load(mUser.getProfileUrl()).resize(70, 70).into(holder.userProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kpos = holder.getAdapterPosition();

                if (kpos != RecyclerView.NO_POSITION){
                    mRef = mFireStore.collection("users").document(mUID).collection("kanal")
                            .document(mUserList.get(kpos).getUID());

                    mRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){

                            }
                            else
                                sendMessageDialog(mUserList.get(kpos));
                        }
                    });
                }
            }
        });
    }

    private void sendMessageDialog(final User user) {
        messageDialog = new Dialog(mContext);
        messageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        messageWindow = messageDialog.getWindow();
        messageWindow.setGravity(Gravity.CENTER);
        messageDialog.setContentView(R.layout.custom_dialog_send_message);

        imgCancel = messageDialog.findViewById(R.id.custom_dialog_send_message_imgCancel);
        sendLinear = messageDialog.findViewById(R.id.custom_dialog_send_message_sendLinear);
        imgProfile = messageDialog.findViewById(R.id.custom_dialog_send_message_imgUserProfile);
        editMessage = messageDialog.findViewById(R.id.custom_dialog_send_message_editMessage);
        textName = messageDialog.findViewById(R.id.custom_dialog_send_message_imgUserName);

        textName.setText(user.getUsername());

        if (user.getProfileUrl().equals("default"))
            imgProfile.setImageResource(R.drawable.bookmate_logo);
        else
            Picasso.with(mContext).load(user.getProfileUrl()).resize(126,126).into(imgProfile);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog.dismiss();
            }
        });

        sendLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessage = editMessage.getText().toString();

                if (!TextUtils.isEmpty(textMessage)){
                    channelID = UUID.randomUUID().toString();

                    messageRequest = new MessageRequest(channelID, mUID, mName, mProfileUrl);
                    mFireStore.collection("MessageRequests").document(user.getUID())
                            .collection("requests").document(mUID).set(messageRequest)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        mData = new HashMap<>();
                                        mData.put("messageContent", textMessage);
                                        mData.put("sender", mUID);
                                        mData.put("receiver", user.getUID());
                                        mData.put("messageType", "text");
                                        mData.put("MessageDate", FieldValue.serverTimestamp());
                                        mData.put("docId", messageDocID);


                                        messageDocID = UUID.randomUUID().toString();
                                        mFireStore.collection("chatChannels").document(channelID)
                                                .collection("Messages").document(messageDocID).set(mData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(mContext, "Your message request has been successfully sent.", Toast.LENGTH_SHORT).show();
                                                            if (messageDialog.isShowing())
                                                                messageDialog.dismiss();
                                                        }
                                                        else
                                                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else
                                        Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                    Toast.makeText(mContext, "You cannot send blank messages.", Toast.LENGTH_SHORT).show();
            }
        });

        messageWindow.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        messageDialog.show();
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class userHolder extends RecyclerView.ViewHolder{

        TextView userName;
        CircleImageView userProfile;


        public userHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_item_txtusername);
            userProfile = itemView.findViewById(R.id.user_item_imguserprofile);
        }
    }
}
