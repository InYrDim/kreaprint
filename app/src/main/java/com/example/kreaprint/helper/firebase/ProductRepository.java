package com.example.kreaprint.helper.firebase;


import com.example.kreaprint.model.Product;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ProductRepository extends BaseRepository {
    private static final String COLLECTION = "products";

    public ProductRepository() {
        super("ProductRepository");
    }

    public void batchInsertProducts(List<Product> products, FirestoreCallback<Boolean> callback) {
        // Implementasi batch insert
    }

    public void getPopularProducts(int limit, FirestoreCallback<List<Product>> callback) {
        db.collection(COLLECTION)
                .orderBy("jumlah_order", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Product> products = querySnapshot.toObjects(Product.class);
                    callback.onSuccess(products);
                })
                .addOnFailureListener(e -> callback.onError(e));
    }
}