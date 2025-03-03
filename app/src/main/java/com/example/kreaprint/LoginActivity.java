package com.example.kreaprint;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView goRegister;
    private EditText editTextEmail, editTextPassword;

    private Button btnLogin;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        goRegister = findViewById(R.id.href_register);

        editTextEmail = findViewById(R.id.log_email);
        editTextPassword = findViewById(R.id.log_password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {
            if (isValidLogin()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Login gagal!", Toast.LENGTH_SHORT).show();
            }
        });

        ivTogglePassword = findViewById(R.id.iv_toggle_password);

        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        goRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Sembunyikan password
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.dark_eye_closed);
        } else {
            // Tampilkan password
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.dark_eye_open);
        }
        // Toggle status
        isPasswordVisible = !isPasswordVisible;

        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    private boolean isValidLogin() {
        String email = "admin@email.com";
        String password = "1234";

        return editTextEmail.getText().toString().equals(email) &&
                editTextPassword.getText().toString().equals(password);
    }
}
