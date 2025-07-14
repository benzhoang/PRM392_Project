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
    private OrderViewModel viewModel;
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

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        String totalStr = getIntent().getStringExtra("total");
        String email = getIntent().getStringExtra("email");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String token = getIntent().getStringExtra("token");
        String address = getIntent().getStringExtra("address");
        String userId = SharedPrefUtils.getString(this, "id", "");

        double total = totalStr != null ? Double.parseDouble(totalStr) : 0;

        // Ngày đặt
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = now.format(formatter);

        // Radio buttons
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

        // Checkout button
        binding.btnNext.setOnClickListener(v -> {
            cartViewModel.getCartsLiveData().observe(this, carts -> {
                if (carts != null && !carts.isEmpty()) {

                    List<OrderProduct> products = new ArrayList<>();
                    for (int i = 0; i < carts.size(); i++) {
                        products.add(new OrderProduct(
                                carts.get(i).getId(),
                                carts.get(i).getItems().getProduct(),
                                carts.get(i).getItems().getQuantity()
                        ));
                    }

                    Order order = new Order(
                            "",                    // id
                            userId,                // userId
                            address,               // address
                            email,                 // email
                            phoneNumber,           // phoneNumber
                            paymentMethod,         // paymentMethod
                            "Pending",             // status
                            (int) total,           // total
                            products,              // products
                            dateStr,               // createdAt
                            dateStr                // updatedAt
                    );

                    Log.d("OrderActivity", "Creating order: " + order);

                    viewModel.createOrderOrder(token, order, isCreated -> {
                        if (isCreated) {
                            for (int i = 0; i < carts.size(); i++) {
                                String productId = carts.get(i).getItems().getProduct();
                                int quantity = carts.get(i).getItems().getQuantity();

                                viewModel.getProductStockOrder(token, productId, stock -> {
                                    int newStock = stock - quantity;
                                    Map<String, Integer> stockMap = new HashMap<>();
                                    stockMap.put("stock", newStock);
                                    viewModel.updateProductStockOrder(token, productId, stockMap, isUpdated -> {
                                        if (!isUpdated) {
                                            Log.e("OrderActivity", "Error updating stock for: " + productId);
                                        }
                                    });
                                });
                            }

                            viewModel.deleteAllCartOrder(token, userId, isDeleted -> {
                                if (isDeleted) {
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
                                                    Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                } else {
                                    Toast.makeText(OrderActivity.this, "Error deleting cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(OrderActivity.this, "Error creating order", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(OrderActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}
