package com.example.prm392_project.ui.activities.sign_up;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.auth.sign_up.SignUpRequest;
import com.example.prm392_project.data.model.auth.sign_up.SignUpResponse;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import retrofit2.Response;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    private final CosmeticsRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public SignUpViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    public interface SignUpCallback {
        void onResult(SignUpResponse response);
    }

    public void signUpUser(String email, String password, String firstName, String lastName,
                           String birthDate, String address, String phoneNumber,
                           SignUpCallback callback) {
        executorService.execute(() -> {
            try {
                // Gọi API hoặc repository ở đây...
                // Nếu repo trả về Call<SignUpResponse>, nhớ .execute() để lấy response thật
                Response<SignUpResponse> response = repository.signUp(
                        new SignUpRequest(email, password, firstName, lastName, birthDate, address, phoneNumber)
                ).execute();
                if (response.isSuccessful() && response.body() != null) {
                    SignUpResponse signUpResponse = response.body();
                    // Gọi callback trên main thread
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onResult(signUpResponse));
                } else {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onResult(null));
                }
            } catch (Exception e) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onResult(null));
            }
        });
    }
}

