package com.example.kreaprint.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.kreaprint.R;
import com.example.kreaprint.adapter.HotProductAdapter;
import com.example.kreaprint.adapter.ProductAdapter;
import com.example.kreaprint.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import com.example.kreaprint.viewmodel.PesananViewModel;

public class FragmentBeranda extends Fragment {
    private Spinner dropdownKategori;
    private RecyclerView recyclerView, hotRecyclerView;
    private ProductAdapter adapter;
    private HotProductAdapter hotProductAdapter;
    private List<Product> produkList, hotProdukList, filteredList;
    private String[] kategoriArray = {"Semua", "Banner", "Kartu Nama", "Brosur", "Undangan"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PesananViewModel pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);
        pesananViewModel.hapusPesanan(0);

        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        dropdownKategori = view.findViewById(R.id.dropdown_kategori);
        recyclerView = view.findViewById(R.id.rv_products);
        hotRecyclerView = view.findViewById(R.id.rv_hot_products);

//        produkList = new ArrayList<>();
//        produkList.add(new Product("Banner", "https://i.ibb.co.com/Fq01cVGj/layanan-1.png", "Banner"));

//      ManualWay
//        produkList.add(new Product("Kartu Nama", R.drawable.layanan_1, "Kartu Nama"));
//        produkList.add(new Product("Brosur", R.drawable.layanan_1, "Brosur"));
//        produkList.add(new Product("Undangan", R.drawable.layanan_1, "Undangan"));
//        produkList.add(new Product("Undangan", R.drawable.layanan_1, "Undangan"));
//        produkList.add(new Product("Undangan", R.drawable.layanan_1, "Undangan"));
//        produkList.add(new Product("Brosur", R.drawable.layanan_1, "Brosur"));

//      ManualWay
//        hotProdukList.add(new Product("Kartu Nama", R.drawable.layanan_1, "Kartu Nama"));
//        hotProdukList.add(new Product("Brosur", R.drawable.layanan_1, "Brosur"));


        hotProdukList = new ArrayList<>();
        hotProdukList.add(new Product("Banner", "https://iili.io/33veckg.png", "Banner"));
        hotProdukList.add(new Product("Undangan", "https://iili.io/33velpa.png", "Undangan"));

        hotProductAdapter = new HotProductAdapter(hotProdukList);

//        Try Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        produkList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(filteredList);

// Atur adapter ke RecyclerView sebelum data di-load
        recyclerView.setAdapter(adapter);

        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    produkList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String nama = document.getString("nama");
                        String imageUrl = document.getString("imageUrl");
                        String kategori = document.getString("kategori");

                        Product product = new Product(nama, imageUrl, kategori);
                        produkList.add(product);
                    }

                    // Setelah data Firestore diambil, update filteredList
                    filteredList.clear();
                    filteredList.addAll(produkList);

                    // Pastikan adapter diberi tahu perubahan data
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal mengambil data", e));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(adapter);

        hotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hotRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        hotRecyclerView.setAdapter(hotProductAdapter);

        adapter.notifyItemInserted(produkList.size() - 1);

        ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, kategoriArray);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownKategori.setAdapter(adapterKategori);

        dropdownKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedKategori = kategoriArray[position];
                filterProduk(selectedKategori);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void filterProduk(String kategori) {
        filteredList.clear();

        if (kategori.equals("Semua")) {
            filteredList.addAll(produkList);
        } else {
            for (Product produk : produkList) {
                if (produk.getKategori().equals(kategori)) {
                    filteredList.add(produk);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }


}