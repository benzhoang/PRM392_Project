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
        void onResult(SignUpResponse response, String errorMessage);
    }

    public void signUpUser(String email, String password, String name, SignUpCallback callback) {
        executorService.execute(() -> {
            try {
                Response<SignUpResponse> response = repository.signUp(
                        new SignUpRequest(email, password, name)
                ).execute();
                if (response.isSuccessful() && response.body() != null) {
                    SignUpResponse signUpResponse = response.body();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            callback.onResult(signUpResponse, null)
                    );
                } else {
                    // Lấy message lỗi từ response.errorBody()
                    String errorMessage = "Error signing up";
                    try {
                        if (response.errorBody() != null) {
                            String errorJson = response.errorBody().string();
                            org.json.JSONObject jsonObject = new org.json.JSONObject(errorJson);
                            errorMessage = jsonObject.optString("message", errorMessage);
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                    String finalErrorMessage = errorMessage;
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            callback.onResult(null, finalErrorMessage)
                    );
                }
            } catch (Exception e) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                        callback.onResult(null, "Lỗi kết nối server")
                );
            }
        });
    }
}

