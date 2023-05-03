package com.cemcoma.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cemcoma.myapplication.R;

public class chatFragment extends Fragment {
    private chatPiece chatPiece;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.fragment_chat, container, false);

        addChatPiece("cemcoma");






        return v;
    }

    private void addChatPiece(String username) {
        chatPiece = new chatPiece();


    }
}