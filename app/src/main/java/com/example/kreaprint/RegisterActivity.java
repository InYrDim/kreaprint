package com.example.kreaprint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextUsername;
    private TextView goLogin;
    private Button btnRegister;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false; // Default: Password tersembunyi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.reg_email);
        editTextPassword = findViewById(R.id.reg_password);
        editTextUsername = findViewById(R.id.reg_username);

        btnRegister = findViewById(R.id.btn_signup);

        btnRegister.setOnClickListener(v -> {
            if (isValidLogin()) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Login gagal!", Toast.LENGTH_SHORT).show();
            }
        });

        goLogin = findViewById(R.id.href_login);

        goLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private boolean isValidLogin() {
        String email = "admin@email.com";
        String password = "1234";

        return editTextEmail.getText().toString().equals(email) &&
                editTextPassword.getText().toString().equals(password);
    }
}

