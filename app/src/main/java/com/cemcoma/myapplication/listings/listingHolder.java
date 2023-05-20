package com.cemcoma.myapplication.listings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;

public class listingHolder extends RecyclerView.ViewHolder {
    ImageView bookPhotoView;
     TextView booknameView, authorView, sellerView, ratingView, priceView;
    public listingHolder(@NonNull View itemView) {
        super(itemView);
        bookPhotoView = itemView.findViewById(R.id.imageListingMp);
        booknameView = itemView.findViewById(R.id.booknameTextView);
        authorView = itemView.findViewById(R.id.authorTextView);
        priceView = itemView.findViewById(R.id.priceTextView);
        sellerView = itemView.findViewById(R.id.sellerTextView);
        ratingView = itemView.findViewById(R.id.sellerRatingTextView);
    }
}
