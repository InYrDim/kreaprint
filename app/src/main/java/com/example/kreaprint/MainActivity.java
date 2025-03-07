package com.example.kreaprint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

    private int currentPage = 0;
    private Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;

    private static final long DEBOUNCE_DELAY = 300;

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
            Fragment selectedFragment;
            int newPage = 0;

            if (item.getItemId() == R.id.home) {
                newPage = 0;
                selectedFragment = new FragmentBeranda();
            } else if (item.getItemId() == R.id.order) {
                newPage = 1;
                selectedFragment = new FragmentPesanan();
            } else if (item.getItemId() == R.id.profile) {
                newPage = 2;
                selectedFragment = new FragmentProfil();
            } else {
                selectedFragment = null;
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null && currentFragment.getClass().equals(selectedFragment.getClass())) {
                return false;
            }

            resetIcons();
            if (item.getItemId() == R.id.home) {
                item.setIcon(R.drawable.ic_home_fill);
            } else if (item.getItemId() == R.id.order) {
                item.setIcon(R.drawable.ic_order_fill);
            } else if (item.getItemId() == R.id.profile) {
                item.setIcon(R.drawable.ic_user_fill);
            }

            if (selectedFragment != null && !getSupportFragmentManager().isStateSaved()) {

                debounceHandler.removeCallbacks(debounceRunnable);
                debounceRunnable = () -> {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    );
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                };
                debounceHandler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
            }
            return true;
        });
    }
    private void resetIcons() {
        bottomNavigationView.getMenu().findItem(R.id.home).setIcon(R.drawable.ic_home_line);
        bottomNavigationView.getMenu().findItem(R.id.order).setIcon(R.drawable.ic_order_line);
        bottomNavigationView.getMenu().findItem(R.id.profile).setIcon(R.drawable.ic_user_line);
    }
}
