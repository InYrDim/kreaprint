package com.example.kreaprint.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kreaprint.R;
import com.example.kreaprint.model.Pesanan;
import com.example.kreaprint.viewmodel.PesananViewModel;

import java.util.Random;

public class FragmentProfil extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        testAddPesanan();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
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