package com.example.kreaprint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.reg_email);
        editTextPassword = findViewById(R.id.reg_password);
        btnLogin = findViewById(R.id.btn_signup);

        btnLogin.setOnClickListener(v -> {

            if (isValidLogin()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Login gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidLogin() {
        String email = "admin@email.com";
        String password = "1234";

        return editTextEmail.getText().toString().equals(email) &&
                editTextPassword.getText().toString().equals(password);
    }
}

