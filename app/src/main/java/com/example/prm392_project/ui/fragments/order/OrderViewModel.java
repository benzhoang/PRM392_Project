package com.example.prm392_project.ui.fragments.order;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.data.repository.CosmeticsRepository;
import com.example.prm392_project.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

@HiltViewModel
public class OrderViewModel extends AndroidViewModel {

    private final CosmeticsRepository cosmeticsRepository;
    private final String token;
    private final String bearerToken;
    private final String userId;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public OrderViewModel(@NonNull CosmeticsRepository cosmeticsRepository, @NonNull Application application) {
        super(application);
        this.cosmeticsRepository = cosmeticsRepository;
        this.token = SharedPrefUtils.getString(application.getApplicationContext(), "accessToken", "");
        this.bearerToken = "Bearer " + (token == null ? "" : token);
        this.userId = SharedPrefUtils.getString(application.getApplicationContext(), "id", "");
    }

    public interface OrdersCallback {
        void onResult(List<Order> orders);
    }

    public void getOrders(final String status, final OrdersCallback callback) {
        executorService.execute(() -> {
            try {
                // Giả sử repository.getOrders trả về Call<List<Order>>
                retrofit2.Response<List<Order>> response = cosmeticsRepository.getOrders(bearerToken, userId).execute();
                List<Order> filteredOrders = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    for (Order order : response.body()) {
                        if (order.getStatus().equals(status)) {
                            filteredOrders.add(order);
                        }
                    }
                }
                // Trả về main thread nếu cần
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onResult(filteredOrders));
            } catch (Exception e) {
                Log.e("OrderViewModel", "Error getting orders: " + e.getMessage());
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onResult(new ArrayList<>()));
            }
        });
    }
}
