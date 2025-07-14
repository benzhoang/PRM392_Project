package com.example.prm392_project.ui.activities.search;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final CosmeticsRepository cosmeticsRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public SearchViewModel(CosmeticsRepository cosmeticsRepository) {
        this.cosmeticsRepository = cosmeticsRepository;
    }

    public void searchProductsSearch(String token, String name, SearchCallback callback) {
        executor.execute(() -> {
            try {
                retrofit2.Response<List<Product>> response = cosmeticsRepository.searchProductByName(token, name).execute();
                List<Product> products;
                if (response.isSuccessful() && response.body() != null) {
                    products = response.body();
                } else {
                    products = java.util.Collections.emptyList();
                }
                callback.onResult(products);
            } catch (Exception e) {
                Log.e("SearchViewModel", "Error searching products: " + e.getMessage());
                callback.onResult(java.util.Collections.emptyList());
            }
        });
    }

    // Interface callback để trả về dữ liệu cho UI thread
    public interface SearchCallback {
        void onResult(List<Product> products);
    }
}
