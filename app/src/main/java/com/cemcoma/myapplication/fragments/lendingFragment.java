package com.cemcoma.myapplication.fragments;

import android.content.Intent;
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
import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.activities.addListingActivityLd;
import com.cemcoma.myapplication.activities.visitorProfileActivity;
import com.cemcoma.myapplication.listings.ldAdapter;
import com.cemcoma.myapplication.listings.listingLd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class lendingFragment extends Fragment implements RecylerviewInterface, View.OnClickListener {

    private User user;
    private ImageButton searchButton, addListingButton;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private List<listingLd> listing;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lending, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewLd);
        searchBar = (EditText) v.findViewById(R.id.searchBarLd);
        searchButton = (ImageButton) v.findViewById(R.id.searchButtonLd);
        addListingButton = (ImageButton) v.findViewById(R.id.addListingButtonLd);
        searchButton.setOnClickListener(this);
        addListingButton.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance(); //photolar burada kaydolcak
        mFirestore = FirebaseFirestore.getInstance();
        listing = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewLd);
        recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));

        initialListings();
        return v;
    }

    private void initialListings() {
        Query query = mFirestore.collection("ld-listings").limit(5);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot docSnap : queryDocumentSnapshots) {
                    String bookname = docSnap.get("name").toString();
                    String author = docSnap.get("author").toString();
                    String sellername = docSnap.get("borrowername").toString();
                    double rating = Double.parseDouble(docSnap.get("rating").toString().substring(0,3));
                    String url = docSnap.get("imageUrl").toString();

                    listing.add(new listingLd(bookname, author, sellername, rating, url));
                }
                showListings();
            }
        });

    }

    private void getSpecifiedListings(String search) {
        listing.clear();
        if(search.equals("")) initialListings();
        else {
            Query query = mFirestore.collection("ld-listings");
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot docSnap : queryDocumentSnapshots) {
                        String bookname = docSnap.get("name").toString();
                        if(bookname.toLowerCase().contains(search.toLowerCase())) {
                            String author = docSnap.get("author").toString();
                            String borrowername = docSnap.get("borrowername").toString();
                            double rating = Double.parseDouble(docSnap.get("rating").toString().substring(0,3));
                            String url = docSnap.get("imageUrl").toString();

                            listing.add(new listingLd(bookname, author, borrowername, rating, url));
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
            Intent intent = new Intent(getActivity() , addListingActivityLd.class);
            startActivity(intent);
        } else if(view.getId() == searchButton.getId()) {
            getSpecifiedListings(searchBar.getText().toString());
        }

    }


    @Override
    public void onListingClick(int position) {
        Intent intent = new Intent(this.getContext(), visitorProfileActivity.class);
        intent.putExtra("username", listing.get(position).getBorrowername());
        startActivity(intent);
    }
    private void searchUnsuccessfull() {
        Toast.makeText(this.getContext(), "Nothing Found!", Toast.LENGTH_SHORT).show();

    }

    private void showListings() {
        recyclerView.setAdapter(new ldAdapter(super.getContext(), listing, this));
    }
}