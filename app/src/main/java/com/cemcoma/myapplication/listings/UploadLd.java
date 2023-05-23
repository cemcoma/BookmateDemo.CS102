package com.cemcoma.myapplication.listings;

public class UploadLd {
    private String name, url, borrowername, author;

    private double rating;



    public UploadLd() {

    }
    public UploadLd(String name, String imageUrl, String sellername, double rating , String author) {
        this.name = name;
        this.url = imageUrl;
        this.borrowername = sellername;
        this.author = author;
        this.rating = rating;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String  getImageUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getBorrowername() {return borrowername;}
    public void setBorrowername(String borrowername) {this.borrowername = borrowername;}

    public double getRating() {return rating;}
    public void setRating(double rating) {this.rating = rating;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
}
