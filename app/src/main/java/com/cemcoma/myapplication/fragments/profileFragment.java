package com.cemcoma.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.activities.userLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class profileFragment extends Fragment {
    ImageView pImage;
    TextView ratingView;
    FirebaseAuth auth;
    Button button;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        button = v.findViewById(R.id.button);
        user = auth.getCurrentUser();
        pImage = (ImageView) v.findViewById(R.id.profileImageView);
        ratingView = (TextView) v.findViewById(R.id.ratingView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =  new Intent(getActivity(), userLoginActivity.class);
                startActivity(intent);
            }
        });





        return v;
    }
}