package com.example.prm392_project.ui.activities.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.R;
import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.data.model.main.order.OrderProduct;
import com.example.prm392_project.databinding.ActivityOrderBinding;
import com.example.prm392_project.ui.activities.MainActivity;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;
import com.example.prm392_project.util.SharedPrefUtils;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private OrderViewModel orderViewModel;
    private CartViewModel cartViewModel;
    private String paymentMethod = "Cash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ViewModel Hilt
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Lấy dữ liệu từ Intent
        double total = getIntent().getDoubleExtra("total", 0);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String token = getIntent().getStringExtra("token");
        String address = getIntent().getStringExtra("address");
        String userId = SharedPrefUtils.getString(this, "user_id", "");
        Log.d("OrderActivity", "total: " + total);
        Log.d("OrderActivity", "phone: " + phoneNumber);
        Log.d("OrderActivity", "address: " + address);


        // Radio buttons chọn payment method
        binding.rdCash.setChecked(true);
        binding.rdCc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                paymentMethod = "CC";
                binding.rdCash.setChecked(false);
                binding.rdPaypal.setChecked(false);
            }
        });
        binding.rdCash.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                paymentMethod = "Cash";
                binding.rdCc.setChecked(false);
                binding.rdPaypal.setChecked(false);
            }
        });
        binding.rdPaypal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                paymentMethod = "Paypal";
                binding.rdCash.setChecked(false);
                binding.rdCc.setChecked(false);
            }
        });

        // Đặt hàng
        binding.btnNext.setOnClickListener(v -> {
            // Lấy danh sách cart từ local
            List<CartViewModel.CartItemLocal> cartItems = cartViewModel.getCartList(this);

            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userId == null || userId.trim().isEmpty() || token == null || token.trim().isEmpty()) {
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuẩn bị orderItems cho API
            List<Map<String, Object>> orderItems = new ArrayList<>();
            for (CartViewModel.CartItemLocal item : cartItems) {
                Map<String, Object> map = new HashMap<>();
                map.put("product_id", item.productId);
                map.put("quantity", item.quantity);
                orderItems.add(map);
            }
            Map<String, Object> body = new HashMap<>();
            body.put("user_id", userId);
            body.put("payment_method", paymentMethod);
            body.put("orderItems", orderItems);

            binding.btnNext.setEnabled(false);

            // Gọi API tạo order
            orderViewModel.createOrder(token, body, orderResponse -> {
                runOnUiThread(() -> {
                    binding.btnNext.setEnabled(true);
                    if (orderResponse != null) {
                        // Xóa cart local
                        cartViewModel.saveCartList(this, new ArrayList<>());
                        PopupDialog.getInstance(OrderActivity.this)
                                .setStyle(Styles.SUCCESS)
                                .setHeading("Order created successfully!")
                                .setDescription("Thanks for your purchase")
                                .setDismissButtonText("OK")
                                .setCancelable(false)
                                .showDialog(new OnDialogButtonClickListener() {
                                    @Override
                                    public void onDismissClicked(Dialog dialog) {
                                        dialog.dismiss();

                                        finish();
                                    }
                                });
                    } else {
                        Toast.makeText(OrderActivity.this, "Error creating order", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}