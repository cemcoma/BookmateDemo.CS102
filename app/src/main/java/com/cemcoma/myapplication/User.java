package com.cemcoma.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;


public class User {
    private String username, UID, password, email, profileUrl;
    private int rating5, rating4, rating3, rating2, rating1;

    private FirebaseFirestore mFirestore;


    /**
     * Retrieves the current user's data by getting the current authenticated user and creates an instance so u can use it :)
     * @param mUser
     */
    public User(FirebaseUser mUser) {
        mFirestore = FirebaseFirestore.getInstance();
        UID = mUser.getUid();
        email = mUser.getEmail();
        mFirestore.collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username = documentSnapshot.get("username").toString();
                password = documentSnapshot.get("password").toString();
                profileUrl = documentSnapshot.get("profileUrl").toString();
                rating1 = Integer.parseInt(documentSnapshot.get("rating1").toString());
                rating2 = Integer.parseInt(documentSnapshot.get("rating2").toString());
                rating3 = Integer.parseInt(documentSnapshot.get("rating3").toString());
                rating4 = Integer.parseInt(documentSnapshot.get("rating4").toString());
                rating5 = Integer.parseInt(documentSnapshot.get("rating5").toString());

            }
        });
    }

    public double getRating() {
        if((rating5+rating4+rating3+rating2+rating1) <= 0) return 0.5;
        return  (rating1 + rating2*2 + rating3*3 + rating4*4 + rating5*5) / (rating5+rating4+rating3+rating2+rating1 * 1.0) ;
    }

    public String getUsername() {
        return username;
    }

    public String getUID() {
        return UID;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * Stores a new user in the database
     * @param mUser
     * @param username
     * @param password
     */
    public static void storeUser(FirebaseUser mUser, String username, String password) {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        String UID = mUser.getUid();
        String email = mUser.getEmail();

        HashMap<String,Object> mData = new HashMap<>();
        mData.put("username", username);
        mData.put("password",password);
        mData.put("profileUrl","default");
        mData.put("rating1",0);
        mData.put("rating2",0);
        mData.put("rating3",0);
        mData.put("rating4",0);
        mData.put("rating5",0);

        mFirestore.collection("users").document(UID).set(mData);
    }
}
