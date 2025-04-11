package com.example.kreaprint.ui;

import static com.example.kreaprint.helper.ImagekitHelper.DEFAULT_PROFILE_FOLDER;

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
import com.example.kreaprint.helper.firebase.FirestoreCallback;
import com.example.kreaprint.helper.firebase.RepositoryFactory;
import com.example.kreaprint.helper.firebase.UserRepository;
import com.example.kreaprint.model.User;
import com.example.kreaprint.utils.FileUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfil extends Fragment {
    private ImageView btnLogout, profileImage;
    private CardView profileImageContainer;
    private ToastHelper profileToast;
    private String userId;

    private static final String TAG = "GoogleSignInHelper";
    private AuthHelper authHelper;
    private View view, rootLayout;

    private TextView tv_name, tv_email;

    Button changePasswordBtn, editProfileBtn;
    ImageButton btn_edit_photo;
    private FilePickerHelper filePickerHelper;
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    handleImageSelection(selectedImageUri);
                }
            });

    private void handleImageSelection(Uri imageUri) {
        filePickerHelper.handleSelectedFile(imageUri, uri -> {
            profileImage.setImageURI(uri);

            // Convert URI to File
            String filePath = FileUtils.getPath(requireContext(), uri);
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    handleProfileImageUpdate(file);

                }
            }
        });
    }


    public void handleProfileImageUpdate(File file) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null || file == null) {
            Log.e("HandleError", "User not logged in or file is null");
            return;
        }

        String userId = firebaseUser.getUid();
        UserRepository userRepo = RepositoryFactory.getUserRepository();

        userRepo.getUserById(userId, new FirestoreCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (user == null) {
                    Log.e("FirestoreError", "User not found");
                    return;
                }

//                Delete Old Image (Existing Image) if Exists
                if( user.getImageUrlId() != null && !user.getImageUrlId().isEmpty()) {
                    String imageUrlId = user.getImageUrlId();
                    deleteImageOnImageKit(imageUrlId);
                }

//              Continue upload image

                Map<String, String> params = new HashMap<>();
                params.put("fileName", file.getName());
                params.put("useUniqueFileName", "true");
                params.put("folder", DEFAULT_PROFILE_FOLDER);

                ImagekitHelper.uploadFile(file,  params, new ImagekitHelper.UploadCallback() {
                    @Override
                    public void onSuccess(ImagekitHelper.UploadResponse response) {
                        Log.d("UploadSuccess", "URL: " + response.url);

                        user.setImageUrl(response.url);
                        user.setImageUrlId(response.fileId);

                        Log.d("UploadSuccess", "IMAGE ID: " + response.fileId);

                        UserRepository userRepo = RepositoryFactory.getUserRepository();
                        userRepo.updateUserImageUrlWithImageId(user.getId(),response.url, response.fileId, new FirestoreCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean result) {
                                Log.d("UpdateSuccess", "User updated with new image");

//                                Update Button
                                btn_edit_photo.setVisibility(View.GONE);

                                updateUiUserInfo();
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("UpdateError", "Error updating user: " + e.getMessage());
                            }
                        });

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("UploadError", error);
                    }
                });


            }
            @Override
            public void onError(Exception e) {
                Log.e("FirestoreError", "Error getting user: " + e.getMessage());
            }
        });

    }

    private void deleteImageOnImageKit(String ImageId) {

        Log.d("ImageDelete", ImageId);

        ImagekitHelper.deleteFile(ImageId, new ImagekitHelper.DeleteCallback() {

            @Override
            public void onSuccess(ImagekitHelper.DeleteResponse response) {
                Log.d("ImageDelete", "Old image deleted");
            }

            @Override
            public void onError(String error) {
                Log.e("ImageDeleteError", "Failed to delete old image: " + error);
            }
        });
    }

    private void setupViews(View view) {
        tv_name = view.findViewById(R.id.tv_displayname);
        tv_email = view.findViewById(R.id.tv_email);

        profileImageContainer = view.findViewById(R.id.card_iv_profile);
        profileImage = view.findViewById(R.id.iv_profile);

        btn_edit_photo = view.findViewById(R.id.btn_edit_photo);

        changePasswordBtn = view.findViewById(R.id.btn_change_password);
        editProfileBtn = view.findViewById(R.id.btn_edit_profile);

        rootLayout = view.findViewById(R.id.root_layout);
    }

    private void setupHelpers() {
        filePickerHelper = new FilePickerHelper(this, profileImage, filePickerLauncher);
    }

    private void setupListeners() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);
        profileToast = new ToastHelper(requireContext());

        setupViews(view);
        setupHelpers();
        setupListeners();

        authHelper = new AuthHelper(requireContext());
        FirebaseUser firebaseUser = authHelper.getCurrentUser();
        userId = null;

        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            Log.d("AuthStatus", "User authenticated: " + authHelper.isLoggedIn());
        } else {
            Log.w("AuthStatus", "No active user session");
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
            UserRepository userRepo = RepositoryFactory.getUserRepository();

            userRepo.getUserById(userId, new FirestoreCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    tv_name.setText(user.getName());
                    tv_email.setText(user.getEmail());

                    try {
                        Log.d("Image", user.getImageUrl());
                        ImageLoaderHelper.loadImage(requireContext(), user.getImageUrl(), profileImage);
                    } catch (Exception e) {
                        Log.d("Image","Setting Image Profile Failed");
                    }
                }

                @Override
                public void onError(Exception e) {

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
            startActivity(intent);
        });
    }

    private void showEditProfileActivity() {
        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
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
