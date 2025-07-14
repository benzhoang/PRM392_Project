package com.example.prm392_project.adapter.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.data.model.main.cart.Cart;
import com.example.prm392_project.databinding.ItemCartBinding;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final CartViewModel viewModel;
    private final String token;

    public CartAdapter(CartViewModel viewModel, String token) {
        this.viewModel = viewModel;
        this.token = token;
    }

    public interface OnItemClickListener {
        void onClick(Cart cart);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemClickListener onMinusClickListener;
    private OnItemClickListener onPlusClickListener;
    private OnItemClickListener onDeleteListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setOnMinusClickListener(OnItemClickListener listener) {
        this.onMinusClickListener = listener;
    }
    public void setOnPlusClickListener(OnItemClickListener listener) {
        this.onPlusClickListener = listener;
    }
    public void setOnDeleteListener(OnItemClickListener listener) {
        this.onDeleteListener = listener;
    }

    private final DiffUtil.ItemCallback<Cart> differCallback = new DiffUtil.ItemCallback<Cart>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
        @Override
        public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.equals(newItem);
        }
    };

    public AsyncListDiffer<Cart> differ = new AsyncListDiffer<>(this, differCallback);

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = differ.getCurrentList().get(position);
        holder.bind(cart);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onClick(cart);
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Cart cart) {
            final int[] productStock = {0}; // Use array for mutable in lambda

            viewModel.getProductDetail( cart.getItems().getProduct(), it -> {
                if (it != null) {
                    Glide.with(itemView.getContext())
                            .load(it.getImageUrl())      // <-- đúng getter với field hiện tại
                            .into(binding.ivProductImage);
                    binding.tvProductName.setText(it.getName());
                    binding.tvProductPrice.setText("$ " + it.getPrice());
                    binding.tvProductSize.setText("Size " + cart.getItems().getSize());
                    binding.tvProductTotalPrice.setText("Total price: " + (it.getPrice() * cart.getItems().getQuantity()));

                }
            });

            binding.quantityTv.setText(String.valueOf(cart.getItems().getQuantity()));

            binding.minusBtn.setOnClickListener(v -> {
                if (cart.getItems().getQuantity() > 1) {
                    binding.quantityTv.setText(String.valueOf(cart.getItems().getQuantity()));
                    if (onMinusClickListener != null) onMinusClickListener.onClick(cart);
                } else {
                    cart.getItems().setQuantity(1);
                    binding.quantityTv.setText("1");
                    if (onDeleteListener != null) onDeleteListener.onClick(cart);
                }
            });

            binding.addBtn.setOnClickListener(v -> {
                if (cart.getItems().getQuantity() < productStock[0]) {
                    binding.quantityTv.setText(String.valueOf(cart.getItems().getQuantity()));
                    if (onPlusClickListener != null) onPlusClickListener.onClick(cart);
                } else {
                    Toast.makeText(binding.getRoot().getContext(), "Product stock is not enough", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
