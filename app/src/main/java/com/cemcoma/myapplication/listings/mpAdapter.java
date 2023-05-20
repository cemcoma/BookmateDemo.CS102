package com.cemcoma.myapplication.listings;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class mpAdapter extends RecyclerView.Adapter<listingHolder> {
    private Context context;
    private List<listingMp> list;
    private final RecylerviewInterface recylerviewInterface;

    public mpAdapter(Context context, List<listingMp> list, RecylerviewInterface recylerviewInterface) {
        this.recylerviewInterface = recylerviewInterface;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public listingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new listingHolder(LayoutInflater.from(context).inflate(R.layout.listing_mp,parent,false), recylerviewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull listingHolder holder, int position) {
        holder.booknameView.setText(list.get(position).getBookname());
        holder.authorView.setText("Author: " + list.get(position).getAuthor());
        holder.priceView.setText("Price: TRY " + list.get(position).getPrice());
        holder.sellerView.setText("Seller: " + list.get(position).getSellername());
        double rating = list.get(position).getRating();
        holder.ratingView.setText("Rating: "+rating + " stars");
        Picasso.with(context).load(list.get(position).getImageUrl()).fit().centerCrop().into(holder.bookPhotoView);
        // ekleme yapıcam ve fotolar da gözükecek :)
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
