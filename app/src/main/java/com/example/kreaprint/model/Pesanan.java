package com.example.kreaprint.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pesanan extends Product implements Parcelable {
    private String statusPesanan;

    public Pesanan(String name, String imageUrl, String kategori, String statusPesanan) {
        super(name, imageUrl, kategori);
        this.statusPesanan = statusPesanan;
    }

    protected Pesanan(Parcel in) {
        super(in.readString(), in.readString(), in.readString());
        statusPesanan = in.readString();
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
        dest.writeString(getNama());
        dest.writeString(getImageUrl());
        dest.writeString(getKategori());
        dest.writeString(statusPesanan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getStatusPesanan() {
        return statusPesanan;
    }

    public void setStatusPesanan(String statusPesanan) {
        this.statusPesanan = statusPesanan;
    }
}
