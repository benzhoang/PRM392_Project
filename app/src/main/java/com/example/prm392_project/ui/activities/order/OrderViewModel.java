package com.example.prm392_project.ui.activities.order;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.data.model.main.order.OrderResponse;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.repository.CosmeticsRepository;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OrderViewModel extends ViewModel {

    private final CosmeticsRepository cosmeticsRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public OrderViewModel(CosmeticsRepository cosmeticsRepository) {
        this.cosmeticsRepository = cosmeticsRepository;
    }

    // -- Functional interfaces for callback --
    public interface BooleanCallback {
        void onResult(boolean result);
    }

    public interface IntCallback {
        void onResult(int result);
    }

    // --- API calls with callbacks (equivalent to coroutine launch) ---

    public void createOrder(String token, Map<String, Object> body, CartViewModel.CreateOrderCallback callback) {
        executorService.execute(() -> {
            try {
                retrofit2.Response<OrderResponse> response = cosmeticsRepository.createOrder(token, body).execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(null);
                }
            } catch (Exception e) {
                callback.onResult(null);
            }
        });
    }

    public void getProductStockOrder(String token, String id, IntCallback callback) {
        executorService.execute(() -> {
            try {
                // Được dùng trong background thread (Executor, v.v.)
                retrofit2.Response<Product> response = cosmeticsRepository.getProductById( id).execute();
                if (response.isSuccessful() && response.body() != null) {
                    // sử dụng biến stock ở đây
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateProductStockOrder(String token, String id, Map<String, Integer> stock, BooleanCallback callback) {
        executorService.execute(() -> {
            try {
                cosmeticsRepository.updateProductStock(token, id, stock);
                callback.onResult(true);
            } catch (Exception e) {
                Log.e("OrderViewModel", "Error updating product stock: " + e.getMessage());
                callback.onResult(false);
            }
        });
    }

    public void deleteAllCartOrder(String token, String userId, BooleanCallback callback) {
        executorService.execute(() -> {
            try {
                cosmeticsRepository.deleteAllCart(token, userId);
                callback.onResult(true);
            } catch (Exception e) {
                Log.e("OrderViewModel", "Error deleting all cart: " + e.getMessage());
                callback.onResult(false);
            }
        });
    }

    // Optional: Call this when ViewModel is destroyed
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
