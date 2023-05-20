package com.cemcoma.myapplication.listings;

public class listingMp {
    private String bookname, author, sellername;
    private double rating;
    private int price, imageInt;



    public listingMp(String bookname, String author, String sellername, int price, double rating, int imageInt  ) {
        this.bookname = bookname;
        this.author = author;
        this.price = price;
        this.sellername = sellername;
        this.rating = rating;
        this.imageInt = imageInt;
    }
    public int getImageInt() {
        return imageInt;
    }

    public void setImageInt(int imageInt) {
        this.imageInt = imageInt;
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

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
