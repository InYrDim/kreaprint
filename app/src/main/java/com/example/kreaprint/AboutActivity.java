package com.example.kreaprint;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kreaprint.components.CustomBackToolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        CustomBackToolbar customBackToolbar = findViewById(R.id.customBackToolbar);
        customBackToolbar.setToolbarTitle("About");
        customBackToolbar.showBackButton(true);

    }
}