package com.cemcoma.myapplication.fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.callback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


public class marketplaceFragment extends Fragment implements View.OnClickListener { //Mp = Marketplace
    private ImageButton[] imageButtonArr;
    private String[] imageUrlArr;
    private Uri[] imageUriArr;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private FirebaseStorage mStorage;
    private FirebaseFirestore mFirestore;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marketplace, container, false);

        recyclerView =(RecyclerView) v.findViewById(R.id.recylerViewMp);
        searchBar = (EditText) v.findViewById(R.id.searchBarMp);
        searchButton = (ImageButton) v.findViewById(R.id.searchButtonMp);
        searchButton.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance(); //photolar burada kaydolcak

        mFirestore = FirebaseFirestore.getInstance();

        initialListings(v, new callback() {
            int i = 0;
            @Override
            public void callbackString(String callbackString) {
                imageUrlArr[i] = callbackString;
                i++;
                if(i == 1) {
                    createButtons(i, v);
                }
            }
        });


        return v;
    }

    private void initialListings(View v, final callback callback) {
        imageButtonArr = new ImageButton[1];
        imageUrlArr = new String[imageButtonArr.length];
        mFirestore.collection("mp-listings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int i = 0;
                for(DocumentSnapshot docSnap : queryDocumentSnapshots) {
                   if(i < imageUrlArr.length) {

                       callback.callbackString(docSnap.get("imageUrl").toString());
                       i++;
                   }
                }
            }
        });
    }

    private void createButtons(int buttonCount, View v) {
            imageUriArr = new Uri[buttonCount];
            for(int i = 0; i < imageButtonArr.length; i++) {
                ImageButton btn = new ImageButton(v.getContext());
                int finalI = i;
                mStorage.getReference().child("mp-listings/"+imageUrlArr[i]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUriArr[finalI] = uri;
                    }
                });
                btn.setImageURI(imageUriArr[i]);
                //recyclerView.addView(btn); TODO: recyler view öğren sonra :(
                imageButtonArr[i] = btn;
            }
    }






    @Override
    public void onClick(View view) {
        if(view.getId() == searchButton.getId()) {

            //TODO: database'ten querry açıp bütün listing'leri alıp imageButtonArr'e eklicek, her seferinde yeni açabilirsin cemocan :)
        } else {
            for(ImageButton btn : imageButtonArr) {
                if(view.getId() == btn.getId()) {
                    //TODO: kullanıcıyı aktar listing'e bastıktan sonra
                }
            }
        }
    }
}