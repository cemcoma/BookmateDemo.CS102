package com.cemcoma.myapplication.listings;

public class UploadMp {
    private String name, url, sellername, author;
    private int price;
    private double rating;



    public UploadMp() {

    }
    public UploadMp(String name, String imageUrl, String sellername, int price, double rating , String author) {
        this.name = name;
        this.url = imageUrl;
        this.sellername = sellername;
        this.author = author;
        this.price = price;
        this.rating = rating;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String  getImageUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getSellername() {return sellername;}
    public void setSellername(String sellername) {this.sellername = sellername;}
    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}
    public double getRating() {return rating;}
    public void setRating(double rating) {this.rating = rating;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
}
