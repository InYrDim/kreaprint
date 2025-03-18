package com.example.kreaprint;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kreaprint.components.CustomBackToolbar;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        CustomBackToolbar customBackToolbar = findViewById(R.id.customBackToolbar);
        customBackToolbar.setToolbarTitle("About");
        customBackToolbar.showBackButton(true);
    }
}