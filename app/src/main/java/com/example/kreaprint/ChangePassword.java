package com.example.kreaprint;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kreaprint.components.CustomBackToolbar;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.example.kreaprint.helper.firebase.FirestoreCallback;
import com.example.kreaprint.helper.firebase.RepositoryFactory;
import com.example.kreaprint.helper.firebase.UserRepository;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ChangePassword extends AppCompatActivity {

    private ImageView ivToggleCurrentPassword;

    private boolean isCurrentPasswordVisible = false;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private ToastHelper changePasswordToast = new ToastHelper(ChangePassword.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        CustomBackToolbar customBackToolbar = findViewById(R.id.customBackToolbar);
        customBackToolbar.setToolbarTitle("");
        customBackToolbar.showBackButton(true);

        Button savePassword = findViewById(R.id.btn_save_cp);

        EditText currentPassword = findViewById(R.id.cp_current_password);
        EditText newPassword = findViewById(R.id.cp_new_password);
        EditText confirmPassword = findViewById(R.id.cp_confirm_password);

        savePassword.setOnClickListener(v -> {

            AuthHelper authHelper = new AuthHelper(this);
            FirebaseUser user = authHelper.getCurrentUser();
            if (user == null) {
                changePasswordToast.showToast("User tidak ditemukan", ToastHelper.ToastType.ERROR);
                finish();
                return;
            }

            String newCurrentPassword = currentPassword.getText().toString().trim();
            String newUserPassword = newPassword.getText().toString().trim();
            String newConfirmPassword = confirmPassword.getText().toString().trim();

            updatePassword(user, newCurrentPassword, newUserPassword, newConfirmPassword, new updateUserInfoCallback() {
                @Override
                public void onFinish() {

                }
            });
            changePasswordToast.showToast("Password Saved");
        });


        ImageView ivToggleCurrent = findViewById(R.id.iv_cp_current_password);
        ImageView ivToggleNew = findViewById(R.id.iv_cp_new_password);
        ImageView ivToggleConfirm = findViewById(R.id.iv_cp_confirm_password);

// Toggle for current password
        ivToggleCurrent.setOnClickListener(v -> {
            isCurrentPasswordVisible = togglePasswordVisibility(currentPassword, ivToggleCurrent, isCurrentPasswordVisible);
        });

// Toggle for new password
        ivToggleNew.setOnClickListener(v -> {
            isNewPasswordVisible = togglePasswordVisibility(newPassword, ivToggleNew, isNewPasswordVisible);
        });

// Toggle for confirm password
        ivToggleConfirm.setOnClickListener(v -> {
            isConfirmPasswordVisible = togglePasswordVisibility(confirmPassword, ivToggleConfirm, isConfirmPasswordVisible);
        });

    }

    private interface updateUserInfoCallback {
        void onFinish();
    }

    private void updatePassword(FirebaseUser user, String newCurrentPassword, String newUserPassword, String newConfirmPassword, ChangePassword.updateUserInfoCallback callback) {

        // Normalize null values to empty strings
        newCurrentPassword = newCurrentPassword == null ? "" : newCurrentPassword;
        newUserPassword = newUserPassword == null ? "" : newUserPassword;
        newConfirmPassword = newConfirmPassword == null ? "" : newConfirmPassword;

        // Check if all fields are empty
        if (newCurrentPassword.isEmpty() && newUserPassword.isEmpty() && newConfirmPassword.isEmpty()) {
            changePasswordToast.showToast("Tidak Ada Perubahan Pada Profil", ToastHelper.ToastType.WARNING);
            finish();
            return;
        }

        // Check if new password and confirmation match
        if (!newUserPassword.equals(newConfirmPassword)) {
            changePasswordToast.showToast("Konfirmasi password tidak cocok", ToastHelper.ToastType.ERROR);
            return;
        }

        // Check if current password is provided
        if (newCurrentPassword.isEmpty()) {
            changePasswordToast.showToast("Password saat ini harus diisi untuk mengubah password", ToastHelper.ToastType.ERROR);
            return;
        }

        // Re-authenticate before updating password
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), newCurrentPassword);
        String finalNewUserPassword = newUserPassword;

        user.reauthenticate(credential).addOnCompleteListener(reAuthTask -> {
            if (reAuthTask.isSuccessful()) {
                // Update password
                user.updatePassword(finalNewUserPassword).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        changePasswordToast.showToast("Profil berhasil diperbarui!", ToastHelper.ToastType.SUCCESS);
                        callback.onFinish();
                        finish();
                    } else {
                        changePasswordToast.showToast("Gagal memperbarui password: " + updateTask.getException().getMessage(), ToastHelper.ToastType.ERROR);
                    }
                });
            } else {
                changePasswordToast.showToast("Autentikasi gagal: " + reAuthTask.getException().getMessage(), ToastHelper.ToastType.ERROR);
            }
        });
    }

    private boolean togglePasswordVisibility(EditText passwordField, ImageView toggleIcon, boolean isVisible) {
        if (isVisible) {
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleIcon.setImageResource(R.drawable.eye_closed);
        } else {
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggleIcon.setImageResource(R.drawable.eye_open);
        }

        // Keep cursor at the end
        passwordField.setSelection(passwordField.getText().length());

        return !isVisible; // return the toggled value
    }

}