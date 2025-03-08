package com.example.kreaprint.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pesanan implements Parcelable {
    private String id;
    private String userId;
    private String produkId;
    private String produkNama;
    private String produkImageUrl;
    private String kategori;
    private int jumlah;
    private double totalHarga;
    private String statusPesanan;
    private long tanggalPemesanan;
    private String metodePembayaran;

    public Pesanan() {

        // Konstruktor kosong untuk Firestore
    }

    public Pesanan(String id, String userId, String produkId, String produkNama, String produkImageUrl, String kategori,
                   int jumlah, double totalHarga, String statusPesanan, long tanggalPemesanan, String metodePembayaran) {
        this.id = id;
        this.userId = userId;
        this.produkId = produkId;
        this.produkNama = produkNama;
        this.produkImageUrl = produkImageUrl;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.totalHarga = totalHarga;
        this.statusPesanan = statusPesanan;
        this.tanggalPemesanan = tanggalPemesanan;
        this.metodePembayaran = metodePembayaran;
    }

    protected Pesanan(Parcel in) {
        id = in.readString();
        userId = in.readString();
        produkId = in.readString();
        produkNama = in.readString();
        produkImageUrl = in.readString();
        kategori = in.readString();
        jumlah = in.readInt();
        totalHarga = in.readDouble();
        statusPesanan = in.readString();
        tanggalPemesanan = in.readLong();
        metodePembayaran = in.readString();
    }

    public static final Creator<Pesanan> CREATOR = new Creator<Pesanan>() {
        @Override
        public Pesanan createFromParcel(Parcel in) {
            return new Pesanan(in);
        }

        @Override
        public Pesanan[] newArray(int size) {
            return new Pesanan[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(produkId);
        dest.writeString(produkNama);
        dest.writeString(produkImageUrl);
        dest.writeString(kategori);
        dest.writeInt(jumlah);
        dest.writeDouble(totalHarga);
        dest.writeString(statusPesanan);
        dest.writeLong(tanggalPemesanan);
        dest.writeString(metodePembayaran);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProdukId() { return produkId; }
    public void setProdukId(String produkId) { this.produkId = produkId; }

    public String getProdukNama() { return produkNama; }
    public void setProdukNama(String produkNama) { this.produkNama = produkNama; }

    public String getProdukImageUrl() { return produkImageUrl; }
    public void setProdukImageUrl(String produkImageUrl) { this.produkImageUrl = produkImageUrl; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public String getStatusPesanan() { return statusPesanan; }
    public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }

    public long getTanggalPemesanan() { return tanggalPemesanan; }
    public void setTanggalPemesanan(long tanggalPemesanan) { this.tanggalPemesanan = tanggalPemesanan; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
}
