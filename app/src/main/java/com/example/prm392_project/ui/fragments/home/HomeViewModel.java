package com.example.prm392_project.ui.fragments.home;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.category.Category;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final CosmeticsRepository repository;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Inject
    public HomeViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    private void postToMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }
    // Callback interface for results
    public interface CategoriesCallback {
        void onResult(List<Category> categories);
    }

    public interface ProductsCallback {
        void onResult(List<Product> products);
    }

    // Get Categories with callback
    public void getCategoriesHome(CategoriesCallback callback) {
        executor.execute(() -> {
            try {
                List<Category> response = repository.getCategories().execute().body();
                if (response != null) {
                    postToMainThread(() -> callback.onResult(response));
                } else {
                    postToMainThread(() -> callback.onResult(null));
                }
            } catch (Exception e) {
                Log.e("HomeViewModel", "Error getting categories: " + e.getMessage());
                postToMainThread(() -> callback.onResult(null));
            }
        });
    }

    // Get Products
    public void getProductsHome( ProductsCallback callback) {
        executor.execute(() -> {
            try {
                retrofit2.Response<List<Product>> response = repository.getProducts().execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(null); // hoặc callback.onResult(Collections.emptyList());
                }
            } catch (Exception e) {
                Log.e("HomeViewModel", "Error getting products: " + e.getMessage());
                callback.onResult(null); // hoặc callback.onResult(Collections.emptyList());
            }
        });
    }

    // Get Products by Category
    public void getProductsByCategoryHome(String token, String categoryId, ProductsCallback callback) {
        executor.execute(() -> {
            try {
                retrofit2.Response<List<Product>> response = repository.getProductsByCategory(token, categoryId).execute();
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(null); // hoặc Collections.emptyList()
                }
            } catch (Exception e) {
                Log.e("HomeViewModel", "Error getting products by category: " + e.getMessage());
                callback.onResult(null); // hoặc Collections.emptyList()
            }
        });
    }
}
