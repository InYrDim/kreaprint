package com.example.kreaprint.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kreaprint.R;
import com.example.kreaprint.adapter.PesananAdapter;
import com.example.kreaprint.model.Pesanan;
import com.example.kreaprint.viewmodel.PesananViewModel;
import java.util.List;

public class FragmentPesanan extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoOrders;
    private PesananAdapter adapter;
    private PesananViewModel pesananViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesanan, container, false);

        recyclerView = view.findViewById(R.id.recycler_pesanan);
        tvNoOrders = view.findViewById(R.id.tv_no_orders);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi ViewModel
        pesananViewModel = new ViewModelProvider(requireActivity()).get(PesananViewModel.class);

        // Amati perubahan data di ViewModel
        pesananViewModel.getPesananList().observe(getViewLifecycleOwner(), pesananList -> {
            if (pesananList.isEmpty()) {
                tvNoOrders.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvNoOrders.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
//                adapter = new PesananAdapter(getContext(), pesananList);
//                recyclerView.setAdapter(adapter);

                PesananAdapter adapter = new PesananAdapter(getContext(), pesananList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(pesanan -> {
                    DetailPesananFragment detailFragment = DetailPesananFragment.newInstance(pesanan);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, detailFragment)
                            .addToBackStack(null)
                            .commit();
                });


            }
        });

        return view;
    }
}
