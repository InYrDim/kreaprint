package com.example.kreaprint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.helper.ImageLoaderHelper;
import com.example.kreaprint.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;

    private TextView productTitle, productDescription, productTip, productPrice;

    private FirestoreHelper firestoreHelper;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productTitle = findViewById(R.id.tv_product_name);
        productDescription = findViewById(R.id.tv_description);
        productTip = findViewById(R.id.tv_detail_tip);
        productPrice = findViewById(R.id.tv_product_price);
        imgProduct = findViewById(R.id.iv_detail_gambar);

        ImageButton backBtn = findViewById(R.id.btn_back);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        firestoreHelper = new FirestoreHelper();

        productId = getIntent().getStringExtra("product_id");

        if (productId != null) {
            Log.d("Product" , productId);
            getProductDetail(productId);
        } else {
            Toast.makeText(this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getProductDetail(String productId) {

        firestoreHelper.getProductById(productId, new FirestoreHelper.FirestoreCallback<Product>() {
            @Override
            public void onCallback(Product product) {
                if (product != null) {
                    productTitle.setText(product.getNama());
                    productDescription.setText(product.getDeskripsi());
                    productTip.setText(product.getTips());
                    productPrice.setText(String.format("Rp. %s", product.getHarga()));

                    ImageLoaderHelper.loadImage(
                            ProductDetailActivity.this,
                            product.getImageUrl(),
                            imgProduct
                    );
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}