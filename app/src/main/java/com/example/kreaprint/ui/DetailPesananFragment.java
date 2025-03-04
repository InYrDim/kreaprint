package com.example.kreaprint.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.kreaprint.R;
import com.example.kreaprint.model.Pesanan;

public class DetailPesananFragment extends Fragment {

    private static final String ARG_PESANAN = "pesanan";
    private Pesanan pesanan;

    public static DetailPesananFragment newInstance(Pesanan pesanan) {
        DetailPesananFragment fragment = new DetailPesananFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PESANAN, pesanan);  // Mengirimkan data menggunakan Parcelable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_pesanan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNama = view.findViewById(R.id.tv_detail_nama);
        TextView tvKategori = view.findViewById(R.id.tv_detail_kategori);
        TextView tvStatus = view.findViewById(R.id.tv_detail_status);
        ImageView ivGambar = view.findViewById(R.id.iv_detail_gambar);

        if (getArguments() != null) {
            pesanan = getArguments().getParcelable(ARG_PESANAN);  // Mengambil data Parcelable

            if (pesanan != null) {
                tvNama.setText(pesanan.getNama());
                tvKategori.setText(pesanan.getKategori());
                tvStatus.setText(pesanan.getStatusPesanan());

                Glide.with(requireContext())
                        .load(pesanan.getImageUrl())
                        .into(ivGambar);
            }
        }
    }
}
