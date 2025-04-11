package com.example.kreaprint.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class ImagekitHelper {

    private static final String TAG = "ImagekitHelper";
    private static final String UPLOAD_BASE_URL = "https://upload.imagekit.io/api/v2/files/";
    private static final String TOKEN_BASE_URL = "https://v0-next-js-token-server.vercel.app/api/";

    public interface UploadCallback {
        void onSuccess(UploadResponse response);
        void onError(String error);
    }

    public interface UploadService {
        @Multipart
        @POST("upload")
        @Headers({
                "Accept: application/json",
                "Authorization: Basic cHJpdmF0ZV9pb28zUEFPRFUzbmtnSVNxc216VTlxa2pMajA9Og=="
        })
        Call<UploadResponse> uploadFile(
                @Part MultipartBody.Part file,
                @Part("fileName") RequestBody fileName,
//                @Part("token") RequestBody token,
                @Part("useUniqueFileName") RequestBody useUniqueFileName
        );
    }

    public interface TokenService {
        @POST("auth")
        @Headers({
                "Accept: application/json",
                "Authorization: Basic cHJpdmF0ZV9pb28zUEFPRFUzbmtnSVNxc216VTlxa2pMajA9Og=="
        })
        Call<AuthTokenResponse> getToken(@Body AuthTokenRequest request);
    }

    public static void uploadFile(File file, UploadCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UPLOAD_BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UploadService uploadService = retrofit.create(UploadService.class);

        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody fileNameBody = RequestBody.create(file.getName(), MediaType.parse("text/plain"));
        RequestBody uniqueFileName = RequestBody.create("true", MediaType.parse("text/plain"));

        Call<UploadResponse> call = uploadService.uploadFile(filePart, fileNameBody, uniqueFileName);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<UploadResponse> call, @NonNull Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onError(response.errorBody() != null ? response.errorBody().string() : "Unknown error");
                    } catch (IOException e) {
                        callback.onError("Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UploadResponse> call, @NonNull Throwable t) {
                callback.onError("Upload failed: " + t.getMessage());
            }
        });
    }

    // The getUploadToken function mimics your JavaScript getToken function.
    private static void getUploadToken(TokenCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TOKEN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TokenService service = retrofit.create(TokenService.class);
        Call<AuthTokenResponse> call = service.getToken(new AuthTokenRequest());

        call.enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthTokenResponse> call, @NonNull Response<AuthTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onTokenReceived(response.body().token);
                } else {
                    callback.onError("Token fetch failed: Code " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthTokenResponse> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // TokenCallback used internally by getUploadToken
    private interface TokenCallback {
        void onTokenReceived(String token);
        void onError(String error);
    }

    // Request payload model (same as JS: { uploadPayload: {}, expire: 60 })
    public static class AuthTokenRequest {
        public Map<String, Object> uploadPayload = new HashMap<>();
        public int expire = 60;
    }

    // Token response model
    public static class AuthTokenResponse {
        public String token;
    }

    // UploadResponse data model — adjust these fields as needed to match the response from ImageKit.
    public static class UploadResponse {
        public String fileId;
        public String name;
        public String url;
        public String thumbnailUrl;
    }
}
