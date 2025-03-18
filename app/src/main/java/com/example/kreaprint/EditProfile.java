package com.example.kreaprint;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.kreaprint.components.CustomBackToolbar;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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


        String userId = getIntent().getStringExtra("USER_ID");
        Log.d("USER_ID", userId);

        Button btnUpdate = findViewById(R.id.btn_ep);

        TextView editTextNewDisplayName = findViewById(R.id.tv_ep_fullname);
        TextView editTextNewEmail = findViewById(R.id.tv_ep_email);

        AuthHelper authHelper = new AuthHelper(this);
        FirebaseUser user = authHelper.getCurrentUser();

        editTextNewDisplayName.setText(user.getDisplayName());
        editTextNewEmail.setText(user.getEmail());

        btnUpdate.setOnClickListener(v -> {
            String newEmail = editTextNewEmail.getText().toString().trim();
            String newDisplayName = editTextNewDisplayName.getText().toString().trim();


            if (!newEmail.isEmpty() && !newDisplayName.isEmpty()) {
                updateUserInfo(user, newEmail, newDisplayName);
            } else {
                epToast.showToast("Email dan nama tidak boleh kosong!", ToastHelper.ToastType.WARNING);
            }
        });

    }

    private void updateUserInfo(FirebaseUser user, String newEmail, String newDisplayName) {
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
                user.updateEmail(newEmail).addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful()) {
                        epToast.showToast("Profil berhasil diperbarui!", ToastHelper.ToastType.SUCCESS);
                        finish();
                    } else {
                        epToast.showToast("Gagal memperbarui email: " + emailTask.getException().getMessage(), ToastHelper.ToastType.ERROR);
                    }
                });
            } else {
                epToast.showToast("Gagal memperbarui profil: " + task.getException().getMessage(), ToastHelper.ToastType.ERROR);
            }
        });
    }



}