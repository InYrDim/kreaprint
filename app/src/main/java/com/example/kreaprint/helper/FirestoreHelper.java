package com.example.kreaprint.helper;

import android.util.Log;

import com.example.kreaprint.model.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void tambahUserKeFirestore(String userId, String email, String username, FirestoreCallback<Boolean> callback) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("username", username);

        db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> callback.onCallback(true))
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal menyimpan user", e);
                    callback.onCallback(false);
                });
    }


    // 🔹 Menambahkan data produk awal ke Firestore
    // 🔹 Method dinamis untuk menambahkan produk ke Firestore

    public void tambahProdukKeFirestore(List<Product> produkList) {
        for (Product produk : produkList) {
            Map<String, Object> produkMap = new HashMap<>();
            produkMap.put("nama", produk.getNama());
            produkMap.put("imageUrl", produk.getImageUrl());
            produkMap.put("kategori", produk.getKategori());
            produkMap.put("jumlah_order", 0);

            db.collection("products")
                    .document(produk.getNama())
                    .set(produkMap);
        }
    }

    public void tambahKategoriKeFirestore(List<String> kategoriList) {
        for (String kategori : kategoriList) {
            Map<String, Object> kategoriMap = new HashMap<>();
            kategoriMap.put("nama", kategori);

            db.collection("categories")
                    .document(kategori)
                    .set(kategoriMap);
        }
    }





    // 🔹 Menambahkan pesanan dan update jumlah order produk
    public void tambahPesanan(String kategori, String produkId, int jumlah) {
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

    // 🔹 Mengambil produk dengan order terbanyak
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

    // 🔹 Mengambil semua kategori dari database
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
}

