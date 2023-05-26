package com.cemcoma.myapplication.fragments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userHolder> {

    private ArrayList<User> mUserList;
    private Context mContext;
    private View v;
    private User mUser;

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        v = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new userHolder(v);
    }

    public userAdapter(ArrayList<User> mUserList, Context mContext) {
        this.mUserList = mUserList;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        mUser = mUserList.get(position);
        holder.userName.setText(mUser.getUsername());

        Picasso.with(mContext).load(mUser.getProfileUrl()).resize(70, 70).into(holder.userProfile);



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
