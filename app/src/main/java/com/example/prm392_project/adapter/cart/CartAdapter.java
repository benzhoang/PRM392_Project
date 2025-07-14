package com.example.prm392_project.adapter.cart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.R;
import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.databinding.ItemCartBinding;
import com.example.prm392_project.ui.fragments.cart.CartFragment;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartFragment.CartItem> items;
    private Map<Integer, Product> productMap = new HashMap<>();
    private final CartActionListener listener;

    public interface CartActionListener {
        void onMinus(CartFragment.CartItem item);
        void onPlus(CartFragment.CartItem item);
        void onDelete(CartFragment.CartItem item);
        void onItemClick(CartFragment.CartItem item);
    }

    public CartAdapter(List<CartFragment.CartItem> items, CartActionListener listener) {
        this.items = new ArrayList<>(items);
        this.listener = listener;
    }

    // Cập nhật lại list khi quantity thay đổi
    public void updateData(List<CartFragment.CartItem> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

    // Khi product được load từ API
    public void updateProduct(int productId, Product product) {
        if (product == null) {
            Log.e("CartAdapter", "Product is null for productId: " + productId);
            // Đừng đưa null vào map, chỉ update nếu khác null!
            return;
        } else {
            Log.d("CartAdapter", "Product loaded: " + product.getName() + " (ID: " + productId + ")");
            productMap.put(productId, product);
            notifyDataSetChanged();
        }
    }


    public void clearProductMap() {
        productMap.clear();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Log.d("CartAdapter", "onBindViewHolder at " + position);
        CartFragment.CartItem cartItem = items.get(position);
        Product product = productMap.get(cartItem.productId);
        holder.bind(cartItem, product, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartFragment.CartItem cartItem, Product product, CartActionListener listener) {
            // Thông tin cơ bản
            binding.quantityTv.setText(String.valueOf(cartItem.quantity));
            binding.addBtn.setOnClickListener(v -> listener.onPlus(cartItem));
            binding.minusBtn.setOnClickListener(v -> listener.onMinus(cartItem));
            itemView.setOnClickListener(v -> listener.onItemClick(cartItem));

            if (product != null) {
                binding.tvProductName.setText(product.getName());
                binding.tvProductPrice.setText("₫ " + (int) product.getPrice());
                binding.tvProductTotalPrice.setText("Tổng: ₫ " + (int) (product.getPrice() * cartItem.quantity));
                Glide.with(binding.getRoot().getContext())
                        .load(product.getImageUrl())
                        .error(R.drawable.ic_cart_empty)  // Ảnh mặc định khi lỗi
                        .into(binding.ivProductImage);
                // ... thêm field khác nếu có
            } else {
                binding.tvProductName.setText("Đang tải...");
                binding.tvProductPrice.setText("");
                binding.tvProductTotalPrice.setText("");
                // icon mặc định nếu muốn
            }
        }
    }
}
