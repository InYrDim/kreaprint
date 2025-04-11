package com.example.kreaprint.helper.firebase;

import android.text.TextUtils;
import android.util.Log;

import com.example.kreaprint.model.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class UserRepository extends BaseRepository {
    private static final String COLLECTION = "users";

    public UserRepository() {
        super("UserRepository");
    }

    public void createUser(FirebaseUser firebaseUser, FirestoreCallback<Boolean> callback) {
        User user = new User();
        user.setName(firebaseUser.getDisplayName());
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setImageUrl(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null);
        user.setCreatedAt(Timestamp.now());


        db.collection(COLLECTION).document(user.getId())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    logSuccess("Berhasil Membuat User");
                    callback.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    logError("Buat user", e);
                    callback.onError(e);
                });
    }

    public void getUserById(String userId, FirestoreCallback<User> callback) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onSuccess(user);
                    } else {
                        callback.onError(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil user: " + e.getMessage());
                    callback.onError(e);
                });
    }


    public void updateUserFullName(String nama, String userId, FirestoreCallback<Boolean> callback) {
        db.collection("users").document(userId)
                .update("name", nama)
                .addOnSuccessListener(aVoid -> {
                    logSuccess("Update nama");
                    callback.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    logError("Update nama", e);
                    callback.onError(e);
                });
    }

    public void updateUserImageUrlWithImageId( String userId, String imageUrl, String imageUrlId,  FirestoreCallback<Boolean> callback) {

        if (TextUtils.isEmpty(userId)) {
            callback.onError(new IllegalArgumentException("User ID tidak valid"));
            return;
        }

        if (TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(imageUrlId)) {
            callback.onError(new IllegalArgumentException("URL gambar dan ID gambar harus tersedia"));
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("imageUrl", imageUrl);
        updates.put("imageUrlId", imageUrlId);

        db.collection("users").document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    logSuccess("Update imageUrl");
                    callback.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    logError("Update imageUrl", e);
                    callback.onError(e);
                });
    }

}