package com.example.kreaprint.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kreaprint.ui.FragmentBeranda;
import com.example.kreaprint.ui.FragmentPesanan;
import com.example.kreaprint.ui.FragmentProfil;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentBeranda(); // Beranda (paling kiri)
            case 1:
                return new FragmentPesanan(); // Pesanan (tengah)
            case 2:
                return new FragmentProfil(); // Profil (paling kanan)
            default:
                return new FragmentBeranda();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Jumlah tab
    }
}
