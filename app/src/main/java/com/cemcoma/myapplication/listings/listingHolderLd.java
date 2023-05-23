package com.cemcoma.myapplication.listings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;

public class listingHolderLd extends RecyclerView.ViewHolder {
    ImageView bookPhotoView;
    TextView booknameView, authorView, borrowerView, ratingView;
    public listingHolderLd(@NonNull View itemView, RecylerviewInterface recylerviewInterface) {
        super(itemView);
        bookPhotoView = itemView.findViewById(R.id.imageListingLd);
        booknameView = itemView.findViewById(R.id.booknameTextViewLd);
        authorView = itemView.findViewById(R.id.authorTextViewLd);
        borrowerView = itemView.findViewById(R.id.borrowerTextView);
        ratingView = itemView.findViewById(R.id.borrowerRatingTextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recylerviewInterface != null) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION) {
                        recylerviewInterface.onListingClick(position);
                    }
                }
            }
        });
    }

}
