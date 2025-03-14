package com.example.kreaprint;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.GoogleSignInHelper;
import com.example.kreaprint.helper.PasswordSignInHelper;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private ImageView ivTogglePassword, googleSignInButton;

    private Button btnLogin;

    private boolean isPasswordVisible = false;
    private AuthHelper authHelper;
    private static final String TAG = "GoogleSignInHelper";


    private GoogleSignInHelper googleSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authHelper = new AuthHelper(this);

        TextView goRegister = findViewById(R.id.href_register);
        editTextEmail = findViewById(R.id.log_email);
        editTextPassword = findViewById(R.id.log_password);
        btnLogin = findViewById(R.id.btn_login);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

//      GoogleSignIn
        loginWithGoogle();

//      Password Based
        loginWithPassword();

//        Navigasi ke Halaman Registrasi
        goRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

//        Toggle Visibility Password
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void loginWithGoogle() {
        googleSignInButton = findViewById(R.id.btn_continue_with_google);
        googleSignInHelper = new GoogleSignInHelper(this, getString(R.string.default_web_client_id),  new GoogleSignInHelper.GoogleSignInCallback() {
            @Override
            public void onSignInSuccess(FirebaseUser user) {
                resetGoogleButtonState();
                Toast.makeText(LoginActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onSignInFailure(Exception e) {
                resetGoogleButtonState();
                if (e instanceof GetCredentialCancellationException) {
                    Toast.makeText(LoginActivity.this, "Sign-in canceled by user", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSignOut() {
                resetGoogleButtonState();
            }

            @Override
            public void onError(GetCredentialException e) {
                Log.e(TAG, "Couldn't retrieve user's credentials: " + e.getLocalizedMessage());
                resetGoogleButtonState();
            }
        });

        googleSignInButton.setOnClickListener(v -> {
            googleSignInButton.setEnabled(false); // Disable button
            googleSignInButton.setAlpha(0.2f); // Make button appear disabled
            googleSignInButton.setImageResource(R.drawable.google_logo_android_neutral_rd_ctn_disabled); // Change icon (optional)

            googleSignInHelper.signIn();
        });
    }

    private void loginWithPassword() {

        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            PasswordSignInHelper passwordSignInHelper = new PasswordSignInHelper(authHelper);
            passwordSignInHelper.loginWithPassword(email, password, new PasswordSignInHelper.AuthCallback() {
                @Override
                public void onLoginStart() {
                    btnLogin.setEnabled(false);
                    btnLogin.setText(R.string.loading_text);
                }

                @Override
                public void onLoginSuccess(FirebaseUser user) {
                    Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onLoginFailure(Exception e) {
                    Toast.makeText(LoginActivity.this, "Login Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                    btnLogin.setText(R.string.text_login);
                }
            });
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.dark_eye_closed);
        } else {
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.dark_eye_open);
        }
        isPasswordVisible = !isPasswordVisible;
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    private void resetGoogleButtonState() {
        runOnUiThread(() -> {
            googleSignInButton.setEnabled(true);
            googleSignInButton.setAlpha(1.0f);
            googleSignInButton.setImageResource(R.drawable.google_logo_android_neutral_rd_ctn);
        });
    }

}

