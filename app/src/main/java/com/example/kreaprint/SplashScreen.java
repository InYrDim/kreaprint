package com.example.kreaprint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_DELAY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen_activity);

        new Handler().postDelayed(() -> startActivity(new Intent(SplashScreen.this, MainActivity.class)), SPLASH_SCREEN_DELAY);
    }
}
