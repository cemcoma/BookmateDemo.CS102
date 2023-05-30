package com.cemcoma.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.favourites.book;
import com.cemcoma.myapplication.favourites.bookAdapter;
import com.cemcoma.myapplication.listings.listingLd;
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

public class favouritesActivity extends AppCompatActivity implements RecylerviewInterface {
    private RecyclerView recyclerView, recyclerViewld;
    private ImageButton goBack;

    private EditText searchBar;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;
    private User user;
    private List<book> listing;
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        recyclerView = findViewById(R.id.favouritesRecylerView);
        mStorage = FirebaseStorage.getInstance(); //photolar burada kaydolcak
        mFirestore = FirebaseFirestore.getInstance();
        listing = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        user = new User(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
        button = findViewById(R.id.addFavouritesButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),addFavouritesActivity.class);
                startActivity(intent);
            }
        });

        goBack = findViewById(R.id.goback2);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        Query query = mFirestore.collection("favourites").whereEqualTo("owner", username);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot docSnap : queryDocumentSnapshots) {
                    String bookname = docSnap.get("bookname").toString();
                    String author = docSnap.get("author").toString();
                    String url = docSnap.get("imageString").toString();
                    String owner = docSnap.get("owner").toString();

                    listing.add(new book(bookname, author, url, owner));
                }
                showListings();
            }
        });
    }

    private void showListings() {
        recyclerView.setAdapter(new bookAdapter(getApplicationContext(), listing, this));
    }


    @Override
    public void onListingClick(int position) {
        book listingToBeDeleted = listing.get(position);
        mFirestore.collection("favourites").document(listingToBeDeleted.getBookname() + "-" + FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
        Toast.makeText(this, "Listing removed", Toast.LENGTH_SHORT).show();
        listing.remove(position);
        showListings();
    }
}