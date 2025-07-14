package com.example.prm392_project.ui.activities.order_detail;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Response;

@HiltViewModel
public class OrderDetailViewModel extends ViewModel {
    private final CosmeticsRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public OrderDetailViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    public interface OrderCallback {
        void onResult(Order order);
    }

    public void getOrderByIdDetail(String token, String id, OrderCallback callback) {
        executorService.execute(() -> {
            try {
                // Gọi Retrofit để lấy dữ liệu Order
                Response<Order> response = repository.getOrderById(token, id).execute();
                Order order = null;
                if (response.isSuccessful() && response.body() != null) {
                    order = response.body();
                }
                Order finalOrder = order;
                // callback về main thread
                new Handler(Looper.getMainLooper()).post(() -> callback.onResult(finalOrder));
            } catch (Exception e) {
                Log.e("OrderDetailViewModel", "Error getting order: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onResult(null));
            }
        });
    }
}
