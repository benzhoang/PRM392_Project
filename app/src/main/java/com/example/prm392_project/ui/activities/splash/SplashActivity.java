package com.example.prm392_project.ui.activities.splash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivitySplashBinding;
import com.example.prm392_project.ui.activities.MainActivity;
import com.example.prm392_project.ui.activities.auth.AuthActivity;
import com.example.prm392_project.util.SharedPrefUtils;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(SplashViewModel.class);

        viewModel.checkEmailExits("test@gmail.com", new SplashViewModel.EmailExistsCallback () {
            @Override
            public void onResult(boolean isConnected) {
                if (isConnected) {
                    boolean isLogin = SharedPrefUtils.getBoolean(SplashActivity.this, "isLogin", false);
                    if (isLogin) {
                        navigateToMainActivity();
                    } else {
                        navigateToAuthActivity();
                    }
                } else {
                    PopupDialog.getInstance(SplashActivity.this)
                            .setStyle(Styles.FAILED)
                            .setHeading("Uh-Oh")
                            .setDescription("Unexpected error occurred. Try again later.")
                            .setDismissButtonText("Exit")
                            .setCancelable(false)
                            .showDialog(new OnDialogButtonClickListener() {
                                @Override
                                public void onDismissClicked(Dialog dialog) {
                                    super.onDismissClicked(dialog);
                                    finish();
                                }
                            });
                }
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
