package com.cemcoma.myapplication.favourites;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class book {
    private String bookname, author, ImageString, owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public book(String bookname, String author, String ImageString, String owner ) {
        this.bookname = bookname;
        this.author = author;
        this.ImageString = ImageString;
        this.owner = owner;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

    public static void storeBook(String bookname, String author,  String ImageString, String owner) {

            FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

            HashMap<String,Object> mData = new HashMap<>();
            mData.put("bookname", bookname);
            mData.put("author",author);
            mData.put("bookImage","default");
            mData.put("owner", owner);


            mFirestore.collection("books").document(bookname).set(mData);
    }
}

