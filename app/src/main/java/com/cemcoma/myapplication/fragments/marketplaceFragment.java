package com.cemcoma.myapplication.fragments;

import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.listings.*;
import com.cemcoma.myapplication.activities.addListingActivity;

import android.content.Context;
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
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.callback;
import com.cemcoma.myapplication.listings.mpAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class marketplaceFragment extends Fragment implements View.OnClickListener, RecylerviewInterface { //Mp = Marketplace
    private ImageButton searchButton, addListingButton;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private  List<listingMp> listing;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marketplace, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewMp);
        searchBar = (EditText) v.findViewById(R.id.searchBarMp);
        searchButton = (ImageButton) v.findViewById(R.id.searchButtonMp);
        addListingButton = (ImageButton) v.findViewById(R.id.addListingButton);
        searchButton.setOnClickListener(this);
        addListingButton.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance(); //photolar burada kaydolcak
        mFirestore = FirebaseFirestore.getInstance();
        listing = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewMp);
        recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));

        initialListings();
        return v;
    }

    private void initialListings() {
        Query query = mFirestore.collection("mp-listings").orderBy("price").limit(5);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot docSnap : queryDocumentSnapshots) {
                    String bookname = docSnap.get("name").toString();
                    String author = docSnap.get("author").toString();
                    String sellername = docSnap.get("sellername").toString();
                    int price = Integer.parseInt(docSnap.get("price").toString());
                    double rating = Double.parseDouble(docSnap.get("rating").toString().substring(0,3));
                    String url = docSnap.get("imageUrl").toString();

                    listing.add(new listingMp(bookname, author, sellername, price, rating, url));
                }
                showListings();
            }
        });

    }

    private void getSpecifiedListings(String search) {
        listing.clear();
        if(search.equals("")) initialListings();
        else {
            Query query = mFirestore.collection("mp-listings");
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot docSnap : queryDocumentSnapshots) {
                        String bookname = docSnap.get("name").toString();
                        if(bookname.toLowerCase().contains(search.toLowerCase())) {
                            String author = docSnap.get("author").toString();
                            String sellername = docSnap.get("sellername").toString();
                            int price = Integer.parseInt(docSnap.get("price").toString());
                            double rating = Double.parseDouble(docSnap.get("rating").toString().substring(0,3));
                            String url = docSnap.get("imageUrl").toString();

                            listing.add(new listingMp(bookname, author, sellername, price, rating, url));
                        }
                    }
                    if(listing.size() == 0) {
                        searchUnsuccessfull();
                    } else showListings();
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == addListingButton.getId()) {
            Intent intent = new Intent(getActivity() , addListingActivity.class);
            startActivity(intent);
        } else if(view.getId() == searchButton.getId()) {
            getSpecifiedListings(searchBar.getText().toString());
        }

    }


    @Override
    public void onListingClick(int position) {
        profileFragment profileFragment = new profileFragment();

        //TODO: position'a göre user alacaksın, onun profiline yönlendireceksin ama şuan olmaz çünkü kimsenin profili yok

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,profileFragment);
        transaction.commit();
    }
    private void searchUnsuccessfull() {
        Toast.makeText(this.getContext(), "Nothing Found!", Toast.LENGTH_SHORT).show();

    }

    private void showListings() {
        recyclerView.setAdapter(new mpAdapter(super.getContext(), listing, this));
    }
}