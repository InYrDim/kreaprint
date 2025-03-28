package com.example.kreaprint.helper;

import android.util.Log;

import com.example.kreaprint.model.Product;
import com.example.kreaprint.model.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String TAG = "FirestoreHelper";

    public void addUser(User user, FirestoreCallback<Boolean> callback) {
        db.collection("users").document(user.getId())
                .set(user)
                .addOnSuccessListener(aVoid -> callback.onCallback(true))
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal menyimpan user", e);
                    callback.onCallback(false);
                });
    }
    public void saveUserToFirestore(FirebaseUser user) {
        DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {

                Log.d(TAG, "User already exists in Firestore");
            } else {

                User newUser = new User();
                newUser.setId(user.getUid());
                newUser.setNama(user.getDisplayName());
                newUser.setEmail(user.getEmail());
                newUser.setCreatedAt(Timestamp.now());

                userRef.set(newUser)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "User added to Firestore"))
                        .addOnFailureListener(e -> Log.e(TAG, "Error adding user to Firestore", e));
            }
        });
    }

    public void getUserById(String userId, FirestoreCallback<User> callback) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onCallback(user);
                    } else {
                        callback.onCallback(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil user: " + e.getMessage());
                    callback.onCallback(null);
                });
    }


    public void addProduct(List<Product> produkList) {
        for (Product produk : produkList) {
            Map<String, Object> produkMap = new HashMap<>();
            produkMap.put("nama", produk.getNama());
            produkMap.put("imageUrl", produk.getImageUrl());
            produkMap.put("kategori", produk.getKategori());
            produkMap.put("jumlah_order", produk.getJumlahOrder());
            produkMap.put("deskripsi", produk.getDeskripsi());
            produkMap.put("tips", produk.getTips());
            produkMap.put("harga", produk.getHarga());

            db.collection("products")
                    .document(produk.getNama())
                    .set(produkMap);
        }
    }

    public void addCategory(List<String> kategoriList) {
        for (String kategori : kategoriList) {
            Map<String, Object> kategoriMap = new HashMap<>();
            kategoriMap.put("nama", kategori);

            db.collection("categories")
                    .document(kategori)
                    .set(kategoriMap);
        }
    }

    public void addOrder(String kategori, String produkId, int jumlah) {
        // Data pesanan
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("produk_id", produkId);
        orderData.put("kategori", kategori);
        orderData.put("jumlah", jumlah);
        orderData.put("timestamp", System.currentTimeMillis());

        // Tambahkan order ke koleksi 'orders'
        db.collection("orders").add(orderData).addOnSuccessListener(documentReference -> {
            // Update jumlah_order pada produk terkait
            DocumentReference produkRef = db.collection("layanan").document(kategori)
                    .collection("produk").document(produkId);

            produkRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Long jumlahOrderSaatIni = documentSnapshot.getLong("jumlah_order");
                    if (jumlahOrderSaatIni == null) jumlahOrderSaatIni = 0L;
                    produkRef.update("jumlah_order", jumlahOrderSaatIni + jumlah);
                }
            });
        });
    }

    public interface FirestoreCallback<T> {
        void onCallback(T data);
    }

    public void getHotProduct(int limit, FirestoreCallback<List<Product>> callback) {
        db.collection("products")
                .orderBy("jumlah_order", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> produkList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            Product product = document.toObject(Product.class);
                            produkList.add(product);
                        }
                    }
                    callback.onCallback(produkList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal mengambil produk terlaris", e));
    }
    public void getProdukByCategory(String kategori, FirestoreCallback<List<Product>> callback) {
        db.collection("products")
                .whereEqualTo("kategori", kategori) // Filter berdasarkan kategori
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> produkList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            Product produk = document.toObject(Product.class);
                            produkList.add(produk);
                        }
                    }
                    callback.onCallback(produkList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal mengambil produk kategori: " + kategori, e));
    }

    public void getAllProduct(FirestoreCallback<List<Product>> callback) {
        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> produkList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            Product produk = document.toObject(Product.class);
                            produkList.add(produk);
                        }
                    }
                    callback.onCallback(produkList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal mengambil semua produk", e));
    }

    public interface CategoryCallback {
        void onCallback(List<String> kategoriList);
    }

    public void getAllCategories(CategoryCallback callback) {
        db.collection("categories").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> kategoriList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            kategoriList.add(document.getId()); // Nama dokumen = kategori
                            Log.d("FirestoreID", "Kategori yang diambil dari Firestore: " + document.getId());
                        }
                        callback.onCallback(kategoriList);
                    }
                });
    }

    public void getProductById(String productId, FirestoreCallback<Product> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        callback.onCallback(product);
                    } else {
                        callback.onCallback(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil produk: " + e.getMessage());
                    callback.onCallback(null);
                });
    }

}

