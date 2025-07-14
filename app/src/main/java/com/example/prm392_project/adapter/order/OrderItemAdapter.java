package com.example.prm392_project.adapter.order;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.prm392_project.data.model.main.order.OrderProduct;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.databinding.ItemOrderBinding;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {

    private final CartViewModel viewModel;
    private final String token;

    public OrderItemAdapter(CartViewModel viewModel, String token) {
        this.viewModel = viewModel;
        this.token = token;
    }

    private final DiffUtil.ItemCallback<OrderProduct> differCallback = new DiffUtil.ItemCallback<OrderProduct>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderProduct oldItem, @NonNull OrderProduct newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrderProduct oldItem, @NonNull OrderProduct newItem) {
            return oldItem.equals(newItem);
        }
    };

    public final AsyncListDiffer<OrderProduct> differ = new AsyncListDiffer<>(this, differCallback);

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(OrderProduct orderProduct);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderProduct orderProduct = differ.getCurrentList().get(position);
        String productId = orderProduct.getProductId();
        int quantity = orderProduct.getQuantity();
        // Dùng callback vì dữ liệu product là async từ viewModel (Kotlin cũng vậy)
        viewModel.getProductDetail( productId, product -> {
            if (product != null) {
                holder.bind(product, quantity);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(orderProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product, int quantity) {
            binding.tvQuantityItemOrder.setText("Quantity: " + quantity);
            Glide.with(binding.getRoot().getContext())
                    .load(product.getImageUrl())
                    .into(binding.ivItemOrder);
            binding.tvNameItemOrder.setText(product.getName());
            binding.tvCategoryItemOrder.setText(product.getCategoryId());
            String formattedPrice = NumberFormat.getNumberInstance(Locale.US)
                    .format((int) product.getPrice());
            binding.tvPriceItemOrder.setText("Price: " + formattedPrice + " $");
        }
    }
}
