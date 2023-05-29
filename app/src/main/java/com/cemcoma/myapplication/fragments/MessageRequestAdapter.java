package com.cemcoma.myapplication.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.listings.MessageRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRequestAdapter extends RecyclerView.Adapter<MessageRequestAdapter.MessageRequestsHolder> {

    private ArrayList<MessageRequest> mMessageRequestsList;
    private Context mContext;
    private MessageRequest messageRequest;
    private View v;

    public MessageRequestAdapter(ArrayList<MessageRequest> mMessageRequestsList, Context mContext) {
        this.mMessageRequestsList = mMessageRequestsList;
        this.mContext = mContext;
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

}
