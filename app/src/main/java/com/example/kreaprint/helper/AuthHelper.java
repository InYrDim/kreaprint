package com.example.kreaprint.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.credentials.ClearCredentialStateRequest;
import android.credentials.CredentialManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kreaprint.LoginActivity;
import com.example.kreaprint.LoginActivity;
import com.example.kreaprint.ui.FragmentBeranda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthHelper {
    private static final String TAG = "AuthHelper";
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";

    private FirebaseAuth auth;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;


    public AuthHelper(Context context) {
        this.context = context;
        this.auth = FirebaseAuth.getInstance();
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = prefs.edit();
    }


    // 🔹 Cek apakah user sudah login
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) || prefs.getBoolean(KEY_USER_ID, false);
    }


    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }


    public void saveUserSession(String userId) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }


    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }


    public void logoutUser() {
        editor.clear();
        editor.apply();
        auth.signOut();


        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

//
//    public void checkLoginStatus() {
//        FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null && isLoggedIn()) {
//            // User sudah login, masuk ke Dashboard
//            context.startActivity(new Intent(context, FragmentBeranda.class));
//        } else {
//            // User belum login, masuk ke halaman Login
//            context.startActivity(new Intent(context, LoginActivity.class));
//        }
//    }

    public void loginUser(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserSession(user.getUid());
                            callback.onSuccess(user);
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }




    public void registerUser(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserSession(user.getUid());
                            callback.onSuccess(user);
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }


    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
}
