package com.example.prm392_project.ui.fragments.cart;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.data.model.main.cart.CartResponse;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Response;

@HiltViewModel
public class CartViewModel extends ViewModel {

    private final CosmeticsRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<Cart>> cartsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Product> productDetailLiveData = new MutableLiveData<>();
    private final MutableLiveData<Cart> updatedCartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteSuccessLiveData = new MutableLiveData<>();

    @Inject
    public CartViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Cart>> getCartsLiveData() {
        return cartsLiveData;
    }

    public LiveData<Product> getProductDetailLiveData() {
        return productDetailLiveData;
    }

    public LiveData<Cart> getUpdatedCartLiveData() {
        return updatedCartLiveData;
    }

    public LiveData<Boolean> getDeleteSuccessLiveData() {
        return deleteSuccessLiveData;
    }

    public void getAllCarts(String token, String userId) {
        executorService.execute(() -> {
            try {
                Response<CartResponse> response = repository.getAllCarts(token, userId).execute();
                if (response.isSuccessful() && response.body() != null) {
                    cartsLiveData.postValue(response.body().getData());
                } else {
                    cartsLiveData.postValue(null);
                }
            } catch (Exception e) {
                Log.e("CartViewModel", "Error getting all carts: " + e.getMessage());
                cartsLiveData.postValue(null);
            }
        });
    }

    public void updateQuantity(String token, String id, Map<String, Integer> quantity) {
        executorService.execute(() -> {
            try {
                Response<Cart> response = (Response<Cart>) repository.updateCart(token, id, quantity).execute();
                if (response.isSuccessful() && response.body() != null) {
                    updatedCartLiveData.postValue(response.body());
                } else {
                    updatedCartLiveData.postValue(null);
                }
            } catch (Exception e) {
                Log.e("CartViewModel", "Error updating quantity: " + e.getMessage());
                updatedCartLiveData.postValue(null);
            }
        });
    }

    public void getProductDetail( String productId, OnProductDetailReceived callback) {
        executorService.execute(() -> {
            try {
                Product product = (Product) repository.getProductById( productId);
                if (callback != null) callback.onReceived(product);
            } catch (Exception e) {
                Log.e("CartViewModel", "Error getting product detail: " + e.getMessage());
                if (callback != null) callback.onReceived(null);
            }
        });
    }

    public interface OnProductDetailReceived {
        void onReceived(Product product);
    }

    public void deleteCart(String token, String cartId) {
        executorService.execute(() -> {
            try {
                Response<Void> response = (Response<Void>) repository.deleteCart(token, cartId).execute();
                deleteSuccessLiveData.postValue(response.isSuccessful());
            } catch (Exception e) {
                Log.e("CartViewModel", "Error deleting cart: " + e.getMessage());
                deleteSuccessLiveData.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdownNow();
    }
}
