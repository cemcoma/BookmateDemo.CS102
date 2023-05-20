package com.cemcoma.myapplication.listings;

public class Upload {
    private String name, url;

    public Upload() {

    }

    public Upload(String name, String imageUrl) {
        this.name = name;
        this.url = imageUrl;
    }

    public String getName() {return name;}
    public void setName(String name) { this.name = name;}
    public String  getImageUrl() { return url;}
    public void setUrl(String url) { this.url = url;}



}
