package com.example.kreaprint.model;

public class Product {
    private String nama;
    private String kategori;
    private String imageUrl;
    private int jumlahOrder; // Menyimpan jumlah pesanan

    public Product() {
        // Constructor kosong diperlukan oleh Firestore
    }

    public Product(String name, String imageUrl, String kategori) {
        this.nama = name;
        this.imageUrl = imageUrl;
        this.kategori = kategori;
        this.jumlahOrder = 0; // Default jumlah order 0
    }

    public Product(String name, String imageUrl, String kategori, int jumlahOrder) {
        this.nama = name;
        this.imageUrl = imageUrl;
        this.kategori = kategori;
        this.jumlahOrder = jumlahOrder; // Bisa diatur jika ingin memberikan nilai awal
    }

    public String getNama() {
        return nama;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getKategori() {
        return kategori;
    }

    public int getJumlahOrder() {
        return jumlahOrder;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setJumlahOrder(int jumlahOrder) {
        this.jumlahOrder = jumlahOrder;
    }
}
