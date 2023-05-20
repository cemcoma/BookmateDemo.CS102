package com.cemcoma.myapplication;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class User {
    private String username, UID, password, email, profileUrl;
    private int rating5, rating4, rating3, rating2, rating1;

    private FirebaseFirestore mFirestore;



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
}
