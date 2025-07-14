package com.example.prm392_project.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivityAuthBinding;
import com.example.prm392_project.ui.activities.check_account.CheckAccountActivity;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
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

        binding.signInBtn.setOnClickListener(v -> navigateToCheckAccountActivity("login"));
        binding.joinUsBtn.setOnClickListener(v -> navigateToCheckAccountActivity("register"));
    }

    private void navigateToCheckAccountActivity(String mode) {
        Intent intent = new Intent(this, CheckAccountActivity.class);
        intent.putExtra("mode", mode); // Truyền "login" hoặc "register"
        startActivity(intent);
    }
}
