package com.cemcoma.myapplication.fragments;

import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.listings.*;
import com.cemcoma.myapplication.activities.addListingActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.callback;
import com.cemcoma.myapplication.listings.mpAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class marketplaceFragment extends Fragment implements View.OnClickListener, RecylerviewInterface { //Mp = Marketplace
    private ImageButton[] imageButtonArr;
    private String[] imageUrlArr;
    private Uri[] imageUriArr;
    private ImageButton searchButton, addListingButton;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private  List<listingMp> listing;
    private Uri imageUri;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marketplace, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewMp);
        searchBar = (EditText) v.findViewById(R.id.searchBarMp);
        searchButton = (ImageButton) v.findViewById(R.id.searchButtonMp);
        addListingButton = (ImageButton) v.findViewById(R.id.addListingButton);
        searchButton.setOnClickListener(this);
        addListingButton.setOnClickListener(this);

        listing = new ArrayList<>();
        listing.add(new listingMp("Biologic Calculus", "Campbell", "Efeler", 20, 4.2, "zort"));



        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewMp);
        recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));


        mStorage = FirebaseStorage.getInstance(); //photolar burada kaydolcak
        mFirestore = FirebaseFirestore.getInstance();


        recyclerView.setAdapter(new mpAdapter(super.getContext(), listing, this));
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == addListingButton.getId()) {
            Intent intent = new Intent(getActivity() , addListingActivity.class);
            startActivity(intent);
        } else if(view.getId() == searchButton.getId()) {
            //TODO: Listingleri searchbar a göre koyacan yeni list yap filan
            //TODO: alltaki örnek ve çalışıyor yey
            listing.add(new listingMp("Cengage", "Johnston", "Cemcoma", 15, 4.7, "zort"));
            recyclerView.setAdapter(new mpAdapter(super.getContext(), listing, this));
        }

    }


    @Override
    public void onListingClick(int position) {
        profileFragment profileFragment = new profileFragment();

        //TODO: position'a göre user alacaksın, onun profiline yönlendireceksin ama şuan olmaz

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,profileFragment);
        transaction.commit();
    }
}