package com.example.kreaprint.helper;

import com.google.firebase.auth.FirebaseUser;

public class PasswordSignInHelper {
    private final AuthHelper authHelper;

    public PasswordSignInHelper(AuthHelper authHelper) {
        this.authHelper = authHelper;
    }

    public void loginWithPassword(String email, String password, AuthCallback callback) {
        if (email.isEmpty() || password.isEmpty()) {
            callback.onLoginFailure(new IllegalArgumentException("Email dan Password harus diisi!"));
            return;
        }

        callback.onLoginStart();

        authHelper.loginUser(email, password, new AuthHelper.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                callback.onLoginSuccess(user);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onLoginFailure(e);
            }
        });
    }

    public interface AuthCallback {
        void onLoginStart();
        void onLoginSuccess(FirebaseUser user);
        void onLoginFailure(Exception e);
    }
}
