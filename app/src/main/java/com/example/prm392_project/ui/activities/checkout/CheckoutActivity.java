package com.example.prm392_project.ui.activities.checkout;

import android.app.AlertDialog;
import android.content.Intent;
import androidx.core.graphics.Insets;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivityCheckoutBinding;
import com.example.prm392_project.ui.activities.order.OrderActivity;
import com.example.prm392_project.util.SharedPrefUtils;

@dagger.hilt.android.AndroidEntryPoint
public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private String email;
    private String phoneNumber;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String totalStr = getIntent().getStringExtra("total");
        double total = totalStr != null ? Double.parseDouble(totalStr) : 0;
        double shippingFee = total * 0.1;
        String token = getIntent().getStringExtra("token");

        binding.tvTotalQuantityValue.setText("$ " + total);
        binding.tvTotalValue.setText("$ " + (total + shippingFee));

        binding.editEmailIv.setOnClickListener(v -> {
            showEditDialog("Edit Email", email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, R.drawable.ic_email, value -> {
                if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                email = value;
                binding.emailTv.setText(email);
                SharedPrefUtils.saveString(this, "email", email);
            });
        });

        binding.editAddressIv.setOnClickListener(v -> {
            showEditDialog("Edit Address", address, InputType.TYPE_CLASS_TEXT, R.drawable.ic_location, value -> {
                if (value.isEmpty()) {
                    Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                address = value;
                binding.addressTv.setText(address);
                SharedPrefUtils.saveString(this, "address", address);
            });
        });

        binding.editPhoneIv.setOnClickListener(v -> {
            showEditDialog("Edit Phone Number", phoneNumber, InputType.TYPE_CLASS_PHONE, R.drawable.ic_telephone, value -> {
                if (value.isEmpty()) {
                    Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                phoneNumber = value;
                binding.phoneTv.setText(phoneNumber);
                SharedPrefUtils.saveString(this, "phoneNumber", phoneNumber);
            });
        });

        binding.btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("total", String.valueOf(total + shippingFee));
            intent.putExtra("email", email);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("address", address);
            intent.putExtra("token", token);
            startActivity(intent);
        });
    }

    private void showEditDialog(String title, String defaultValue, int inputType, int iconResId, ValueCallback callback) {
        EditText editText = new EditText(this);
        editText.setInputType(inputType);
        editText.setText(defaultValue);
        editText.setSelection(defaultValue.length());
        editText.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        editText.setCompoundDrawablePadding(16);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("OK", (dialog, which) -> {
                    String value = editText.getText().toString().trim();
                    callback.onValueEntered(value);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public interface ValueCallback {
        void onValueEntered(String value);
    }
}
