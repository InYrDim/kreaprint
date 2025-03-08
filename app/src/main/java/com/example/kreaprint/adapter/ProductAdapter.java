package com.example.kreaprint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kreaprint.R;
import com.example.kreaprint.helper.ImageLoaderHelper;
import com.example.kreaprint.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> produkList;
    private HotProductAdapter.OnItemClickListener listener;
    public ProductAdapter(List<Product> produkList) {
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product produk = produkList.get(position);
        holder.tvNama.setText(produk.getNama());

        ImageLoaderHelper.loadImage(
                holder.itemView.getContext(),
                produk.getImageUrl(),
                holder.imgProduct
        );

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.pop_up);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(produk);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(HotProductAdapter.OnItemClickListener listener) {
        this.listener = listener;
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
