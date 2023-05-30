package com.cemcoma.myapplication.favourites;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.RecylerviewInterface;

public class bookHolder extends RecyclerView.ViewHolder {
    ImageView bookPhotoView;
    TextView booknameView, authorView;

    public bookHolder(@NonNull View itemView, RecylerviewInterface recylerviewInterface) {
        super(itemView);
        bookPhotoView = itemView.findViewById(R.id.bookimage);
        booknameView = itemView.findViewById(R.id.booknameTextViewBook);
        authorView = itemView.findViewById(R.id.authorTextViewBook);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recylerviewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recylerviewInterface.onListingClick(position);
                    }
                }
            }
        });
    }
}
