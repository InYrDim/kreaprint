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
import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.helper.GoogleSignInHelper;
import com.example.kreaprint.helper.PasswordSignInHelper;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextDisplayname;

    private boolean isPasswordVisible = false;
    private static final String TAG = "GoogleSignInHelper";
    private ImageView ivTogglePassword;
    private ImageView googleSignInButton;

    private AuthHelper authHelper;
    private GoogleSignInHelper googleSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);

        authHelper = new AuthHelper(this);


        editTextEmail = findViewById(R.id.reg_email);
        editTextPassword = findViewById(R.id.reg_password);
        editTextDisplayname = findViewById(R.id.reg_username);
        ivTogglePassword = findViewById(R.id.iv_reg_toggle_password);

        registerWithPassword();

        registerWithGoogle();

        loginInsteadRegister();

        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
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

        Button btnRegister = findViewById(R.id.btn_signup);
        PasswordSignInHelper passwordSignInHelper = new PasswordSignInHelper(authHelper);

        btnRegister.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String displayName = editTextDisplayname.getText().toString().trim();

            passwordSignInHelper.registerWithPassword(email, password, displayName, new PasswordSignInHelper.AuthCallback() {
                @Override
                public void onLoginStart() {
                    btnRegister.setEnabled(false);
                    Toast.makeText(RegisterActivity.this, "Mendaftarkan...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoginSuccess(FirebaseUser user) {
                    FirestoreHelper firestoreHelper = new FirestoreHelper();
                    firestoreHelper.saveUserToFirestore(user);

                    Toast.makeText(RegisterActivity.this, "Registrasi berhasil! Selamat datang, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onLoginFailure(Exception e) {
                    btnRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.eye_closed);
        } else {
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.eye_open);
        }
        isPasswordVisible = !isPasswordVisible;
        editTextPassword.setSelection(editTextPassword.getText().length());
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
}

