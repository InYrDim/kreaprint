package com.example.kreaprint.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.kreaprint.model.Pesanan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPesananFragment extends Fragment {

    private static final String ARG_PESANAN = "pesanan";
    private Pesanan pesanan;
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

        expandableListView = view.findViewById(R.id.expandableListView);
        prepareListData();

        adapter = new ExpandableListViewAdapter(requireContext(), listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);

        return view;
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
                tvNama.setText(pesanan.getProdukNama());
                tvKategori.setText(pesanan.getKategori());
                tvStatus.setText(pesanan.getStatusPesanan());

                Glide.with(requireContext())
                        .load(pesanan.getProdukNama())
                        .into(ivGambar);
            }
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Tambahkan kategori
        listDataHeader.add("Karakteristik");
        listDataHeader.add("Penggunaan");

        // Tambahkan data produk dalam kategori
        List<String> karakteristik = new ArrayList<>();
        karakteristik.add("Ketebalan dan Kekuatan: Cukup tebal dan kuat, cocok untuk penggunaan indoor maupun outdoor.\n" +
                "Ketahanan terhadap Cuaca: Tahan air dan sinar UV, sehingga tidak mudah luntur atau rusak.\n" +
                "Permukaan Cetak: Halus, sehingga mampu menghasilkan cetakan dengan warna yang tajam dan detail tinggi.\n" +
                "Fleksibilitas: Mudah dipotong, dilipat, atau diaplikasikan ke berbagai media.\n");

        List<String> penggunaan = new ArrayList<>();
        penggunaan.add("Banner dan Spanduk: Digunakan untuk promosi, event, atau branding.\n" +
                "Stiker dan Label: Bisa digunakan untuk stiker custom, label produk, atau dekorasi.\n" +
                "Backdrop dan Display: Cocok untuk latar belakang pameran atau booth.\n" +
                "Dekorasi Kendaraan: Dapat digunakan untuk wrapping mobil atau motor.\n");


        // Masukkan ke HashMap
        listDataChild.put(listDataHeader.get(0), karakteristik);
        listDataChild.put(listDataHeader.get(1), penggunaan);
    }
}
