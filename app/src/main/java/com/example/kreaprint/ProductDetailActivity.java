package com.example.kreaprint;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kreaprint.helper.FirestoreHelper;
import com.example.kreaprint.helper.ImageLoaderHelper;
import com.example.kreaprint.helper.ToastHelper;
import com.example.kreaprint.helper.firebase.FirestoreCallback;
import com.example.kreaprint.helper.firebase.ProductRepository;
import com.example.kreaprint.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView productTitle, productDescription, productTip, productPrice;

    private FirestoreHelper firestoreHelper;
    private String productId;

    private ToastHelper productDetailToast = new ToastHelper(ProductDetailActivity.this);

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

        Button order = findViewById(R.id.btn_order);
        order.setOnClickListener(v -> {
            productDetailToast.showToast("Ordering Via Whatsapp");

            // WhatsApp order logic
            String phoneNumber = String.valueOf(R.string.default_admin);  //Isi sendiri
            String productName = productTitle.getText().toString();
            String message = "Halo, saya ingin memesan produk: " + productName;

            String url = "https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ProductDetailActivity.this, "WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        firestoreHelper = new FirestoreHelper();
        productId = getIntent().getStringExtra("product_id");

        if (productId != null) {
            Log.d("Product", productId);
            getProductDetail(productId);
        } else {
            Toast.makeText(this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getProductDetail(String productId) {

        ProductRepository productRepository = new ProductRepository();
        productRepository.getProductById(productId, new FirestoreCallback<Product>() {
                @Override
                public void onSuccess(Product product) {
                    productTitle.setText(product.getName());
                    productDescription.setText(product.getDescription());
                    productTip.setText(product.getTips());
                    productPrice.setText(String.format("Rp. %s", product.getPrice()));

                    ImageLoaderHelper.loadImage(
                            ProductDetailActivity.this,
                            product.getImageUrls().get(0),
                            imgProduct
                    );
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(ProductDetailActivity.this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        });
    }
}
