package com.example.prm392_project.ui.fragments.cart;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.data.model.main.cart.CartResponse;
import com.example.prm392_project.data.model.main.order.OrderResponse;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.data.repository.CosmeticsRepository;
import com.example.prm392_project.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Inject
    public CartViewModel(CosmeticsRepository repository) {
        this.repository = repository;
    }

    // 1. Đọc cart local từ SharedPref (chỉ chứa productId, quantity)
    public List<CartItemLocal> getCartList(Context context) {
        List<CartItemLocal> cartItems = new ArrayList<>();
        String cartJson = SharedPrefUtils.getString(context, "cart_items", "[]");
        try {
            org.json.JSONArray array = new org.json.JSONArray(cartJson);
            for (int i = 0; i < array.length(); i++) {
                org.json.JSONObject obj = array.getJSONObject(i);
                CartItemLocal item = new CartItemLocal();
                item.productId = obj.getInt("product_id");
                item.quantity = obj.getInt("quantity");
                cartItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    // 2. Gọi API lấy chi tiết sản phẩm (bằng productId)
    public void getProductDetail(String productId, OnProductDetailReceived callback) {
        executorService.execute(() -> {
            try {
                retrofit2.Call<Product> call = repository.getProductById(productId);
                retrofit2.Response<Product> response = call.execute();

                Product product = null;
                if (response.isSuccessful()) {
                    product = response.body();
                }
                Log.d("CartFragment", "API trả về product: " + (product == null ? "null" : product.getName()));
                if (callback != null) callback.onReceived(product);
            } catch (Exception e) {
                if (callback != null) callback.onReceived(null);
            }
        });
    }


    public interface CreateOrderCallback {
        void onResult(OrderResponse order);
    }

    // 3. Cập nhật lại danh sách cart local vào SharedPref
    public void saveCartList(Context context, List<CartItemLocal> items) {
        org.json.JSONArray array = new org.json.JSONArray();
        for (CartItemLocal item : items) {
            org.json.JSONObject obj = new org.json.JSONObject();
            try {
                obj.put("product_id", item.productId);
                obj.put("quantity", item.quantity);
            } catch (Exception ignored) {}
            array.put(obj);
        }
        SharedPrefUtils.saveString(context, "cart_items", array.toString());
    }

    // 4. Interface callback khi lấy chi tiết sp
    public interface OnProductDetailReceived {
        void onReceived(Product product);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdownNow();
    }

    // 5. Model CartItemLocal
    public static class CartItemLocal {
        public int productId;
        public int quantity;
    }
}

