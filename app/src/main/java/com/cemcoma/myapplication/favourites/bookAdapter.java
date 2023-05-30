package com.cemcoma.myapplication.favourites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class bookAdapter extends RecyclerView.Adapter<bookHolder> {
    private Context context;
    private List<book> list;
    private final RecylerviewInterface recylerviewInterface;

    public bookAdapter(Context context, List<book> list, RecylerviewInterface recylerviewInterface) {
        this.recylerviewInterface = recylerviewInterface;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public bookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new bookHolder(LayoutInflater.from(context).inflate(R.layout.book,parent,false), recylerviewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull bookHolder holder, int position) {
        holder.booknameView.setText(list.get(position).getBookname());
        holder.authorView.setText("Author: " + list.get(position).getAuthor());
        Picasso.with(context).load(list.get(position).getImageString()).fit().centerCrop().into(holder.bookPhotoView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}