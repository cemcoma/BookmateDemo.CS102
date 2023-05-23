package com.cemcoma.myapplication.listings;

public class listingLd {
    private String bookname, author,borrowername, ImageString;
    private double rating;



    public listingLd(String bookname, String author, String borrowername, double rating, String ImageString  ) {
        this.bookname = bookname;
        this.author = author;
        this.borrowername = borrowername;
        this.rating = rating;
        this.ImageString = ImageString;
    }
    public String getImageUrl() {
        return ImageString;
    }

    public void setImageString(String ImageString) {
        this.ImageString = ImageString;
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

    public String getBorrowername() {
        return borrowername;
    }

    public void setBorrowername(String borrowername) {
        this.borrowername = borrowername;
    }

    public double getRating() {
        return rating;
    }
}
