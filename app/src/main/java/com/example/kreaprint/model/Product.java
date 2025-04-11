package com.example.kreaprint.model;

public class Product {

    private String id;

    private String nama;
    private String kategori;
    private String imageUrl;

    private int jumlahOrder; // Menyimpan jumlah pesanan
    private String deskripsi;
    private int harga;
    private String tips;

    public Product() {

    }
    public Product(String id, String nama, String kategori, String imageUrl, int jumlahOrder, String deskripsi, int harga, String tips) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.imageUrl = imageUrl;
        this.jumlahOrder = jumlahOrder;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.tips = tips;
    }


    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setJumlahOrder(int jumlahOrder) {
        this.jumlahOrder = jumlahOrder;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getKategori() {
        return kategori;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getJumlahOrder() {
        return jumlahOrder;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getHarga() {
        return harga;
    }
    public String  getTips(){
        return tips;
    }
}
