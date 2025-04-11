package com.example.kreaprint.model;

import com.google.firebase.Timestamp;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone = "";
    private String address = "";
    private String role = "customer";
    private String imageUrl = "https://i.ibb.co.com/99DSRD4c/default-profile-kreaprint.png";

    private String imageUrlId;

    private Timestamp createdAt = Timestamp.now();

    public User() {}

    public String getImageUrl() { return this.imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getImageUrlId() { return this.imageUrlId; }
    public void setImageUrlId(String imageUrlId) { this.imageUrlId = imageUrlId; }

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = (phone != null) ? phone : ""; }

    public String getAlamat() { return address; }
    public void setAlamat(String alamat) { this.address = (alamat != null) ? alamat : ""; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp  getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp  createdAt) { this.createdAt = createdAt; }
}
