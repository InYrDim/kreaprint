package com.example.kreaprint.model;

import com.google.firebase.Timestamp;

public class User {
    private String id;
    private String nama;
    private String email;
    private String phone = "";  // Default kosong
    private String alamat = ""; // Default kosong
    private String role = "customer"; // "customer" atau "admin"
    private String imageUrl = "https://i.ibb.co.com/99DSRD4c/default-profile-kreaprint.png", imageUrlId;
    private Timestamp createdAt;

    public User() {

    }

    public User(String id, String nama, String email, String role, Timestamp  createdAt) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String id, String nama, String email, String phone, String alamat, String role, Timestamp  createdAt) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.phone = (phone != null) ? phone : "";   // Jika null, atur ke string kosong
        this.alamat = (alamat != null) ? alamat : ""; // Jika null, atur ke string kosong
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getter dan Setter
    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getImageUrlId() { return this.imageUrlId; }
    public void setImageUrlId(String imageUrlId) { this.imageUrlId = imageUrlId; }

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = (phone != null) ? phone : ""; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = (alamat != null) ? alamat : ""; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp  getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp  createdAt) { this.createdAt = createdAt; }
}
