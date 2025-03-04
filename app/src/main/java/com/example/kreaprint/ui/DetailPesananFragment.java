package com.example.kreaprint.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.kreaprint.R;

public class DetailPesananFragment extends Fragment {
    private TextView tvNama, tvKategori, tvStatus;
    private ImageView ivProduk;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pesanan, container, false);

        tvNama = view.findViewById(R.id.tv_detail_nama);
        tvKategori = view.findViewById(R.id.tv_detail_kategori);
        tvStatus = view.findViewById(R.id.tv_detail_status);
        ivProduk = view.findViewById(R.id.iv_detail_produk);

        // Ambil data dari bundle
        if (getArguments() != null) {
            tvNama.setText(getArguments().getString("nama"));
            tvKategori.setText(getArguments().getString("kategori"));
            tvStatus.setText(getArguments().getString("statusPesanan"));
            String imageUrl = getArguments().getString("imageUrl");

            // Load image dengan Glide
            Glide.with(requireContext()).load(imageUrl).into(ivProduk);
        }

        return view;
    }
}
