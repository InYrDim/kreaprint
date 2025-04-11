package com.example.kreaprint;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kreaprint.components.CustomBackToolbar;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangePassword extends AppCompatActivity {

    private ToastHelper changePasswordToast = new ToastHelper(ChangePassword.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        CustomBackToolbar customBackToolbar = findViewById(R.id.customBackToolbar);
        customBackToolbar.setToolbarTitle("");
        customBackToolbar.showBackButton(true);

        Button savePassword = findViewById(R.id.btn_save_cp);

        savePassword.setOnClickListener(v -> {
            changePasswordToast.showToast("Password Saved");
        });

    }


}