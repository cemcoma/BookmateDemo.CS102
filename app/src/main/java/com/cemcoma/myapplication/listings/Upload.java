package com.cemcoma.myapplication.listings;

public class Upload {
    private String name, url, sellername;
    private int price;
    private double rating;



    public Upload() {

    }
    public Upload(String name, String imageUrl, String sellername, int price, double rating) {
        this.name = name;
        this.url = imageUrl;
        this.sellername = sellername;
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
}
