package com.example.prm392_project.ui.activities.splash;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashViewModel extends ViewModel {
    private final CosmeticsRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    public SplashViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    public interface EmailExistsCallback{
        void onResult(boolean isConnected);
    }

    public void checkEmailExits(final String email, final EmailExistsCallback callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("SplashViewModel", "Checking server connection");
                boolean isServerConnected = false;
                try {
                    retrofit2.Call<Boolean> call = repository.checkUserExist(email);
                    Boolean result = false;
                    try {
                        retrofit2.Response<Boolean> response = call.execute();
                        if (response.isSuccessful() && response.body() != null) {
                            result = response.body();
                        }
                    } catch (Exception ex) {
                        Log.e("SplashViewModel", "Error checkUserExist: " + ex.getMessage());
                        result = false;
                    }
                    isServerConnected = result;
                } catch (Exception e) {
                    Log.e("SplashViewModel", "Error checking server connection: " + e.getMessage());
                    isServerConnected = false;
                }
                final boolean finalIsServerConnected = isServerConnected;
                // Gọi callback ở main thread
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("SplashViewModel", "Server connected: " + finalIsServerConnected);
                        callback.onResult(finalIsServerConnected);
                    }
                });
            }
        });
    }
}
