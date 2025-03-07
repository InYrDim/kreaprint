package com.example.kreaprint.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class BerandaViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> allProducts = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> hotProducts = new MutableLiveData<>();
    private final MutableLiveData<List<String>> categories = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> isDataLoaded = new MediatorLiveData<>();

    private final FirestoreHelper firestoreHelper;

    public LiveData<List<Product>> getAllProducts() { return allProducts; }
    public LiveData<List<Product>> getHotProducts() { return hotProducts; }
    public LiveData<List<String>> getCategories() { return categories; }
    public LiveData<Boolean> isDataLoaded() { return isDataLoaded; }

    public BerandaViewModel() {
        this.firestoreHelper = new FirestoreHelper();

        isDataLoaded.setValue(false); // Default: belum selesai

        isDataLoaded.addSource(allProducts, value -> checkIfAllDataLoaded());
        isDataLoaded.addSource(hotProducts, value -> checkIfAllDataLoaded());
        isDataLoaded.addSource(categories, value -> checkIfAllDataLoaded());

        loadAllData(); // Panggil saat ViewModel dibuat
    }

    public void loadAllData() {
        loadProducts();
        loadHotProducts();
        loadCategories();
    }

    private void checkIfAllDataLoaded() {
        if (allProducts.getValue() != null && hotProducts.getValue() != null && categories.getValue() != null) {
            isDataLoaded.setValue(true); // Semua data sudah selesai dimuat
        }
    }

    private void loadProducts() {
        firestoreHelper.getAllProduct(allProducts::setValue);
    }

    private void loadHotProducts() {
        firestoreHelper.getHotProduct(5, hotProducts::setValue);
    }

    private void loadCategories() {
        firestoreHelper.getAllCategories(categories::setValue);
    }
}
