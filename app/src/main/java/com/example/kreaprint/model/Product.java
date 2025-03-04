package com.example.kreaprint.model;


public class Product {
    private String nama;
    private String kategori;

    private String imageUrl;


//    Example When Test
//    public Product(String nama, int imageResId, String kategori) {
//        this.nama = nama;
//        this.imageResId = imageResId;
//        this.kategori = kategori;
//    }

    public Product(String name, String imageUrl, String kategori) {
        this.nama = name;
        this.imageUrl = imageUrl;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

//    public int getImageResId() {
//        return imageResId;
//    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getKategori() {
        return kategori;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

