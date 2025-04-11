package com.example.kreaprint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.kreaprint.ui.FragmentBeranda;
import com.example.kreaprint.ui.FragmentProfil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private final Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;
    private static final long DEBOUNCE_DELAY = 300;
    private int currentFragmentId = -1;

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.main_bottom_nav);
        frameLayout = findViewById(R.id.fragment_container);

        fragmentMap.put(R.id.home, new FragmentBeranda());
        fragmentMap.put(R.id.profile, new FragmentProfil());

        if (savedInstanceState == null) {
            loadFragment(R.id.home, false);
            currentFragmentId = R.id.home;
            bottomNavigationView.setSelectedItemId(R.id.home);
            updateActiveIcon(R.id.home);
        } else {
            currentFragmentId = savedInstanceState.getInt("CURRENT_FRAGMENT_ID", R.id.home);
            bottomNavigationView.setSelectedItemId(currentFragmentId);
            updateActiveIcon(currentFragmentId);
        }

        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == currentFragmentId) {
            return false;
        }

        debounceHandler.removeCallbacks(debounceRunnable);
        debounceRunnable = () -> loadFragment(itemId, true);
        debounceHandler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);

        return true;
    }

    private void loadFragment(int fragmentId, boolean useAnimation) {
        Fragment fragment = fragmentMap.get(fragmentId);
        if (fragment != null && !getSupportFragmentManager().isStateSaved()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (useAnimation) {
                setFragmentTransactionAnimation(transaction, fragmentId);
            }

            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            currentFragmentId = fragmentId;
            updateActiveIcon(fragmentId);
        }
    }

    private void setFragmentTransactionAnimation(FragmentTransaction transaction, int newFragmentId) {
        if (newFragmentId > currentFragmentId) {
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
        } else if (newFragmentId < currentFragmentId) {
            transaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        }
    }

    private void updateActiveIcon(int itemId) {
        bottomNavigationView.getMenu().findItem(R.id.home).setIcon(itemId == R.id.home ? R.drawable.ic_home_fill : R.drawable.ic_home_line);
        bottomNavigationView.getMenu().findItem(R.id.profile).setIcon(itemId == R.id.profile ? R.drawable.ic_user_fill : R.drawable.ic_user_line);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_FRAGMENT_ID", currentFragmentId);
    }
}