package com.example.kreaprint;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.kreaprint.components.CustomBackToolbar;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.example.kreaprint.helper.firebase.FirestoreCallback;
import com.example.kreaprint.helper.firebase.RepositoryFactory;
import com.example.kreaprint.helper.firebase.UserRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EditProfile extends AppCompatActivity {

    private ToastHelper epToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        CustomBackToolbar customBackToolbar = findViewById(R.id.customBackToolbar);
        customBackToolbar.setToolbarTitle("");
        customBackToolbar.showBackButton(true);

        epToast = new ToastHelper(this);

        AuthHelper authHelper = new AuthHelper(this);
        FirebaseUser user = authHelper.getCurrentUser();
        if (user == null) {
            epToast.showToast("User tidak ditemukan", ToastHelper.ToastType.ERROR);
            finish();
            return;
        }

        String userId = user.getUid();
        Button btnUpdate = findViewById(R.id.btn_ep);

        TextView editTextNewDisplayName = findViewById(R.id.tv_ep_fullname);
        TextView editTextNewEmail = findViewById(R.id.tv_ep_email);

        editTextNewDisplayName.setText(user.getDisplayName());
        editTextNewEmail.setText(user.getEmail());

        btnUpdate.setOnClickListener(v -> {
            String newEmail = editTextNewEmail.getText().toString().trim();
            String newDisplayName = editTextNewDisplayName.getText().toString().trim();

            if (!newEmail.isEmpty() && !newDisplayName.isEmpty()) {
                btnUpdate.setEnabled(false);
                btnUpdate.setText(R.string.loading_text);

                updateUserInfo(user, newEmail, newDisplayName, () -> {
                    btnUpdate.setEnabled(true);
                    btnUpdate.setText(R.string.btn_text_ep);
                });
            } else {
                epToast.showToast("Email dan nama tidak boleh kosong!", ToastHelper.ToastType.WARNING);
            }
        });

    }

    private interface updateUserInfoCallback {
        void onFinish();
    }

    private void updateUserInfo(FirebaseUser user, String newEmail, String newDisplayName, updateUserInfoCallback callback) {
        if (newDisplayName.equals(user.getDisplayName()) && newEmail.equals(user.getEmail())) {
            epToast.showToast("Tidak Ada Perubahan Pada Profil", ToastHelper.ToastType.WARNING);
            finish();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                UserRepository userRepo = RepositoryFactory.getUserRepository();
                userRepo.updateUserFullName(newDisplayName, user.getUid(), new FirestoreCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        // Handle success
                        epToast.showToast("Profil berhasil diperbarui!", ToastHelper.ToastType.SUCCESS);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error
                    }
                });

                user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful()) {
                        epToast.showToast("Profil berhasil diperbarui! Silahkan cek email untuk verifikasi", ToastHelper.ToastType.SUCCESS);
                        finish();
                    } else {
                        epToast.showToast("Gagal memperbarui email: " + emailTask.getException().getMessage(), ToastHelper.ToastType.ERROR);
                    }
                }).addOnFailureListener(e -> {
                    epToast.showToast("Gagal memperbarui email: " + e.getMessage(), ToastHelper.ToastType.ERROR);
                });

                callback.onFinish();
            } else {
                epToast.showToast("Gagal memperbarui profil: " + task.getException().getMessage(), ToastHelper.ToastType.ERROR);
                callback.onFinish();
            }
        });
    }



}