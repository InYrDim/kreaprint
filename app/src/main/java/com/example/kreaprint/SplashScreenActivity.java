package com.example.kreaprint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import androidx.lifecycle.ViewModelProvider;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.ImagekitHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.example.kreaprint.model.ImagekitResponse;
import com.example.kreaprint.viewmodel.BerandaViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 seconds delay
    private AuthHelper authHelper;
    private BerandaViewModel berandaViewModel;
    private ToastHelper toastHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        authHelper = new AuthHelper(this);
        toastHelper = new ToastHelper(this);
        berandaViewModel = new ViewModelProvider(this).get(BerandaViewModel.class);

        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, SPLASH_DELAY);
    }

    private void checkLoginStatus() {
        if (!authHelper.isLoggedIn()) {
            toastHelper.showToast("Silakan login terlebih dahulu.", ToastHelper.ToastType.INFO);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String displayName = authHelper.getCurrentUser().getDisplayName();
        toastHelper.showToast("Selamat datang kembali! " + displayName, ToastHelper.ToastType.INFO);

        observeDataLoading();
    }

    private void observeDataLoading() {
        berandaViewModel.isDataLoaded().observe(this, isLoaded -> {
            if (isLoaded) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        berandaViewModel.loadAllData();
    }
}
