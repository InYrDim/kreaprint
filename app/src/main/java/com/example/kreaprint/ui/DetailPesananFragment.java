package com.example.kreaprint.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.kreaprint.R;
import com.example.kreaprint.adapter.ExpandableListViewAdapter;
import com.example.kreaprint.components.CustomBackToolbar;
import com.example.kreaprint.helper.ImageLoaderHelper;
import com.example.kreaprint.model.Pesanan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailPesananFragment extends Fragment {

    private static final String ARG_PESANAN = "pesanan";
    private Pesanan pesanan;
    private CustomBackToolbar customBackToolbar;

    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter adapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    public static DetailPesananFragment newInstance(Pesanan pesanan) {
        DetailPesananFragment fragment = new DetailPesananFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PESANAN, pesanan);  // Mengirimkan data menggunakan Parcelable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_pesanan, container, false);

        customBackToolbar = view.findViewById(R.id.orderDetailProductToolbar);
        customBackToolbar.showBackButton(true);
        customBackToolbar.setToolbarTitle("Pesanan");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvKategori = view.findViewById(R.id.tv_detail_kategori);
        TextView tvStatus = view.findViewById(R.id.tv_detail_status);
        ImageView ivGambar = view.findViewById(R.id.iv_detail_gambar);
        Button orders_btn = view.findViewById(R.id.orders_btn);

        if (getArguments() != null) {
            pesanan = getArguments().getParcelable(ARG_PESANAN);  // Mengambil data Parcelable

            if (pesanan != null) {
                customBackToolbar.setToolbarTitle(pesanan.getProdukNama());
                tvKategori.setText(pesanan.getKategori());
                Log.d("Pesanan" , String.valueOf(Objects.equals(pesanan.getStatusPesanan(), "Selesai")));

                if (Objects.equals(pesanan.getStatusPesanan(), "Selesai")) {
                    orders_btn.setText("Selesai");
                    orders_btn.setBackgroundColor(getResources().getColor(R.color.shade_primary_color));
                }

                tvStatus.setText(pesanan.getStatusPesanan());

                ImageLoaderHelper.loadImage(requireContext(), pesanan.getProdukImageUrl(), ivGambar);

            }
        }
    }

}
