package com.example.kreaprint;

import android.annotation.SuppressLint;
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

import com.example.kreaprint.helper.GoogleSignInHelper;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextUsername;

    private static final String TAG = "GoogleSignInHelper";
    private ImageView ivTogglePassword;
    private ImageView googleSignInButton;
    private GoogleSignInHelper googleSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);


        registerWithPassword();

        registerWithGoogle();

        loginInsteadRegister();

    }

    private void loginInsteadRegister() {
        TextView goLogin = findViewById(R.id.href_login);

        goLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
    private void registerWithPassword() {

        editTextEmail = findViewById(R.id.reg_email);
        editTextPassword = findViewById(R.id.reg_password);
        editTextUsername = findViewById(R.id.reg_username);

        Button btnRegister = findViewById(R.id.btn_signup);


        btnRegister.setOnClickListener(v -> {
            if (isValidLogin()) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Login gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerWithGoogle() {
        googleSignInButton = findViewById(R.id.btn_continue_with_google);
        googleSignInHelper = new GoogleSignInHelper(this, getString(R.string.default_web_client_id),  new GoogleSignInHelper.GoogleSignInCallback() {
            @Override
            public void onSignInSuccess(FirebaseUser user) {
                resetGoogleButtonState();
                Toast.makeText(RegisterActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onSignInFailure(Exception e) {
                resetGoogleButtonState();
                if (e instanceof GetCredentialCancellationException) {
                    Toast.makeText(RegisterActivity.this, "Sign-in canceled by user", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void resetGoogleButtonState() {
        runOnUiThread(() -> {
            googleSignInButton.setEnabled(true);
            googleSignInButton.setAlpha(1.0f);
            googleSignInButton.setImageResource(R.drawable.google_logo_android_neutral_rd_ctn);
        });
    }
    private boolean isValidLogin() {



        return true;
    }
}

