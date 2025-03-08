package com.example.kreaprint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.kreaprint.helper.AuthHelper;

import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.model.Product;
import com.example.kreaprint.viewmodel.BerandaViewModel;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.kreaprint.R;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.MainActivity;
import com.example.kreaprint.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    private AuthHelper authHelper;
    private BerandaViewModel berandaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        addData();

        authHelper = new AuthHelper(this);
        berandaViewModel = new ViewModelProvider(this).get(BerandaViewModel.class);

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        FirebaseUser user = authHelper.getCurrentUser();

        if (user == null) {
            // Jika user belum login, pindah ke LoginActivity
            Toast.makeText(this, "Silakan login terlebih dahulu.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Jika user sudah login, mulai proses loading data
        Toast.makeText(this, "Selamat datang kembali!", Toast.LENGTH_SHORT).show();
        observeDataLoading();
    }

    private void observeDataLoading() {
        berandaViewModel.isDataLoaded().observe(this, isLoaded -> {
            if (isLoaded) {
                // Setelah semua data selesai dimuat, pindah ke MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        // Mulai load data setelah memastikan user login
        berandaViewModel.loadAllData();
    }

    private void addData() {
        FirestoreHelper helper = new FirestoreHelper();
        List productList = new ArrayList();
        productList.add(new Product("Stiker glossy atau doff", "Stiker & Cutting", "", 0, "", 0, ""));

        helper.addProduct(productList);
    }
}

