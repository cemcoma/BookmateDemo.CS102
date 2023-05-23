package com.cemcoma.myapplication.listings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ldAdapter extends RecyclerView.Adapter<listingHolderLd> {
    private Context context;
    private List<listingLd> list;
    private final RecylerviewInterface recylerviewInterface;

    public ldAdapter(Context context, List<listingLd> list, RecylerviewInterface recylerviewInterface) {
        this.recylerviewInterface = recylerviewInterface;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public listingHolderLd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new listingHolderLd(LayoutInflater.from(context).inflate(R.layout.listing_ld,parent,false), recylerviewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull listingHolderLd holder, int position) {
        holder.booknameView.setText(list.get(position).getBookname());
        holder.authorView.setText("Author: " + list.get(position).getAuthor());
        holder.borrowerView.setText("Borrower: " + list.get(position).getBorrowername());
        double rating = list.get(position).getRating();
        holder.ratingView.setText("Rating: "+rating + " stars");
        Picasso.with(context).load(list.get(position).getImageUrl()).fit().centerCrop().into(holder.bookPhotoView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
