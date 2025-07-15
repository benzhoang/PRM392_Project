package com.example.prm392_project.ui.activities.product_detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        int id = getIntent().getIntExtra("id", -1);
        Log.d("ProductDetailActivity", "Received id: " + id);
        String token = getIntent().getStringExtra("token");
        userId = SharedPrefUtils.getString(this, "userId", "");



        binding.addToBagBtn.setOnClickListener(v -> {
            int newQuantity = 1;
            try {
                // Đọc danh sách cart hiện tại
                String cartJson = SharedPrefUtils.getString(this, "cart_items", "[]");
                if (cartJson == null || cartJson.trim().isEmpty()) cartJson = "[]";
                org.json.JSONArray array = new org.json.JSONArray(cartJson);

                boolean existed = false;
                for (int i = 0; i < array.length(); i++) {
                    org.json.JSONObject obj = array.getJSONObject(i);
                    if (obj.optInt("product_id", -1) == id) {
                        newQuantity = obj.optInt("quantity", 0) + 1;
                        obj.put("quantity", newQuantity);
                        array.put(i, obj);
                        existed = true;
                        break;
                    }
                }
                if (!existed) {
                    org.json.JSONObject obj = new org.json.JSONObject();
                    obj.put("product_id", id);
                    obj.put("quantity", 1);
                    array.put(obj);
                    newQuantity = 1;
                }

                SharedPrefUtils.saveString(this, "cart_items", array.toString());

                // Hiển thị popup thông báo
                PopupDialog.getInstance(this)
                        .setStyle(Styles.SUCCESS)
                        .setHeading("Đã thêm vào giỏ hàng!")
                        .setDescription("Số lượng hiện tại của sản phẩm này: " + newQuantity)
                        .setDismissButtonText("Ok")
                        .setCancelable(false)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                dialog.dismiss();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Có lỗi khi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });


        viewModel.getProductDetail( String.valueOf(id), product -> {
            runOnUiThread(() -> {
                Log.d("ProductDetailActivity", "product: " + (product == null ? "null" : product.getName()));
                if (product != null) {
                    // Lấy giá và định dạng
                    double price = product.getPrice(); // Nếu getPrice trả về double
                    // Nếu getPrice trả về String thì: double price = Double.parseDouble(product.getPrice());
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    String priceVN = formatter.format(price) + " VNĐ";

                    // Set tên sản phẩm
                    binding.productNameTv.setText(product.getName());
                    binding.productNameMainTv.setText(product.getName());

                    // Set giá
                    binding.productPriceTv.setText(priceVN);

                    // Set nahnx (category)
                    binding.productCategoryTv.setText("Nhãn hiệu: " + String.valueOf(product.getBrand()));

                    // Set mô tả
                    binding.productDsTv.setText(product.getDescription());

                    // Set số lượng tồn kho
                    int stock = product.getStockQuantity();
                    binding.productStockTv.setText("Kho: " + stock);

                    // Set hình ảnh sản phẩm
                    List<String> images = new ArrayList<>();
                    images.add(product.getImageUrl());
                    binding.imageViewpager.setAdapter(new ImageProductAdapter(images));
                    binding.indicator.setViewPager2(binding.imageViewpager);

                    // Disable nút nếu hết hàng
                    if (stock == 0) {
                        binding.addToBagBtn.setEnabled(false);
                        binding.addToBagBtn.setText("Hết hàng");
                    } else {
                        binding.addToBagBtn.setEnabled(true);
                        binding.addToBagBtn.setText("Thêm vào giò hàng");
                    }

                    // Lưu lại để dùng khi thêm vào giỏ
                    this.price = price;
                    this.productImage = product.getImageUrl();
                    this.categoryId = String.valueOf(product.getCategoryId());

                }
            });
        });
    }
}
