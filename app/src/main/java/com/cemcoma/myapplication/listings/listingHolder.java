package com.cemcoma.myapplication.listings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;

public class listingHolder extends RecyclerView.ViewHolder {
    ImageView bookPhotoView;
     TextView booknameView, authorView, sellerView, ratingView, priceView;
    public listingHolder(@NonNull View itemView, RecylerviewInterface recylerviewInterface) {
        super(itemView);
        bookPhotoView = itemView.findViewById(R.id.imageListingMp);
        booknameView = itemView.findViewById(R.id.booknameTextView);
        authorView = itemView.findViewById(R.id.authorTextView);
        priceView = itemView.findViewById(R.id.priceTextView);
        sellerView = itemView.findViewById(R.id.sellerTextView);
        ratingView = itemView.findViewById(R.id.sellerRatingTextView);

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
