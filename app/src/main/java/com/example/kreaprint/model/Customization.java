package com.example.kreaprint.model;

// Menampung kustomisasi untuk layanan yang dipilih
public class Customization {
    private String id;
    private String orderId;
    private String opsi;
    private String warna;
    private String ukuran;

    public Customization() {
        // Konstruktor kosong untuk Firestore
    }

    public Customization(String id, String orderId, String opsi, String warna, String ukuran) {
        this.id = id;
        this.orderId = orderId;
        this.opsi = opsi;
        this.warna = warna;
        this.ukuran = ukuran;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOpsi() { return opsi; }
    public void setOpsi(String opsi) { this.opsi = opsi; }

    public String getWarna() { return warna; }
    public void setWarna(String warna) { this.warna = warna; }

    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }
}
