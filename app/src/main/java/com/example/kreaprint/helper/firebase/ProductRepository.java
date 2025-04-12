package com.example.kreaprint.helper.firebase;


import android.util.Log;

import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.model.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends BaseRepository {
    private static final String COLLECTION = "products";

    public ProductRepository() {
        super("ProductRepository");
    }

    public void batchInsertProducts(List<Product> products, FirestoreCallback<Boolean> callback) {
        // Implementasi batch insert
    }

    public void getProductById(String productId, FirestoreCallback<Product> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document("Neon Box")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        callback.onSuccess(product);
                    } else {
                        callback.onError(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil produk: " + e.getMessage());
                    callback.onError(null);
                });
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
                    callback.onSuccess(produkList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal mengambil semua produk", e));
    }

    public void getProductsByCategory(String categoryRef,
                                      FirestoreCallback<List<Product>> callback) {

        db.collection(COLLECTION)
                .whereEqualTo("categoryId", categoryRef)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> products = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);

                        assert product != null;
                        product.setId(doc.getId());
                        products.add(product);
                    }
                    callback.onSuccess(products);
                });
    }

    public void getMostFavoritedProducts(FirestoreCallback<List<Product>> callback) {
        db.collection("products")
                .whereEqualTo("isActive", true)
                .orderBy("favoriteCount", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> products = querySnapshot.toObjects(Product.class);
                    callback.onSuccess(products);
                });
    }
    public void getBestSellers(FirestoreCallback<List<Product>> callback) {
        db.collection("products")
                .whereEqualTo("isActive", true)
                .orderBy("salesCount", Query.Direction.DESCENDING)
                .limit(10) // Ambil top 10
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> products = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        product.setId(doc.getId());
                        products.add(product);
                    }
                    callback.onSuccess(products);
                });
    }
}