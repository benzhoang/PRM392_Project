package com.example.prm392_project.ui.activities.product_detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.R;
import com.example.prm392_project.adapter.product.ProductAdapter;
import com.example.prm392_project.adapter.viewpagger2.ImageProductAdapter;
import com.example.prm392_project.data.model.main.cart.CartRequest;
import com.example.prm392_project.data.model.main.cart.ItemCartRequest;
import com.example.prm392_project.databinding.ActivityProductDetailBinding;
import com.example.prm392_project.util.SharedPrefUtils;
import com.maxkeppeler.sheets.input.InputSheet;
import com.maxkeppeler.sheets.input.type.InputRadioButtons;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private ProductDetailViewModel viewModel;

    private String userId;
    private double price;
    private String productImage;
    private String categoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edge-to-edge (nếu cần)
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                v.setPadding(
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                        insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                );
                return insets;
            });
        }

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        binding.backButton.setOnClickListener(v -> onBackPressed());

        String id = getIntent().getStringExtra("id");
        String token = getIntent().getStringExtra("token");
        userId = SharedPrefUtils.getString(this, "id", "");

        binding.selectSizeBtn.setOnClickListener(v -> {

        });

        binding.addToBagBtn.setOnClickListener(v -> {

        });

        viewModel.getProductDetail( id, product -> {
            runOnUiThread(() -> {
                if (product != null) {
                    double price = product.getPrice(); // hoặc nếu price là String thì: Double.parseDouble(product.getPrice())
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    String priceVN = formatter.format(price) + "VNĐ";
                    binding.productNameTv.setText(product.getName());
                    binding.productPriceTv.setText( priceVN);
                    binding.productCategoryTv.setText(product.getCategoryId());
                    binding.productDsTv.setText(product.getDescription());
                    binding.productNameMainTv.setText(product.getName());
                    List<String> images = new ArrayList<>();
                    images.add(product.getImageUrl());
                    binding.imageViewpager.setAdapter(new ImageProductAdapter(images));

                    binding.indicator.setViewPager2(binding.imageViewpager);

                    price = product.getPrice();
                    productImage = product.getImageUrl();
                    categoryId = product.getCategoryId();
                    if (false) {
                        binding.addToBagBtn.setEnabled(false);
                        binding.addToBagBtn.setText("Out of stock");
                    }
                    // Load related products
                    viewModel.getProductsByCategory(token, categoryId, relatedProducts -> {
                        runOnUiThread(() -> {
                            if (relatedProducts != null) {
                                ProductAdapter adapter = new ProductAdapter();
                                adapter.differ.submitList(relatedProducts); // Nếu bạn port ProductAdapter sang Java, sửa lại cho phù hợp
                                adapter.setOnItemClickListener(product1 -> {
                                    Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("id", product1.getId());
                                    intent.putExtra("token", token);
                                    intent.putExtra("categoryId", product1.getCategoryId());
                                    startActivity(intent);
                                });
                                binding.relatedProductsRv.setAdapter(adapter);
                            }
                        });
                    });
                }
            });
        });
    }
}
