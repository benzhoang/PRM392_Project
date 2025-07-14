package com.example.prm392_project.ui.activities.product_detail;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.data.model.main.cart.CartRequest;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductDetailViewModel extends ViewModel {

    private final CosmeticsRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Inject
    public ProductDetailViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    // Callback interface
    public interface ProductCallback {
        void onResult(Product product);
    }

    public interface ProductListCallback {
        void onResult(List<Product> products);
    }

    public interface CartCallback {
        void onResult(Cart cart);
        void onError(Exception e);
    }

    public void getProductDetail( String id, ProductCallback callback) {
        executorService.execute(() -> {
            try {
                retrofit2.Response<Product> response = repository.getProductById(id).execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(null);
                }
            } catch (Exception e) {
                Log.e("ProductDetailViewModel", "Error getting product detail: " + e.getMessage());
                callback.onResult(null); // return null on error
            }
        });
    }

    public void getProductsByCategory(String token, String categoryId, ProductListCallback callback) {
        executorService.execute(() -> {
            try {
                retrofit2.Call<List<Product>> call = repository.getProductsByCategory(token, categoryId);
                retrofit2.Response<List<Product>> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(null); // hoặc callback.onResult(new ArrayList<>())
                }
            } catch (Exception e) {
                Log.e("ProductDetailViewModel", "Error getting products by category: " + e.getMessage());
                callback.onResult(null);
            }
        });
    }

    public void createCart(String token, CartRequest cartRequest, CartCallback callback) {
        executorService.execute(() -> {
            try {
                retrofit2.Call<Cart> call = repository.createCart(token, cartRequest);
                retrofit2.Response<Cart> response = call.execute();
                Cart cart = response.body();
                callback.onResult(cart);
            } catch (Exception e) {
                Log.e("ProductDetailViewModel", "Error creating cart: " + e.getMessage());
                callback.onError(e);
            }
        });
    }
}
