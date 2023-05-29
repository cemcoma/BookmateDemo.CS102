package com.cemcoma.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.listings.listingMp;
import com.cemcoma.myapplication.listings.mpAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class userListingsActivity extends AppCompatActivity implements RecylerviewInterface {
    private RecyclerView recyclerView;
    private ImageButton searchButton, addListingButton;

    private EditText searchBar;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private User user;
    private List<listingMp> listing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listings);
        recyclerView = findViewById(R.id.listingsRecylerView);
        mStorage = FirebaseStorage.getInstance(); //photolar burada kaydolcak
        mFirestore = FirebaseFirestore.getInstance();
        listing = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        user = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        Query query = mFirestore.collection("mp-listings").whereEqualTo("sellername", username).limit(5);
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
    private void showListings() {
        recyclerView.setAdapter(new mpAdapter(getApplicationContext(), listing, this));
    }

    @Override
    public void onListingClick(int position) {
        listingMp listingToBeDeleted = listing.get(position);
        mFirestore.collection("mp-listings").document(listingToBeDeleted.getBookname() + "-" + FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
        Toast.makeText(this, "Listing removed", Toast.LENGTH_SHORT).show();
        listing.remove(position);
    }
}