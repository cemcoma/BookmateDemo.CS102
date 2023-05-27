package com.cemcoma.myapplication.fragments;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Query mQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_chat, container, false);

       mUser = FirebaseAuth.getInstance().getCurrentUser();
       mFirestore = FirebaseFirestore.getInstance();
       mUserList = new ArrayList<>();

       mRecycleView = v.findViewById(R.id.Users_fragment_recycleView);
       mRecycleView.setHasFixedSize(true);
       mRecycleView.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));

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
               mAdapter = new userAdapter(mUserList, v.getContext());
               mRecycleView.setAdapter(mAdapter);
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


}