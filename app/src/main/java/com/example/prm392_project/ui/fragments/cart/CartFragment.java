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
import com.example.prm392_project.ui.activities.product_detail.ProductDetailActivity;
import com.example.prm392_project.util.SharedPrefUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartViewModel viewModel;
    private String total = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        String token = SharedPrefUtils.getString(requireContext(), "accessToken", "");
        String bearerToken = "Bearer " + token;
        String userId = SharedPrefUtils.getString(requireContext(), "id", "");

        binding.paymentBtn.setOnClickListener(v -> {
            if (!total.isEmpty()) {
                try {
                    if (Double.parseDouble(total) > 0) {
                        Intent intent = new Intent(requireContext(), CheckoutActivity.class);
                        intent.putExtra("token", bearerToken);
                        intent.putExtra("total", total);
                        startActivity(intent);
                    } else {
                        Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Invalid total value", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getAllCarts(bearerToken, userId);
    }

    private void handleItemClick(Cart cart, String bearerToken) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra("id", cart.getItems().getProduct());
        intent.putExtra("token", bearerToken);
        startActivity(intent);
    }

    private void handleQuantityChange(Cart cart, String bearerToken, String userId, int change) {
        int newQuantity = cart.getItems().getQuantity() + change;
        viewModel.updateQuantity(bearerToken, cart.getId(),
                java.util.Collections.singletonMap("quantity", newQuantity));
    }

    private void handleItemDeletion(Cart cart, String bearerToken, String userId) {
        viewModel.deleteCart(bearerToken, cart.getId());
    }


    private void refreshCartList(String bearerToken, String userId) {
        viewModel.getAllCarts(bearerToken, userId);
    }

    private void updateUI(List<Cart> carts) {
        boolean isEmpty = carts.isEmpty();
        binding.llCartEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.cartRv.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        double totalPrice = 0;
        for (Cart cart : carts) {
            totalPrice += cart.getItems().getPrice() * cart.getItems().getQuantity();
        }
        total = String.valueOf(totalPrice);
        binding.totalPriceTv.setText("$: " + total);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
