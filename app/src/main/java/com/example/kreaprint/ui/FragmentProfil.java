package com.example.kreaprint.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kreaprint.R;
import com.example.kreaprint.helper.AuthHelper;
import com.example.kreaprint.model.Pesanan;
import com.example.kreaprint.viewmodel.PesananViewModel;
import com.example.kreaprint.LoginActivity;

import java.util.Random;

public class FragmentProfil extends Fragment {
    private Button btnLogout;
    private AuthHelper authHelper; // Tambahkan AuthHelper

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        authHelper = new AuthHelper(requireContext()); // Inisialisasi AuthHelper

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logoutUser());

        testAddPesanan();

        return view;
    }

    private void logoutUser() {
        authHelper.logoutUser();
        Toast.makeText(requireContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show();

        // Arahkan ke LoginActivity setelah logout
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void testAddPesanan() {
        String[] Category = {"Semua", "Banner", "Kartu Nama", "Brosur", "Undangan"};
        String[] Status = {"Sedang Diproses", "Selesai"};
        String[] ImageUrls = {"https://iili.io/33veckg.png", "https://iili.io/33velpa.png"};

        Random rand = new Random();

        PesananViewModel pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);
        Pesanan newPesanan = new Pesanan("Kartu Nama", ImageUrls[rand.nextInt(ImageUrls.length)], Category[rand.nextInt(Category.length)], Status[rand.nextInt(Status.length)]);
        pesananViewModel.tambahPesanan(newPesanan);
    }
}
