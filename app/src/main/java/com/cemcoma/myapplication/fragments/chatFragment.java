package com.cemcoma.myapplication.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.cemcoma.myapplication.listings.MessageRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class chatFragment extends Fragment {

    private RecyclerView mRecycleView;
    private View v;

    private userAdapter mAdapter;
    private FirebaseUser mUser;
    private ArrayList<User> mUserList;
    private User mKullanıcı;

    private FirebaseFirestore mFirestore;
    private Query mQuery, query;

    private Toolbar mToolbar;
    private RelativeLayout mRelaNotif;
    private TextView txtNtfCount;
    private MessageRequest mMessageRequest;
    private ArrayList<MessageRequest> messageRequestList;
    private Dialog MessageRequestsDialog;
    private ImageView closeMessageRequests;
    private RecyclerView MessageRequestsRecycleView;
    private DocumentReference mRef;
    private User RefUser;
    private MessageRequestAdapter messageRequestAdapter;
    private DocumentReference userRef;
    private User RefKullanici;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        userRef = mFirestore.collection("users").document(mUser.getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                    RefKullanici = documentSnapshot.toObject(User.class);
            }
        });

        mUserList = new ArrayList<>();

        mRecycleView = v.findViewById(R.id.Users_fragment_recycleView);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));

        mToolbar = v.findViewById(R.id.ToolBar);
        mRelaNotif = v.findViewById(R.id.bar_layout_relaNotif);
        txtNtfCount = v.findViewById(R.id.bar_layout_notification_count);
        messageRequestList = new ArrayList<>();

        mRef = mFirestore.collection("users").document(mUser.getUid());
        mRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    RefUser = documentSnapshot.toObject(User.class);

                    mQuery = mFirestore.collection("users");
                    mQuery.addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT);
                            return;
                        }

                        if (value != null) {
                            mUserList.clear();

                            for (DocumentSnapshot snapshot: value.getDocuments()) {
                                mKullanıcı = snapshot.toObject(User.class);

                                assert mKullanıcı != null;
                                //if (!mKullanıcı.getUID().equals(mUser.getUid()))
                                mUserList.add(mKullanıcı);
                            }
                            mRecycleView.addItemDecoration(new LinearDecoration(20, mUserList.size()));
                            mAdapter = new userAdapter(mUserList, v.getContext(), RefUser.getUID(), RefUser.getUsername(), RefUser.getProfileUrl());
                            mRecycleView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        });

        query = mFirestore.collection("MessageRequests").document(mUser.getUid()).collection("requests");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null) {
                    txtNtfCount.setText(String.valueOf(value.getDocuments().size()));
                    messageRequestList.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        mMessageRequest = snapshot.toObject(MessageRequest.class);
                        messageRequestList.add(mMessageRequest);
                    }
                }
            }
        });
       mRelaNotif.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MessageRequestsDialog();
           }
       });


        return v;
    }

    class LinearDecoration extends RecyclerView.ItemDecoration{

        private int MarginQuantity;
        private int DataCount;

        public LinearDecoration(int marginQuantity, int dataCount) {
            MarginQuantity = marginQuantity;
            DataCount = dataCount;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);

            if (pos != DataCount - 1) {
                outRect.bottom = MarginQuantity;
            }
        }
    }

    public void MessageRequestsDialog(){
        MessageRequestsDialog = new Dialog(v.getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        MessageRequestsDialog.setContentView(R.layout.custom_dialog_received_message_requests);

        closeMessageRequests = MessageRequestsDialog.findViewById(R.id.received_request_message_imgClose);
        MessageRequestsRecycleView = MessageRequestsDialog.findViewById(R.id.custom_dialog_received_message_request_RecycleView);

        closeMessageRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageRequestsDialog.dismiss();
            }
        });

        MessageRequestsRecycleView.setHasFixedSize(true);
        MessageRequestsRecycleView.setLayoutManager(new LinearLayoutManager(v.getContext(),
                LinearLayoutManager.VERTICAL, false));

        if (messageRequestList.size() > 0){
            messageRequestAdapter = new MessageRequestAdapter(messageRequestList, v.getContext(), RefKullanici.getUID(),
                    RefKullanici.getUsername(), RefKullanici.getProfileUrl());
            MessageRequestsRecycleView.setAdapter(messageRequestAdapter);
        }

        MessageRequestsDialog.show();

    }

}