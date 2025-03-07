package com.example.kreaprint.model;

public class User {
    private String userId;
    private String email;
    private String username;

    // Constructor kosong diperlukan untuk Firestore
    public User() {}

    public User(String userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    // Getter dan Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
