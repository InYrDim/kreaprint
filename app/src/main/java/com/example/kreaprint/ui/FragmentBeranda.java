package com.example.kreaprint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kreaprint.AboutActivity;
import com.example.kreaprint.ProductDetailActivity;
import com.example.kreaprint.R;
import com.example.kreaprint.adapter.HotProductAdapter;
import com.example.kreaprint.adapter.ProductAdapter;
import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.model.Product;
import com.example.kreaprint.viewmodel.PesananViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentBeranda extends Fragment {

    private Spinner categorySpinner;
    private RecyclerView productRecyclerView, hotProductRecyclerView;
    private ProductAdapter productAdapter;
    private HotProductAdapter hotProductAdapter;
    private List<Product> productList, hotProductList, filteredProductList;
    private FirestoreHelper firestoreHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        initializeViews(view);
        initializeData();
        setupEventListeners();
        return view;
    }

    private void initializeViews(View view) {
        categorySpinner = view.findViewById(R.id.dropdown_kategori);
        productRecyclerView = view.findViewById(R.id.rv_products);
        hotProductRecyclerView = view.findViewById(R.id.rv_hot_products);
        ImageView aboutImageView = view.findViewById(R.id.iv_about);

        aboutImageView.setOnClickListener(v -> startActivity(new Intent(getContext(), AboutActivity.class)));
    }

    private void initializeData() {
        firestoreHelper = new FirestoreHelper();
        productList = new ArrayList<>();
        hotProductList = new ArrayList<>();
        filteredProductList = new ArrayList<>();

        setupHotProductRecyclerView();
        setupProductRecyclerView();
        loadHotProducts();
        loadAllProducts();
        loadCategories();

        PesananViewModel pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);
        pesananViewModel.hapusPesanan(0);
    }

    private void setupHotProductRecyclerView() {
        hotProductAdapter = new HotProductAdapter(hotProductList);
        hotProductAdapter.setOnItemClickListener(product -> navigateToProductDetail(product.getNama()));

        hotProductRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hotProductRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        hotProductRecyclerView.setAdapter(hotProductAdapter);
    }

    private void setupProductRecyclerView() {
        productAdapter = new ProductAdapter(filteredProductList);
        productAdapter.setOnItemClickListener(product -> navigateToProductDetail(product.getNama()));

        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        productRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        productRecyclerView.setAdapter(productAdapter);
    }

    private void loadHotProducts() {
        firestoreHelper.getHotProduct(5, products -> {
            if (products.isEmpty()) {
                Log.d("FirestoreData", "Tidak ada produk terlaris ditemukan.");
            }
            hotProductList.clear();
            hotProductList.addAll(products);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> hotProductAdapter.notifyDataSetChanged());
            }
        });
    }

    private void loadAllProducts() {
        firestoreHelper.getAllProduct(products -> {
            if (products.isEmpty()) {
                Log.d("FirestoreData", "Tidak ada produk ditemukan.");
            }
            productList.clear();
            productList.addAll(products);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> productAdapter.notifyDataSetChanged());
            }
        });
    }

    private void loadCategories() {
        firestoreHelper.getAllCategories(categories -> {
            List<String> categoryNames = new ArrayList<>();
            categoryNames.add("Semua");
            categoryNames.addAll(categories);

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryAdapter);
        });
    }

    private void setupEventListeners() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                filterProducts(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterProducts(String category) {
        filteredProductList.clear();
        if (category.equals("Semua")) {
            filteredProductList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getKategori().equals(category)) {
                    filteredProductList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void navigateToProductDetail(String productId) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("product_id", productId);
        startActivity(intent);
    }
}