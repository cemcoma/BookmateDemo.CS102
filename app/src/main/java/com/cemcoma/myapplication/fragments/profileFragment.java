package com.cemcoma.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cemcoma.myapplication.R;


public class profileFragment extends Fragment {
    private ImageView pImage;
    private TextView ratingView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        pImage = (ImageView) v.findViewById(R.id.profileImageView);
        ratingView = (TextView) v.findViewById(R.id.ratingView);







        return v;
    }
}