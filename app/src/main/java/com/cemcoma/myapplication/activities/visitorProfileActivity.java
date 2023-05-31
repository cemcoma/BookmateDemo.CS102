package com.cemcoma.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.callback;
import com.cemcoma.myapplication.favourites.book;
import com.cemcoma.myapplication.favourites.bookAdapter;
import com.cemcoma.myapplication.listings.MessageRequest;
import com.cemcoma.myapplication.listings.listingMp;
import com.cemcoma.myapplication.listings.mpAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class visitorProfileActivity extends AppCompatActivity implements RecylerviewInterface {
    private ImageView pImage;
    private ImageButton backButton, chatButton;
    private HashMap<String,Boolean> prefs;
    private String UIDVisiting, username, UIDVisited, URlVisited;

    protected TextView ratingView, usernameView, preferencesView, listingView, favoritesView;
    protected FirebaseAuth auth;
    protected FirebaseUser authUser;
    protected User visitingUser, visitedUser;
    private List<listingMp> listing;
    private List<book> listingBook;
    private RecyclerView recyclerView, recyclerViewBook;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_profile);
        auth = FirebaseAuth.getInstance();

        auth = FirebaseAuth.getInstance();
        authUser = auth.getCurrentUser();
        assert authUser != null;
        visitingUser = new User(authUser);
        prefs = visitingUser.getPreferences();
        UIDVisiting = visitingUser.getUID();
        pImage = (ImageView) findViewById(R.id.profileImageViewV);
        ratingView = (TextView) findViewById(R.id.ratingViewV);
        usernameView = findViewById(R.id.usernameTextViewV);
        preferencesView = findViewById(R.id.preferencesTextViewV);
        listingView = findViewById(R.id.listingsTextViewV);
        favoritesView = findViewById(R.id.favoritesTextViewV);
        recyclerView = findViewById(R.id.profileRecylerViewListingV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewBook = findViewById(R.id.recylerviewBookV);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listing = new ArrayList<>();
        listingBook = new ArrayList<>();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        backButton = findViewById(R.id.finishButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chatButton = findViewById(R.id.chatButton);


        initializeValues(new callback() {
            @Override
            public void callbackString(String callbackString) {
                initializeListings(username);
                initializeFavorites(username);
                URlVisited = callbackString;
                visitedUser = new User(UIDVisited);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startChat();}
        });
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


        FirebaseFirestore.getInstance().collection("users").whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                usernameView.setText(documentSnapshot.get("username").toString());
                prefs = (HashMap<String, Boolean>) documentSnapshot.get("preferences");
                String r = "Favorite Genres:  ";
                for (String s : prefs.keySet()) {
                    if (prefs.get(s)) {
                        r = r + s + ", ";
                    }
                }
                preferencesView.setText(r.substring(0,r.length()-2));
                UIDVisited = documentSnapshot.get("UID").toString();
                callback.callbackString(documentSnapshot.get("profileUrl").toString());

                if(!documentSnapshot.get("profileUrl").toString().equals("default")) Picasso.with(getApplicationContext()).load(documentSnapshot.get("profileUrl").toString()).fit().centerCrop().into(pImage);
                int rating1 = Integer.parseInt(documentSnapshot.get("rating1").toString());
                int rating2 = Integer.parseInt(documentSnapshot.get("rating2").toString());
                int rating3 = Integer.parseInt(documentSnapshot.get("rating3").toString());
                int rating4 = Integer.parseInt(documentSnapshot.get("rating4").toString());
                int rating5 = Integer.parseInt(documentSnapshot.get("rating5").toString());

                if((rating5+rating4+rating3+rating2+rating1) <= 0) ratingView.setText(0.5 + "");
                else ratingView.setText(Double.toString((rating1 + rating2*2 + rating3*3 + rating4*4 + rating5*5)/(rating5+rating4+rating3+rating2+rating1 * 1.0)).substring(0,3));
            }
            }
        });

    }
    private void showListingsMp() {
        recyclerView.setAdapter(new mpAdapter(this.getApplicationContext(), listing, this));
    }

    private void showListingsFavourites() {
        recyclerViewBook.setAdapter(new bookAdapter(this.getApplicationContext(), listingBook, this));
    }

    private void startChat() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("kanal")
                .document(UIDVisited).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Intent chatIntent = new Intent(visitorProfileActivity.this, ChatActivity.class);
                            chatIntent.putExtra("channelID", documentSnapshot.getData().get("kanalID").toString());
                            chatIntent.putExtra("targetProfile", URlVisited);
                            chatIntent.putExtra("targetID", UIDVisited);
                            //chatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(chatIntent);
                        } else {
                            sendMessageDialog(visitedUser);
                        }
                    }
                });
    }

    private void sendMessageDialog(final User user) {
        Dialog messageDialog = new Dialog(visitorProfileActivity.this);
        messageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window messageWindow = messageDialog.getWindow();
        messageWindow.setGravity(Gravity.CENTER);
        messageDialog.setContentView(R.layout.custom_dialog_send_message);

        View imgCancel = messageDialog.findViewById(R.id.custom_dialog_send_message_imgCancel);
        View sendLinear = messageDialog.findViewById(R.id.custom_dialog_send_message_sendLinear);
        CircleImageView imgProfile = messageDialog.findViewById(R.id.custom_dialog_send_message_imgUserProfile);
        EditText editMessage = messageDialog.findViewById(R.id.custom_dialog_send_message_editMessage);
        TextView textName = messageDialog.findViewById(R.id.custom_dialog_send_message_imgUserName);

        textName.setText(user.getUsername());

        if (user.getProfileUrl().equals("default"))
            imgProfile.setImageResource(R.drawable.bookmate_logo);
        else
            Picasso.with(visitorProfileActivity.this).load(user.getProfileUrl()).resize(126,126).into(imgProfile);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog.dismiss();
            }
        });

        sendLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editMessage.getText().toString();

                if (!TextUtils.isEmpty(textMessage)){
                    String channelID = UUID.randomUUID().toString();

                    MessageRequest messageRequest = new MessageRequest(channelID, UIDVisiting, visitingUser.getUsername(), visitingUser.getProfileUrl());
                    FirebaseFirestore.getInstance().collection("MessageRequests").document(user.getUID())
                            .collection("requests").document(UIDVisiting).set(messageRequest)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        HashMap<String,Object>mData = new HashMap<>();
                                        mData.put("messageContent", textMessage);
                                        mData.put("sender", UIDVisiting);
                                        mData.put("receiver", user.getUID());
                                        mData.put("messageType", "text");
                                        mData.put("MessageDate", FieldValue.serverTimestamp());
                                        String messageDocID = UUID.randomUUID().toString();
                                        mData.put("docId", messageDocID);



                                        FirebaseFirestore.getInstance().collection("chatChannels").document(channelID)
                                                .collection("Messages").document(messageDocID).set(mData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(visitorProfileActivity.this, "Your message request has been successfully sent.", Toast.LENGTH_SHORT).show();
                                                            if (messageDialog.isShowing())
                                                                messageDialog.dismiss();
                                                        }
                                                        else
                                                            Toast.makeText(visitorProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else
                                        Toast.makeText(visitorProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                    Toast.makeText(visitorProfileActivity.this, "You cannot send blank messages.", Toast.LENGTH_SHORT).show();
            }
        });

        messageWindow.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        messageDialog.show();
    }

    @Override
    public void onListingClick(int position) {
        //do nothing...
    }
}