package com.example.prm392_project.ui.activities.sign_in;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivitySignInBinding;
import com.example.prm392_project.ui.activities.MainActivity;
import com.example.prm392_project.util.SharedPrefUtils;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private SignInViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
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

        // Lấy ViewModel bằng Hilt (Java)
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(SignInViewModel.class);

        String email = getIntent().getStringExtra("email");
        binding.emailTv.setText(email);

        binding.editTv.setOnClickListener(v -> finish());

        binding.signInBtn.setOnClickListener(v -> {
            String password = binding.passwordEt.getText().toString().trim();
            if (!password.isEmpty()) {
                viewModel.signInUser(email, password, response -> {
                    runOnUiThread(() -> {
                        if (response != null) {
                            SharedPrefUtils.saveString(this, "userId", response.getId());
                            SharedPrefUtils.saveString(this, "email", response.getEmail());
                            SharedPrefUtils.saveString(this, "accessToken", response.getAccessToken());
                            SharedPrefUtils.saveBoolean(this, "isLogin", true);

                            PopupDialog.getInstance(this)
                                    .setStyle(Styles.SUCCESS)
                                    .setHeading("Đăng nhập thành công!")
                                    .setDescription("Chào mừng tới Cosmeticsshop")
                                    .setCancelable(false)
                                    .setDismissButtonText("Tiếp tục")
                                    .showDialog(new OnDialogButtonClickListener() {
                                        @Override
                                        public void onDismissClicked(Dialog dialog) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            binding.passwordEt.setError("Mật khẩu sai hoặc không hợp lệ");
                            Toast.makeText(this, "Mật khẩu sai hoặc không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
}
