package com.example.kreaprint.helper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kreaprint.model.User;
import com.example.kreaprint.utils.FileUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.imagekit.android.entity.UploadResponse;

import java.io.File;

public class FilePickerHelper {
    public static final String TAG = "FilePickerHelper";

    private final Fragment fragment;
    private final ImageView imageView;
    private final ActivityResultLauncher<Intent> filePickerLauncher;
    private final ActivityResultLauncher<String> permissionLauncher;

    public FilePickerHelper(Fragment fragment, ImageView imageView, ActivityResultLauncher<Intent> filePickerLauncher) {
        this.fragment = fragment;
        this.imageView = imageView;
        this.filePickerLauncher = filePickerLauncher;

        // Initialize permission launcher
        this.permissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "Permission granted! Opening file picker...");
                openFilePicker();
            } else {
                Log.e(TAG, "Storage permission denied!");
            }
        });
    }

    // Check and Request Storage Permission
    public void checkStoragePermission() {
        Log.d(TAG, "Checking Storage Permission...");

        if (fragment == null) {
            Log.e(TAG, "Fragment is null, cannot request permission!");
            return;
        }

        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES; // Android 13+
        }

        if (ContextCompat.checkSelfPermission(fragment.requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission NOT granted, requesting now...");
            permissionLauncher.launch(permission);
        } else {
            Log.d(TAG, "Permission already granted, opening file picker...");
            openFilePicker();
        }
    }



    // Open File Picker
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        filePickerLauncher.launch(intent);
    }

    public interface HandleSelectedFileCallback {
        void onFileSelected(Uri imageUri);
    }

    // Handle Selected File
    public void handleSelectedFile(Uri imageUri, HandleSelectedFileCallback callback) {
        if (imageUri != null) {



            callback.onFileSelected(imageUri);

        }
    }



    // Upload Image to ImageKit
    private void uploadImageToImageKit(Uri imageUri) {

//        Get Image Currently
//        Log.d(TAG, "Uploading file: " + imageUri.toString());
//
//        // Get file path
//        String filePath = FileUtils.getPath(fragment.requireContext(), imageUri);
//        if (filePath == null) {
//            Log.e(TAG, "Failed to get file path from URI");
//            return;
//        }
//
//
//        File file = new File(FileUtils.getPath(fragment.requireContext(), imageUri));
//        if (!file.exists()) {
//            Log.e(TAG, "File does not exist: " + file.getAbsolutePath());
//            return;
//        }
//
//        ImagekitHelper.uploadFile(file, new ImagekitHelper.UploadCallback() {
//            @Override
//            public void onSuccess(ImagekitHelper.UploadResponse response) {
//                Log.d("UploadSuccess", "URL: " + response.url);
//
////                Update Database
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null) {
//                    String userId = user.getUid();
//                    String imageUrl = response.url;
//
//                    User currentUser = new User();
//                    currentUser.setId(userId);
//                    currentUser.setImageUrl(imageUrl);
//                    currentUser.setImageUrlId(response.fileId);
//
//                    FirestoreHelper firestoreHelper = new FirestoreHelper();
//                    firestoreHelper.updateUserWithMerge(currentUser, data -> {
//                        Log.d("UpdateSuccess", "User data updated successfully");
//                    });
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.e("UploadError", error);
//            }
//        });
    }
}
