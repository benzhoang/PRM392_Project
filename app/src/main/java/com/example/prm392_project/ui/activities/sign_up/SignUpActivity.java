package com.example.prm392_project.ui.activities.sign_up;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.R;
import com.example.prm392_project.databinding.ActivitySignUpBinding;
import com.example.prm392_project.ui.activities.MainActivity;
import com.example.prm392_project.util.SharedPrefUtils;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        String email = getIntent().getStringExtra("email");
        binding.emailTv.setText(email);

        binding.editTv.setOnClickListener(v -> finish());



        binding.createAccountBtn.setOnClickListener(v -> {
            boolean error = false;
            String firstname = binding.firstnameEt.getText().toString().trim();
            String lastname = binding.lastnameEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();


            // Validate first name (bạn có thể gọi lại easyvalidation trong Java nếu đã add dependency)
            if (firstname.isEmpty() || firstname.matches(".*\\d.*")) {
                binding.firstnameEt.setError("Invalid first name");
                binding.firstnameEt.requestFocus();
                error = true;
            } else {
                binding.firstnameEt.setError(null);
            }
            // Validate last name
            if (lastname.isEmpty() || lastname.matches(".*\\d.*")) {
                binding.lastnameEt.setError("Invalid last name");
                binding.lastnameEt.requestFocus();
                error = true;
            } else {
                binding.lastnameEt.setError(null);
            }
            // Validate password
            if (password.length() < 6 || !password.matches(".*\\d.*")) {
                binding.passwordEt.setError("Password must be at least 6 characters and have a number");
                binding.passwordEt.requestFocus();
                error = true;
            } else {
                binding.passwordEt.setError(null);
            }

            String name = firstname + " " + lastname;
            if (error) {
                return;
            } else {
                viewModel.signUpUser(
                        email,
                        password,
                        name,
                        (response, errorMessage) -> {
                            if (response != null) {
                                SharedPrefUtils.saveString(this, "email", response.getUser().getEmail());
                                SharedPrefUtils.saveString(this, "name", response.getUser().getName());

                                PopupDialog.getInstance(this)
                                        .setStyle(Styles.SUCCESS)
                                        .setHeading("Đăng kí thành công!")
                                        .setDescription("Chào mừng tới Cosmeticsshop")
                                        .setDismissButtonText("Tiếp tục")
                                        .setCancelable(false)
                                        .showDialog(new OnDialogButtonClickListener() {
                                            @Override
                                            public void onDismissClicked(Dialog dialog) {
                                                super.onDismissClicked(dialog);
                                                dialog.dismiss();
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            } else {
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }
}
