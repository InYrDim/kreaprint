package com.example.kreaprint.model;

public class User {
    private String id;
    private String nama;
    private String email;
    private String phone = "";  // Default kosong
    private String alamat = ""; // Default kosong
    private String role = "customer"; // "customer" atau "admin"
    private long createdAt;

    public User() {
        // Konstruktor kosong untuk Firestore
    }

    public User(String id, String nama, String email, String role, long createdAt) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String id, String nama, String email, String phone, String alamat, String role, long createdAt) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.phone = (phone != null) ? phone : "";   // Jika null, atur ke string kosong
        this.alamat = (alamat != null) ? alamat : ""; // Jika null, atur ke string kosong
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getter dan Setter
    public String getId() { return id; }
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

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
