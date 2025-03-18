package com.example.kreaprint.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kreaprint.ChangePassword;
import com.example.kreaprint.EditProfile;
import com.example.kreaprint.R;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.helper.GoogleSignInHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.example.kreaprint.model.Pesanan;
import com.example.kreaprint.viewmodel.PesananViewModel;
import com.example.kreaprint.LoginActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.UUID;

public class FragmentProfil extends Fragment {
    private ImageView btnLogout, profileImage;

    private CardView profileImageContainer;

    private ToastHelper profileToast;
    private String userId;

    private static final String TAG = "GoogleSignInHelper";
    private AuthHelper authHelper;

    private View view;


    Button changePasswordBtn, editProfileBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);

        authHelper = new AuthHelper(requireContext());
        profileToast = new ToastHelper(requireContext());

        TextView tv_name = view.findViewById(R.id.tv_displayname);
        TextView tv_email = view.findViewById(R.id.tv_email);

        profileImageContainer = view.findViewById(R.id.card_iv_profile);
        profileImage = view.findViewById(R.id.iv_profile);

        changePasswordBtn = view.findViewById(R.id.btn_change_password);
        editProfileBtn = view.findViewById(R.id.btn_edit_profile);

        FirebaseUser firebaseUser = authHelper.getCurrentUser();

        if(authHelper.isLoggedIn()) {
            userId = authHelper.getUserId();
            Log.d("SignIn", String.valueOf(authHelper.isLoggedIn()));
        } {
            userId = authHelper.getCurrentUser().getUid();

        }


        String displayName = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        userId = firebaseUser.getUid();

        if (displayName.isEmpty()) {
            displayName = "User" + System.currentTimeMillis();
        }

        tv_name.setText(displayName);
        tv_email.setText(email);

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logoutUser());

        testAddPesanan();

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showChangePasswordActivity();
        showEditProfileActivity();
    }
    private void showOrders() {
        
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
            Intent intent = new Intent(getContext(),EditProfile.class);
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

    private void testAddPesanan() {
        String[] Category = {"Semua", "Banner", "Kartu Nama", "Brosur", "Undangan"};
        String[] Status = {"Sedang Diproses", "Selesai"};
        String[] ImageUrls = {"https://iili.io/33veckg.png", "https://iili.io/33velpa.png"};
        String[] MetodePembayaran = {"Transfer Bank", "E-Wallet", "COD"};

        Random rand = new Random();

        // Simulasi ID dan Data Dummy
        String id = UUID.randomUUID().toString(); // Buat ID unik
        String userId = "user123"; // Simulasi user ID
        String produkId = "produk123"; // Simulasi produk ID
        String produkNama = "Kartu Nama";
        String produkImageUrl = ImageUrls[rand.nextInt(ImageUrls.length)];
        String kategori = Category[rand.nextInt(Category.length)];
        int jumlah = rand.nextInt(10) + 1; // Jumlah acak 1-10
        double totalHarga = jumlah * 5000; // Simulasi harga per item
        String statusPesanan = Status[rand.nextInt(Status.length)];
        long tanggalPemesanan = System.currentTimeMillis(); // Timestamp saat ini
        String metodePembayaran = MetodePembayaran[rand.nextInt(MetodePembayaran.length)];

        Pesanan newPesanan = new Pesanan(
                id, userId, produkId, produkNama, produkImageUrl, kategori,
                jumlah, totalHarga, statusPesanan, tanggalPemesanan, metodePembayaran
        );

        PesananViewModel pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);
        pesananViewModel.tambahPesanan(newPesanan);
    }

}
