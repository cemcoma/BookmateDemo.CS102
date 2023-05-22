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


public class User implements Parcelable {
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

    protected User(Parcel in) {
        username = in.readString();
        UID = in.readString();
        password = in.readString();
        email = in.readString();
        profileUrl = in.readString();
        rating5 = in.readInt();
        rating4 = in.readInt();
        rating3 = in.readInt();
        rating2 = in.readInt();
        rating1 = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(UID);
        parcel.writeString(password);
        parcel.writeString(email);
        parcel.writeString(profileUrl);
        parcel.writeInt(rating5);
        parcel.writeInt(rating4);
        parcel.writeInt(rating3);
        parcel.writeInt(rating2);
        parcel.writeInt(rating1);
    }
}
