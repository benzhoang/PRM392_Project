package com.example.prm392_project.ui.activities.order_detail;

import android.os.Bundle;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.R;
import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.databinding.ActivityOrderDetailBinding;
import com.example.prm392_project.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderDetailActivity extends AppCompatActivity {
    private ActivityOrderDetailBinding binding;
    private OrderDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Lấy ViewModel dùng Hilt (Java)
        viewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);

        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        String orderId = getIntent().getStringExtra("orderId");
        if (orderId == null) orderId = "";

        String token = "Bearer " + SharedPrefUtils.getString(this, "accessToken", "");

        // Gọi ViewModel để lấy chi tiết đơn hàng
        viewModel.getOrderByIdDetail(token, orderId, new OrderDetailViewModel.OrderCallback() {
            @Override
            public void onResult(Order order) {
                if (order != null) {
                    binding.orderId.setText(order.getId());
                    binding.orderPlaced.setText(order.getCreatedAt());
                    binding.total.setText(order.getTotal() + " USD");
                    binding.paymentMethod.setText(order.getPaymentMethod());
                    binding.address.setText(order.getAddress());
                    binding.phone.setText(order.getPhoneNumber());
                    binding.email.setText(order.getEmail());

                    binding.trackOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(OrderDetailActivity.this)
                                    .setTitle("Order Status")
                                    .setMessage("Your order is " + order.getStatus())
                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                    });
                } else {
                    // Handle error nếu order null
                }
            }
        });
    }
}
