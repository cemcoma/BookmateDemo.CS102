package com.cemcoma.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.activities.favouritesActivity;
import com.cemcoma.myapplication.activities.userListingsActivity;
import com.cemcoma.myapplication.activities.userLoginActivity;
import com.cemcoma.myapplication.callback;
import com.cemcoma.myapplication.favourites.book;
import com.cemcoma.myapplication.favourites.bookAdapter;
import com.cemcoma.myapplication.listings.listingMp;
import com.cemcoma.myapplication.listings.mpAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class profileFragment extends Fragment implements RecylerviewInterface{
    private ImageView pImage;
    protected TextView ratingView, usernameView, preferencesView, listingView, favoritesView;
    protected FirebaseAuth auth;
    protected Button button;
    protected FirebaseUser authUser;
    protected User user;
    private List<listingMp> listing;
    private List<book> listingBook;
    private RecyclerView recyclerView, recyclerViewBook;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        button = v.findViewById(R.id.button);
        auth = FirebaseAuth.getInstance();
        authUser = auth.getCurrentUser();
        assert authUser != null;
        user = new User(authUser);
        pImage = (ImageView) v.findViewById(R.id.profileImageView);
        ratingView = (TextView) v.findViewById(R.id.ratingView);
        usernameView = v.findViewById(R.id.usernameTextView);
        preferencesView = v.findViewById(R.id.preferencesTextView);
        listingView = v.findViewById(R.id.listingsTextView);
        favoritesView = v.findViewById(R.id.favoritesTextView);
        recyclerView = v.findViewById(R.id.profileRecylerViewListing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBook = v.findViewById(R.id.recylerviewBook);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(getContext()));

        listing = new ArrayList<>();
        listingBook = new ArrayList<>();
        listingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToListings = new Intent(getActivity(), userListingsActivity.class);
                intentToListings.putExtra("username", user.getUsername());
                startActivity(intentToListings);
            }
        });

        favoritesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToFavourites = new Intent(getActivity(), favouritesActivity.class);
                intentToFavourites.putExtra("username", user.getUsername());
                startActivity(intentToFavourites);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =  new Intent(getActivity(), userLoginActivity.class);
                startActivity(intent);
            }
        });

        initializeValues(new callback() {
            @Override
            public void callbackString(String callbackString) {
                initializeListings(callbackString);
                initializeFavorites(callbackString);
            }
        });


        return v;
    }

    private void initializeFavorites(String username) {

        Query query = FirebaseFirestore.getInstance().collection("favourites").whereEqualTo("owner", username).limit(5);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot docSnap : queryDocumentSnapshots) {
                    String bookname = docSnap.get("bookname").toString();
                    String author = docSnap.get("author").toString();
                    String url = docSnap.get("imageString").toString();

                    listingBook.add(new book(bookname, author, url, username));
                }
                showListingsFavourites();
            }
        });

    }

    private void initializeListings(String username) {
        Query query = FirebaseFirestore.getInstance().collection("mp-listings").whereEqualTo("sellername", username).limit(5);
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
                showListingsMp();
            }
        });
    }

    private void initializeValues(final callback callback) {

        FirebaseFirestore.getInstance().collection("users").document(authUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usernameView.setText(documentSnapshot.get("username").toString());
                callback.callbackString(documentSnapshot.get("username").toString());

                if(!documentSnapshot.get("profileUrl").toString().equals("default")) Picasso.with(getContext()).load(documentSnapshot.get("profileUrl").toString()).fit().centerCrop().into(pImage);
                int rating1 = Integer.parseInt(documentSnapshot.get("rating1").toString());
                int rating2 = Integer.parseInt(documentSnapshot.get("rating2").toString());
                int rating3 = Integer.parseInt(documentSnapshot.get("rating3").toString());
                int rating4 = Integer.parseInt(documentSnapshot.get("rating4").toString());
                int rating5 = Integer.parseInt(documentSnapshot.get("rating5").toString());

                if((rating5+rating4+rating3+rating2+rating1) <= 0) ratingView.setText(0.5 + "");
                else ratingView.setText(Double.toString((rating1 + rating2*2 + rating3*3 + rating4*4 + rating5*5)/(rating5+rating4+rating3+rating2+rating1 * 1.0)).substring(0,3));
            }
        });

    }
    private void showListingsMp() {
        recyclerView.setAdapter(new mpAdapter(this.getContext(), listing, this));
    }

    private void showListingsFavourites() {
        recyclerViewBook.setAdapter(new bookAdapter(this.getContext(), listingBook, this));
    }

    @Override
    public void onListingClick(int position) {
        //do nothing...
    }
}