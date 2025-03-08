package com.example.kreaprint.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.kreaprint.ProductDetailActivity;
import com.example.kreaprint.R;
import com.example.kreaprint.adapter.HotProductAdapter;
import com.example.kreaprint.adapter.ProductAdapter;
import com.example.kreaprint.helper.FirestoreHelper;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PesananViewModel pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);
        pesananViewModel.hapusPesanan(0);

        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        dropdownKategori = view.findViewById(R.id.dropdown_kategori);
        recyclerView = view.findViewById(R.id.rv_products);
        hotRecyclerView = view.findViewById(R.id.rv_hot_products);

        FirestoreHelper firestoreHelper = new FirestoreHelper();

        hotProdukList = new ArrayList<>();

        hotProductAdapter = new HotProductAdapter(hotProdukList);

        hotProductAdapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.getNama()); // Kirim ID produk ke activity detail
            startActivity(intent);
        });

        hotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hotRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        hotRecyclerView.setAdapter(hotProductAdapter);

        firestoreHelper.getHotProduct(5, new FirestoreHelper.FirestoreCallback<List<Product>>() {
            @Override
            public void onCallback(List<Product> produkList) {
                if (produkList.isEmpty()) {
                    Log.d("FirestoreData", "Tidak ada produk terlaris ditemukan.");
                }

                hotProdukList.clear();
                hotProdukList.addAll(produkList);

                requireActivity().runOnUiThread(() -> hotProductAdapter.notifyDataSetChanged());
            }
        });
//------------------------------------------
        produkList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(filteredList);

        adapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.getNama()); // Kirim ID produk ke activity detail
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(adapter);

        firestoreHelper.getAllProduct(new FirestoreHelper.FirestoreCallback<List<Product>>() {
            @Override
            public void onCallback(List<Product> products) {
                if (products.isEmpty()) {
                    Log.d("FirestoreData", "Tidak ada produk terlaris ditemukan.");
                }

                produkList.clear();
                produkList.addAll(products);

                requireActivity().runOnUiThread(() -> hotProductAdapter.notifyDataSetChanged());
            }
        });

        adapter.notifyItemInserted(produkList.size() - 1);

        firestoreHelper.getAllCategories(kategoriList -> {
            // Logging kategoriList untuk memastikan data masuk
            Log.d("Firestore", "Kategori dari Firestore: " + kategoriList);

            List<String> kategoriNamaList = new ArrayList<>();
            kategoriNamaList.add("Semua");

            kategoriNamaList.addAll(kategoriList);

            ArrayAdapter<String> adapterKategori = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, kategoriNamaList);
            adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dropdownKategori.setAdapter(adapterKategori);

            dropdownKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedKategori = kategoriNamaList.get(position);
                    Log.d("Firestore", "Kategori dipilih: " + selectedKategori);
                    filterProduk(selectedKategori);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
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