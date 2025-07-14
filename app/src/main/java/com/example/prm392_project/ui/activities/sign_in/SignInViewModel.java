package com.example.prm392_project.ui.activities.sign_in;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.auth.sign_in.SignInRequest;
import com.example.prm392_project.data.model.auth.sign_in.SignInResponse;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Response;

@HiltViewModel
public class SignInViewModel extends ViewModel {
    private final CosmeticsRepository repository;

    @Inject
    public SignInViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    // Interface callback Java
    public interface SignInCallback {
        void onResult(SignInResponse response);
    }

    // Hàm đăng nhập bất đồng bộ (Java)
    public void signInUser(String email, String password, SignInCallback callback) {
        new Thread(() -> {
            try {
                SignInRequest signInRequest = new SignInRequest(email, password);
                // repository.signIn(signInRequest) phải trả về Call<SignInResponse>
                Response<SignInResponse> response = repository.signIn(signInRequest).execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(null);
                }
            } catch (Exception e) {
                Log.e("SignInViewModel", "Error signing in: " + e.getMessage());
                callback.onResult(null);
            }
        }).start();
    }
}
