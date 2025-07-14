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


        });
    }
}
