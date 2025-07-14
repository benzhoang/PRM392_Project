package com.example.prm392_project.ui.activities.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivitySettingBinding;
import com.example.prm392_project.ui.activities.term_of_user.TermOfUserActivity;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Edge-to-edge tương tự Kotlin
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        binding.cvTermOfUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermOfUserActivity.class);
            startActivity(intent);
        });
    }
}
