package com.example.prm392_project.ui.fragments.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.prm392_project.adapter.cart.CartAdapter;
import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.databinding.FragmentCartBinding;
import com.example.prm392_project.ui.activities.checkout.CheckoutActivity;
import com.example.prm392_project.ui.activities.order.OrderViewModel;
import com.example.prm392_project.ui.activities.product_detail.ProductDetailActivity;
import com.example.prm392_project.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartFragment extends Fragment {
    private OrderViewModel orderViewModel;

    private FragmentCartBinding binding;
    private CartAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        Log.d("CartFragment", "onViewCreated called");
        List<CartItem> cartItems = getCartItemsFromPref();
        Log.d("CartFragment", "Cart items loaded: " + cartItems.size());
        adapter = new CartAdapter(cartItems, new CartAdapter.CartActionListener() {
            @Override
            public void onMinus(CartItem item) {
                updateCartItemQuantity(item.productId, -1);
            }

            @Override
            public void onPlus(CartItem item) {
                updateCartItemQuantity(item.productId, 1);
            }

            @Override
            public void onDelete(CartItem item) {
                removeCartItem(item.productId);
            }

            @Override
            public void onItemClick(CartItem item) {
                // Chuyển sang ProductDetailActivity
                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                intent.putExtra("id", item.productId);
                startActivity(intent);
            }
        });
        binding.cartRv.setAdapter(adapter);
        loadCartProducts();
        binding.paymentBtn.setOnClickListener(v -> {

            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tính tổng tiền từ adapter/productMap
            double total = 0;
            for (CartItem item : cartItems) {
                // Lấy product từ adapter.productMap, nếu đã được load (nếu chưa load thì tính bằng 0)
                if (adapter.getProductMap().containsKey(item.productId)) {
                    total += adapter.getProductMap().get(item.productId).getPrice() * item.quantity;
                }
            }

            // Chuyển sang màn hình thanh toán (CheckoutActivity, hoặc OrderActivity...)
            Intent intent = new Intent(requireContext(), CheckoutActivity.class); // hoặc OrderActivity nếu bạn dùng activity này để nhập thông tin giao hàng
            // Truyền tổng tiền và danh sách sản phẩm (tùy bạn xử lý bên Activity, ở đây truyền tổng tiền là đủ)
            intent.putExtra("total", total);
            // Nếu muốn truyền cart chi tiết: có thể serialize thành JSON rồi truyền, hoặc chỉ truyền lại id và quantity, hoặc fetch lại trong Activity sau cũng được

            startActivity(intent);
        });
    }

    private List<CartItem> getCartItemsFromPref() {
        List<CartItem> list = new ArrayList<>();
        String cartJson = SharedPrefUtils.getString(requireContext(), "cart_items", "[]");
        try {
            if (cartJson == null || cartJson.trim().isEmpty()) cartJson = "[]";
            org.json.JSONArray array = new org.json.JSONArray(cartJson);
            for (int i = 0; i < array.length(); i++) {
                org.json.JSONObject obj = array.getJSONObject(i);
                int productId = obj.getInt("product_id");
                int quantity = obj.getInt("quantity");
                list.add(new CartItem(productId, quantity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // Cập nhật số lượng
    private void updateCartItemQuantity(int productId, int delta) {
        List<CartItem> items = getCartItemsFromPref();
        boolean changed = false;
        for (CartItem item : items) {
            if (item.productId == productId) {
                item.quantity += delta;
                if (item.quantity <= 0) {
                    items.remove(item);
                }
                changed = true;
                break;
            }
        }
        if (changed) saveCartItemsToPref(items);
        adapter.updateData(items);
        loadCartProducts();
    }

    // Xóa sản phẩm khỏi giỏ
    private void removeCartItem(int productId) {
        List<CartItem> items = getCartItemsFromPref();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).productId == productId) {
                items.remove(i);
                break;
            }
        }
        saveCartItemsToPref(items);
        adapter.updateData(items);
        loadCartProducts();
    }

    private void saveCartItemsToPref(List<CartItem> items) {
        org.json.JSONArray array = new org.json.JSONArray();
        for (CartItem item : items) {
            org.json.JSONObject obj = new org.json.JSONObject();
            try {
                obj.put("product_id", item.productId);
                obj.put("quantity", item.quantity);
            } catch (Exception ignored) {}
            array.put(obj);
        }
        SharedPrefUtils.saveString(requireContext(), "cart_items", array.toString());
    }

    // Lấy chi tiết từng sản phẩm bằng API và cập nhật Adapter
    private void loadCartProducts() {
        List<CartItem> items = getCartItemsFromPref();
        adapter.clearProductMap();
        adapter.updateData(items);

        if (items.isEmpty()) {
            binding.llCartEmpty.setVisibility(View.VISIBLE);
            binding.cartRv.setVisibility(View.GONE);
            binding.totalPriceTv.setText("0 VNĐ");
            return;
        }

        final double[] total = {0.0};
        final int[] loadedCount = {0};

        CartViewModel vm = new ViewModelProvider(this).get(CartViewModel.class);

        for (CartItem item : items) {
            vm.getProductDetail(String.valueOf(item.productId), product -> {
                // LUÔN ĐƯA VỀ UI THREAD KHI UPDATE UI!
                requireActivity().runOnUiThread(() -> {
                    if (product != null) {
                        adapter.updateProduct(item.productId, product);
                        total[0] += product.getPrice() * item.quantity;
                    }
                    loadedCount[0]++;
                    if (loadedCount[0] == items.size()) {
                        binding.totalPriceTv.setText("" + ((int)total[0]) + "Đ");
                    }
                });
            });
        }

        binding.llCartEmpty.setVisibility(View.GONE);
        binding.cartRv.setVisibility(View.VISIBLE);
    }




    // Model CartItem local
    public static class CartItem {
        public int productId;
        public int quantity;

        public CartItem(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

