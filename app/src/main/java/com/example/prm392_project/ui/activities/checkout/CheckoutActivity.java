package com.example.prm392_project.ui.activities.checkout;

import android.app.AlertDialog;
import android.content.Intent;
import androidx.core.graphics.Insets;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

        // Lấy total đúng kiểu double
        final double total = getIntent().getDoubleExtra("total", 0);
        final double shippingFee = total * 0.1;
        final String token = getIntent().getStringExtra("token");

        // Hiện lên UI
        binding.tvShippingFeeValue.setText(shippingFee +"Đ");
        binding.tvTotalQuantityValue.setText( total+"Đ");
        binding.tvTotalValue.setText( (total + shippingFee)+"Đ");

        // Lấy info đã lưu (nếu có)
        final String[] phoneHolder = {SharedPrefUtils.getString(this, "phoneNumber", "")};
        final String[] addressHolder = {SharedPrefUtils.getString(this, "address", "")};

        binding.phoneTv.setText(phoneHolder[0]);
        binding.addressTv.setText(addressHolder[0]);
        binding.editAddressIv.setOnClickListener(v -> {
            showEditDialog("Edit Address", addressHolder[0], InputType.TYPE_CLASS_TEXT, R.drawable.ic_location, value -> {
                if (value.isEmpty()) {
                    Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                addressHolder[0] = value;
                binding.addressTv.setText(value);
                SharedPrefUtils.saveString(this, "address", value);
            });
        });

        binding.editPhoneIv.setOnClickListener(v -> {
            showEditDialog("Edit Phone Number", phoneHolder[0], InputType.TYPE_CLASS_PHONE, R.drawable.ic_telephone, value -> {
                if (value.isEmpty()) {
                    Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                phoneHolder[0] = value;
                binding.phoneTv.setText(value);
                SharedPrefUtils.saveString(this, "phoneNumber", value);
            });
        });

        // Khi bấm nút checkout
        binding.btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("total", total + shippingFee); // double
            intent.putExtra("phoneNumber", phoneHolder[0]);
            intent.putExtra("address", addressHolder[0]);
            intent.putExtra("token", token);

            Log.d("CheckoutActivity", "total: " + (total + shippingFee));
            Log.d("CheckoutActivity", "phone: " + phoneHolder[0]);
            Log.d("CheckoutActivity", "address: " + addressHolder[0]);

            startActivity(intent);
        });
    }

    private void showEditDialog(String title, String defaultValue, int inputType, int iconResId, ValueCallback callback) {
        EditText editText = new EditText(this);
        editText.setInputType(inputType);
        editText.setText(defaultValue != null ? defaultValue : "");
        editText.setSelection(editText.getText().length());
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