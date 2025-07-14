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

        binding.dateEt.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                binding.dateEt.setText(date);
            }, 2000, 1, 1);
            datePicker.show();
        });

        binding.createAccountBtn.setOnClickListener(v -> {
            boolean error = false;
            String firstname = binding.firstnameEt.getText().toString().trim();
            String lastname = binding.lastnameEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();
            String address = binding.addressEt.getText().toString().trim();
            String phoneNumber = binding.phoneEt.getText().toString().trim();
            String birthDateInput = binding.dateEt.getText().toString().trim();
            String formattedBirthDate = null;
            if (!birthDateInput.isEmpty()) {
                try {
                    SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    formattedBirthDate = sdfOutput.format(sdfInput.parse(birthDateInput));
                } catch (ParseException e) {
                    e.printStackTrace();
                    error = true;
                }
            }

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
            // Validate address
            if (address.isEmpty() || address.length() < 10) {
                binding.addressEt.setError("Address must be at least 10 characters");
                binding.addressEt.requestFocus();
                error = true;
            } else {
                binding.addressEt.setError(null);
            }
            // Validate phone number
            if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10,12}")) {
                binding.phoneEt.setError("Invalid phone number");
                binding.phoneEt.requestFocus();
                error = true;
            } else {
                binding.phoneEt.setError(null);
            }
            // Validate birth date
            if (birthDateInput.isEmpty()) {
                binding.dateEt.setError("Birthdate cannot be empty");
                binding.dateEt.requestFocus();
                error = true;
            } else {
                binding.dateEt.setError(null);
            }

            if (error) {
                return;
            } else {
                viewModel.signUpUser(
                        email,
                        password,
                        firstname,
                        lastname,
                        formattedBirthDate,
                        address,
                        phoneNumber,
                        response -> {
                            if (response != null) {
                                SharedPrefUtils.saveString(this, "id", response.getData().getId());
                                SharedPrefUtils.saveString(this, "email", response.getData().getEmail());
                                SharedPrefUtils.saveString(this, "firstName", response.getData().getFirstName());
                                SharedPrefUtils.saveString(this, "lastName", response.getData().getLastName());
                                SharedPrefUtils.saveString(this, "birthDate", response.getData().getBirthDate());
                                SharedPrefUtils.saveString(this, "address", response.getData().getAddress());
                                SharedPrefUtils.saveString(this, "phoneNumber", response.getData().getPhoneNumber());
                                SharedPrefUtils.saveString(this, "accessToken", response.getAccessToken());
                                SharedPrefUtils.saveBoolean(this, "isLogin", true);

                                PopupDialog.getInstance(this)
                                        .setStyle(Styles.SUCCESS)
                                        .setHeading("Sign up success!")
                                        .setDescription("Welcome to Cosmeticsshop")
                                        .setDismissButtonText("OK")
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
                                Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }
}
