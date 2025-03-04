package com.example.kreaprint.utils;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.kreaprint.model.Product;
import com.example.kreaprint.model.ImageResponse;
import com.example.kreaprint.network.ApiService;
import com.example.kreaprint.network.RetrofitClient;

import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirestoreUploader {
    private static final String API_KEY = "6d207e02198a847aa98d0a2a901485a5"; // Ganti dengan API Key FreeImage.Host
    private FirebaseFirestore db;
    private ApiService apiService;

    public FirestoreUploader() {
        this.db = FirebaseFirestore.getInstance();
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void tambahProdukKeFirestore(List<Product> produkList) {
        for (Product produk : produkList) {
            File fileGambar = new File("/storage/emulated/0/Download/" + produk.getImageUrl());
            uploadImageToFreeImageHost(produk, fileGambar);
        }
    }

    private void uploadImageToFreeImageHost(Product produk, File imageFile) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("source", imageFile.getName(), requestFile);
        RequestBody apiKeyBody = RequestBody.create(MediaType.parse("text/plain"), API_KEY);

        Call<ImageResponse> call = apiService.uploadImage(body, apiKeyBody);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().status_code == 200) {
                    String imageUrl = response.body().image.url;
                    Log.d("UPLOAD", "Gambar berhasil diunggah: " + imageUrl);

                    // Simpan data produk ke Firestore dengan URL gambar
                    simpanProdukKeFirestore(produk, imageUrl);
                } else {
                    Log.e("UPLOAD", "Upload gagal, response tidak valid");
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.e("UPLOAD", "Gagal mengunggah gambar: " + t.getMessage());
            }
        });
    }

    private void simpanProdukKeFirestore(Product produk, String imageUrl) {
        produk.setImageUrl(imageUrl);

        db.collection("products")
                .add(produk)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Produk berhasil disimpan"))
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal menyimpan produk", e));
    }
}
