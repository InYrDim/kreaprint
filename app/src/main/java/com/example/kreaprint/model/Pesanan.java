package com.example.kreaprint.model;

public class Pesanan extends Product {
    private String statusPesanan;

    public Pesanan(String nama, String imageUrl, String kategori, String statusPesanan) {
        super(nama, imageUrl, kategori);
        this.statusPesanan = statusPesanan;
    }

    public String getStatusPesanan() {
        return statusPesanan;
    }

    public void setStatusPesanan(String statusPesanan) {
        this.statusPesanan = statusPesanan;
    }
}
