package com.example.kreaprint.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kreaprint.ChangePassword;
import com.example.kreaprint.EditProfile;
import com.example.kreaprint.R;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.FilePickerHelper;
import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.helper.GoogleSignInHelper;
import com.example.kreaprint.helper.ImageLoaderHelper;
import com.example.kreaprint.helper.ImagekitHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.example.kreaprint.LoginActivity;
import com.example.kreaprint.model.User;
import com.example.kreaprint.utils.FileUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class FragmentProfil extends Fragment {
    private ImageView btnLogout, profileImage;
    private CardView profileImageContainer;
    private ToastHelper profileToast;
    private String userId;

    private static final String TAG = "GoogleSignInHelper";
    private AuthHelper authHelper;
    private View view;

    // Declare the TextView fields for user info
    private TextView tv_name, tv_email;

    Button changePasswordBtn, editProfileBtn;

    private FilePickerHelper filePickerHelper;
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();

                    filePickerHelper.handleSelectedFile(selectedImageUri, uri -> {

                        profileImage.setImageURI(uri);

                        Log.d(TAG, "Uploading file: " + uri.toString());

                        // Get file path
                        String filePath = FileUtils.getPath(requireContext(), uri);
                        if (filePath == null) {
                            Log.e(TAG, "Failed to get file path from URI");
                            return;
                        }

                        File file = new File(FileUtils.getPath(requireContext(), uri));
                        if (!file.exists()) {
                            Log.e(TAG, "File does not exist: " + file.getAbsolutePath());
                            return;
                        }

                        handleProfileImageUpdate(file);

                    });
                }
                Log.d("FilePickerHelper", "Image getted");
            });


    public void handleProfileImageUpdate(File file) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null || file == null) {
            Log.e("HandleError", "User not logged in or file is null");
            return;
        }

        String userId = firebaseUser.getUid();
        FirestoreHelper firestoreHelper = new FirestoreHelper();

        firestoreHelper.getUserById(userId, user -> {
            if (user != null) {
                deleteOldImageIfExistsThenUpload(user, file, firestoreHelper);
            } else {
                Log.e("FirestoreError", "User not found");
            }
        });
    }

    private void deleteOldImageIfExistsThenUpload(User user, File file, FirestoreHelper firestoreHelper) {
        String imageUrlId = user.getImageUrlId();

        if (imageUrlId != null && !imageUrlId.isEmpty()) {
            ImagekitHelper.deleteFile(imageUrlId, new ImagekitHelper.DeleteCallback() {
                @Override
                public void onSuccess(ImagekitHelper.DeleteResponse response) {
                    Log.d("ImageDelete", "Old image deleted");
                    uploadNewImageAndUpdateUser(user, file, firestoreHelper);
                }

                @Override
                public void onError(String error) {
                    Log.e("ImageDeleteError", "Failed to delete old image: " + error);
                    uploadNewImageAndUpdateUser(user, file, firestoreHelper); // Still upload
                }
            });
        } else {
            uploadNewImageAndUpdateUser(user, file, firestoreHelper); // No image to delete
        }
    }

    private void uploadNewImageAndUpdateUser(User user, File file, FirestoreHelper firestoreHelper) {
        ImagekitHelper.uploadFile(file, new ImagekitHelper.UploadCallback() {
            @Override
            public void onSuccess(ImagekitHelper.UploadResponse response) {
                Log.d("UploadSuccess", "URL: " + response.url);

                user.setImageUrl(response.url);
                user.setImageUrlId(response.fileId);

                firestoreHelper.updateUserWithMerge(user, result -> {
                    Log.d("UpdateSuccess", "User updated with new image");
                    updateUiUserInfo();
                });
            }

            @Override
            public void onError(String error) {
                Log.e("UploadError", error);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);

        authHelper = new AuthHelper(requireContext());
        profileToast = new ToastHelper(requireContext());

        // Initialize the TextView fields
        tv_name = view.findViewById(R.id.tv_displayname);
        tv_email = view.findViewById(R.id.tv_email);

        profileImageContainer = view.findViewById(R.id.card_iv_profile);
        profileImage = view.findViewById(R.id.iv_profile);

        filePickerHelper = new FilePickerHelper(this, profileImage, filePickerLauncher);

        changePasswordBtn = view.findViewById(R.id.btn_change_password);
        editProfileBtn = view.findViewById(R.id.btn_edit_profile);

        ImageButton btn_edit_photo = view.findViewById(R.id.btn_edit_photo);
        View rootLayout = view.findViewById(R.id.root_layout);

        profileImageContainer.setOnClickListener(v -> btn_edit_photo.setVisibility(View.VISIBLE));

        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_edit_photo.setVisibility(View.GONE);
            }
            return false;
        });

        btn_edit_photo.setOnClickListener(v -> {
            Log.d("ImageKit", "Starting");
            filePickerHelper.checkStoragePermission();
        });

        FirebaseUser firebaseUser = authHelper.getCurrentUser();

        if (authHelper.isLoggedIn()) {
            userId = authHelper.getUserId();
            Log.d("SignIn", String.valueOf(authHelper.isLoggedIn()));
        } else {
            if (firebaseUser != null) {
                userId = firebaseUser.getUid();
            }
        }

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showChangePasswordActivity();
        showEditProfileActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUiUserInfo();
    }

    private void updateUiUserInfo() {
        FirebaseUser firebaseUser = authHelper.getCurrentUser();

        if (firebaseUser != null) {

            FirestoreHelper firestoreHelper = new FirestoreHelper();

            firestoreHelper.getUserById(firebaseUser.getUid(), user -> {
                if (user != null) {
                    tv_name.setText(user.getNama());
                    tv_email.setText(user.getEmail());

                    try {
                        Log.d("Image", user.getImageUrl());
                        ImageLoaderHelper.loadImage(requireContext(), user.getImageUrl(), profileImage);
                    } catch (Exception e) {
                        Log.d("Image","Setting Image Profile Failed");
                    }
                }
            });
        } else {
            tv_name.setText("Unknown User");
            tv_email.setText("-");
        }
    }

    private void showChangePasswordActivity() {
        changePasswordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChangePassword.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    private void showEditProfileActivity() {
        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    private void logoutUser() {
        authHelper.logoutUser();

        GoogleSignInHelper googleSignInHelper = new GoogleSignInHelper(requireContext(), getString(R.string.default_web_client_id), null);

        googleSignInHelper.signOut(() -> {
            if (isAdded() && getContext() != null) { // Ensure fragment is still attached
                profileToast.showToast("Signed out successfully");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                Log.e(TAG, "Fragment is detached. Cannot perform UI updates.");
            }
        });
    }

}
