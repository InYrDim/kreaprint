package com.example.kreaprint.helper;

import android.content.Context;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kreaprint.R;

public class ImageLoaderHelper {

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.bg_promo_text) // Placeholder jika gambar belum dimuat
                .error(R.drawable.layanan_1) // Gambar default jika gagal memuat
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Menggunakan cache untuk efisiensi
                .into(imageView);
    }

    public static void loadImage(Context context, String imageUrl, ImageView imageView, @DrawableRes int placeholder, @DrawableRes int errorImage) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(placeholder) // Placeholder custom
                .error(errorImage) // Gambar error custom
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
