package com.example.kreaprint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.kreaprint.R;
import com.example.kreaprint.model.Pesanan;
import java.util.List;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {
    private Context context;
    private List<Pesanan> pesananList;
    private OnItemClickListener listener;
    public PesananAdapter(Context context, List<Pesanan> pesananList) {
        this.context = context;
        this.pesananList = pesananList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pesanan pesanan = pesananList.get(position);
        holder.tvNama.setText(pesanan.getNama());
        holder.tvKategori.setText(pesanan.getKategori());
        holder.tvStatusPesanan.setText(pesanan.getStatusPesanan());

        // Load gambar menggunakan Glide
        Glide.with(context)
                .load(pesanan.getImageUrl())
                .into(holder.ivProduct);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.pop_up);

        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pesanan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pesananList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Pesanan pesanan);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvKategori, tvStatusPesanan;
        ImageView ivProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_produk);
            tvKategori = itemView.findViewById(R.id.tv_kategori_produk);
            tvStatusPesanan = itemView.findViewById(R.id.tv_status_pesanan);
            ivProduct = itemView.findViewById(R.id.iv_produk);
        }
    }
}
