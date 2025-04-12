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
import com.example.kreaprint.helper.firebase.CategoryRepository;
import com.example.kreaprint.helper.firebase.FirestoreCallback;
import com.example.kreaprint.helper.firebase.ProductRepository;
import com.example.kreaprint.model.Category;
import com.example.kreaprint.model.Product;
import com.example.kreaprint.viewmodel.PesananViewModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentBeranda extends Fragment {

    private Spinner categorySpinner;
    private RecyclerView productRecyclerView, hotProductRecyclerView;
    private ProductAdapter productAdapter;
    private HotProductAdapter hotProductAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        initializeViews(view);
        initializeRepositories();
        setupRecyclerViews();

        loadData();

        return view;
    }




    private void initializeViews(View view) {
        categorySpinner = view.findViewById(R.id.dropdown_kategori);
        productRecyclerView = view.findViewById(R.id.rv_products);
        hotProductRecyclerView = view.findViewById(R.id.rv_hot_products);
        ImageView aboutImageView = view.findViewById(R.id.iv_about);

        aboutImageView.setOnClickListener(v -> startActivity(new Intent(getContext(), AboutActivity.class)));
    }

    private void initializeRepositories() {
        productRepository = new ProductRepository();
        categoryRepository = new CategoryRepository();
    }

    private void setupRecyclerViews() {
        // Setup Hot Products
        hotProductAdapter = new HotProductAdapter(new ArrayList<>());
        hotProductAdapter.setOnItemClickListener(this::navigateToProductDetail);
        hotProductRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hotProductRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL));
        hotProductRecyclerView.setAdapter(hotProductAdapter);

        // Setup All Products
        productAdapter = new ProductAdapter(productList);
        productAdapter.setOnItemClickListener(this::navigateToProductDetail);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        productRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        productRecyclerView.setAdapter(productAdapter);
    }

    private void loadData() {
        loadCategories();
        loadMostFavoritedProducts();
        new ViewModelProvider(requireActivity()).get(PesananViewModel.class).hapusPesanan(0);
    }
    private void loadMostFavoritedProducts() {
        productRepository.getAllProduct( new FirestoreCallback<List<Product>>() {

            @Override
            public void onSuccess(List<Product> products) {
                Log.d("FragmentBeranda", "All products: " + products.toString());
                requireActivity().runOnUiThread(() -> {
                    hotProductAdapter.updateList(products);
                    Log.d("FragmentBeranda", "Filtered products: " + products.size());
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void loadAllProducts() {
        productRepository.getAllProduct( new FirestoreCallback<List<Product>>() {

            @Override
            public void onSuccess(List<Product> products) {
                Log.d("FragmentBeranda", "All products: " + products.toString());
                requireActivity().runOnUiThread(() -> {
                    productAdapter.updateList(products);
                    Log.d("FragmentBeranda", "Filtered products: " + products.size());
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void loadCategories() {
        categoryRepository.getAllCategories(new FirestoreCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList = categories;

                for (Category category : categoryList) {
                    Log.d("FragmentBeranda", "Category: " + category.getId());
                }

                setupCategorySpinner();
            }

            @Override
            public void onError(Exception e) {
                Log.e("FragmentBeranda", "Error loading categories", e);
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Semua"); // Default item

        for (Category category : categoryList) {
            spinnerItems.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                spinnerItems
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    loadAllProducts();
                } else {
                    Category selectedCategory = categoryList.get(position - 1);

                    CategoryRepository categoryRepository = new CategoryRepository();
                    DocumentReference categoryRef = categoryRepository.getCategoryRefById(selectedCategory.getId());
                    filterProducts(categoryRef );
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterProducts(DocumentReference categoryRef) {
        productRepository.getProductsByCategory(categoryRef.getId(), new FirestoreCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                Log.d("FragmentBeranda", "Filtered products: " + products.toString());
                requireActivity().runOnUiThread(() -> {
                    productAdapter.updateList(products);
                    Log.d("FragmentBeranda", "Filtered products: " + products.size());
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("FragmentBeranda", "Error filtering products", e);
            }
        });
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("product_id", product.getName());
        startActivity(intent);
    }
}