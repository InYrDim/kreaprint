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
import java.util.UUID;

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

        // Buat objek Pesanan dengan semua atribut
        Pesanan newPesanan = new Pesanan(
                id, userId, produkId, produkNama, produkImageUrl, kategori,
                jumlah, totalHarga, statusPesanan, tanggalPemesanan, metodePembayaran
        );

        // Tambahkan pesanan ke ViewModel
        PesananViewModel pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);
        pesananViewModel.tambahPesanan(newPesanan);
    }

}
