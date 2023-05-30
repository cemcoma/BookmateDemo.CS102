package com.cemcoma.myapplication.listings;

import android.content.Context;
import android.text.NoCopySpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatHolder> {
    private static final int MESSAGE_LEFT = 1;
    private static final int MESSAGE_RIGHT = 0;


    private ArrayList<chat> mChatList;
    private Context mContext;
    private String TargetProfile;
    private String mUID;
    private View v;
    private chat mChat;

    public chatAdapter(ArrayList<chat> mChatList, Context mContext, String mUID, String TargetProfile) {
        this.mChatList = mChatList;
        this.mContext = mContext;
        this.mUID = mUID;
        this.TargetProfile = TargetProfile;
    }

    @NonNull
    @Override
    public chatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_LEFT) {
            v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
        } else if (viewType == MESSAGE_RIGHT) {
            v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        }

        return new chatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull chatHolder holder, int position) {
        mChat = mChatList.get(position);
        holder.txtMessage.setText(mChat.getMessageContent());

        if (!mChat.getSender().equals(mUID)){
            if (TargetProfile.equals("default"))
                holder.imgProfile.setImageResource(R.drawable.bookmate_logo);
            else
                Picasso.with(mContext).load(TargetProfile).resize(56,56).into(holder.imgProfile);
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    class chatHolder extends RecyclerView.ViewHolder{
        private CircleImageView imgProfile;
        private TextView txtMessage;

        public chatHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.chat_item_imgProfile);
            txtMessage = itemView.findViewById(R.id.chat_item_txtMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position).getSender().equals(mUID))
            return MESSAGE_RIGHT;
        else
            return MESSAGE_LEFT;
    }
}
