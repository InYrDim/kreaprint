package com.example.kreaprint.network;

import com.example.kreaprint.model.ImageResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("1/upload")
    Call<ImageResponse> uploadImage(
            @Part MultipartBody.Part image,
            @Part("key") RequestBody apiKey
    );
}
