package com.example.kreaprint;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.kreaprint.model.Product;
import com.example.kreaprint.ui.FragmentBeranda;
import com.example.kreaprint.ui.FragmentPesanan;
import com.example.kreaprint.ui.FragmentProfil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.main_bottom_nav);
        frameLayout = findViewById(R.id.fragment_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentBeranda())
                    .commit();

            bottomNavigationView.getMenu().findItem(R.id.home).setIcon(R.drawable.ic_home_fill);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            resetIcons();

            if (item.getItemId() == R.id.home) {
                selectedFragment = new FragmentBeranda();
                item.setIcon(R.drawable.ic_home_fill);
            } else if (item.getItemId() == R.id.order) {
                selectedFragment = new FragmentPesanan();
                item.setIcon(R.drawable.ic_order_fill);
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new FragmentProfil();
                item.setIcon(R.drawable.ic_user_fill);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

//        Test Db
//        tambahProdukKeFirestore();
    }

    private void resetIcons() {
        bottomNavigationView.getMenu().findItem(R.id.home).setIcon(R.drawable.ic_home_line);
        bottomNavigationView.getMenu().findItem(R.id.order).setIcon(R.drawable.ic_order_line);
        bottomNavigationView.getMenu().findItem(R.id.profile).setIcon(R.drawable.ic_user_line);
    }

//    For Testing Only
    private void tambahProdukKeFirestore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Product> produkList = new ArrayList<>();
        produkList.add(new Product("Banner", "https://iili.io/33veckg.png", "Banner"));


        for (Product produk : produkList) {
            db.collection("products")
                    .add(produk);
        }
    }


}
