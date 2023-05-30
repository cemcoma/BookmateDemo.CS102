package com.cemcoma.myapplication.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.listings.MessageRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRequestAdapter extends RecyclerView.Adapter<MessageRequestAdapter.MessageRequestsHolder> {

    private ArrayList<MessageRequest> mMessageRequestsList;
    private Context mContext;
    private MessageRequest messageRequest, newMessageRequest;
    private View v;
    private int mPos;
    private FirebaseFirestore mFireStore;
    private String mUID, mName, mProfileUrl;

    public MessageRequestAdapter(ArrayList<MessageRequest> mMessageRequestsList, Context mContext, String mUID, String mName, String mProfileUrl) {
        this.mMessageRequestsList = mMessageRequestsList;
        this.mContext = mContext;
        this.mUID = mUID;
        this.mName = mName;
        this.mProfileUrl = mProfileUrl;

        mFireStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MessageRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(mContext).inflate(R.layout.received_message_requests_item, parent, false);
        return new MessageRequestsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRequestsHolder holder, int position) {
        messageRequest = mMessageRequestsList.get(position);
        holder.txtMessage.setText(messageRequest.getUserName() + " wants to send you a message.");

        if (messageRequest.getUserProfile().equals("default"))
            holder.imgProfile.setImageResource(R.drawable.bookmate_logo);
        else
            Picasso.with(mContext).load(messageRequest.getUserProfile()).resize(77,77).into(holder.imgProfile);

        holder.imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPos = holder.getAdapterPosition();

                if (mPos != RecyclerView.NO_POSITION){
                    newMessageRequest = new MessageRequest(mMessageRequestsList.get(mPos).getKanalID(), mMessageRequestsList.get(mPos).getUserID(),
                            mMessageRequestsList.get(mPos).getUserName(), mMessageRequestsList.get(mPos).getUserProfile());

                    mFireStore.collection("users").document(mUID).collection("kanal").document(mMessageRequestsList.get(mPos).getUserID())
                            .set(newMessageRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        newMessageRequest = new MessageRequest(mMessageRequestsList.get(mPos).getKanalID(), mUID, mName, mProfileUrl);

                                        mFireStore.collection("users").document(mMessageRequestsList.get(mPos).getUserID()).collection("kanal")
                                                .document(mUID).set(newMessageRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful())
                                                            deleteMessageRequest(mMessageRequestsList.get(mPos).getUserID(), "Message request accepted.");
                                                        else
                                                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    }else
                                        Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });

        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPos = holder.getAdapterPosition();

                if (mPos != RecyclerView.NO_POSITION)
                    deleteMessageRequest(mMessageRequestsList.get(mPos).getUserID(), "Message request rejected.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageRequestsList.size();
    }

    class MessageRequestsHolder extends RecyclerView.ViewHolder{

        CircleImageView imgProfile;
        TextView txtMessage;
        ImageView imgCancel, imgAccept;

        public MessageRequestsHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.received_message_requests_item_imgProfile);
            txtMessage = itemView.findViewById(R.id.received_message_requests_item_txtMessage);
            imgCancel = itemView.findViewById(R.id.received_message_requests_item_imgCancel);
            imgAccept = itemView.findViewById(R.id.received_message_requests_item_imgAccept);

        }
    }

    private void deleteMessageRequest(String targetUUID, final String messageContent){
        mFireStore.collection("MessageRequests").document(mUID).collection("requests").document(targetUUID)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyDataSetChanged();
                            Toast.makeText(mContext, messageContent, Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
