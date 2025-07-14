package com.example.prm392_project.ui.activities.check_account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivityCheckAccountBinding;
import com.example.prm392_project.ui.activities.sign_in.SignInActivity;
import com.example.prm392_project.ui.activities.sign_up.SignUpActivity;
import com.example.prm392_project.ui.activities.splash.SplashViewModel;

@dagger.hilt.android.AndroidEntryPoint
public class CheckAccountActivity extends AppCompatActivity {
    private ActivityCheckAccountBinding binding;
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckAccountBinding.inflate(getLayoutInflater());
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

        // Lấy ViewModel kiểu Hilt/Java
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        binding.continueBtn.setOnClickListener(view -> {
            String email = binding.emailEt.getText().toString().trim();

            if (!isValidEmail(email)) {
                binding.emailEt.setError("Invalid email address");
                return;
            }

            // Hàm checkEmailExits phải dùng interface/callback Java thuần
            viewModel.checkEmailExits(email, new SplashViewModel.EmailExistsCallback() {
                @Override
                public void onResult(boolean isExists) {
                    Intent intent;
                    if (isExists) {
                        intent = new Intent(CheckAccountActivity.this, SignInActivity.class);
                    } else {
                        intent = new Intent(CheckAccountActivity.this, SignUpActivity.class);
                    }
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            });
        });
    }

    // Validate email kiểu Java thuần (bạn có thể thay regex nếu muốn)
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
