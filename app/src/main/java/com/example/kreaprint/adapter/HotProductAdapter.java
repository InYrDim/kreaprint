package com.example.kreaprint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kreaprint.R;
import com.example.kreaprint.model.Product;

import java.util.List;

public class HotProductAdapter extends RecyclerView.Adapter<HotProductAdapter.ViewHolder> {
    private List<Product> produkList;

    public HotProductAdapter(List<Product> produkList) {
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product produk = produkList.get(position);
        holder.tvNama.setText(produk.getNama());

//        holder.imgProduct.setImageResource(produk.getImageResId());
//        USING GLIDE FOR READING URL:
        Glide.with(holder.itemView.getContext())
                .load(produk.getImageUrl())  // URL dari produk
                .placeholder(R.drawable.bg_promo_text) // Placeholder jika gambar belum dimuat
                .error(R.drawable.layanan_1) // Gambar default jika gagal memuat
                .into(holder.imgProduct); // Masukkan ke ImageView


    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_produk);
            imgProduct = itemView.findViewById(R.id.img_product);
        }
    }
}
