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
        // Luôn giả lập là server kết nối thành công
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            callback.onResult(true);
        }, 800); // Có thể delay nhẹ để giống hiệu ứng loading
    }

}
