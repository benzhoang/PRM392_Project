package com.example.prm392_project.adapter.order;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_project.data.model.main.order.Order;
import com.example.prm392_project.databinding.ItemSumTotalBinding;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderTotalAdapter extends RecyclerView.Adapter<OrderTotalAdapter.OrderTotalViewHolder> {

    private final CartViewModel viewModel;
    private final String token;

    public OrderTotalAdapter(CartViewModel viewModel, String token) {
        this.viewModel = viewModel;
        this.token = token;
    }

    private final DiffUtil.ItemCallback<Order> differCallback = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.equals(newItem);
        }
    };

    public final AsyncListDiffer<Order> differ = new AsyncListDiffer<>(this, differCallback);

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public OrderTotalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSumTotalBinding binding = ItemSumTotalBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new OrderTotalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderTotalViewHolder holder, int position) {
        Order order = differ.getCurrentList().get(position);
        holder.bind(order);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class OrderTotalViewHolder extends RecyclerView.ViewHolder {
        private final ItemSumTotalBinding binding;

        public OrderTotalViewHolder(ItemSumTotalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Order order) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = "";
            try {
                Date date = originalFormat.parse(order.getCreatedAt());
                if (date != null) {
                    formattedDate = targetFormat.format(date);
                }
            } catch (ParseException e) {
                // Nếu lỗi format, cứ để trống
            }
            binding.tvDay.setText(formattedDate);

            String formattedPrice = NumberFormat.getNumberInstance(Locale.US)
                    .format(order.getTotal());
            binding.tvTotalPrice.setText(formattedPrice + " $");

            binding.itemOrderRecyclerView.setHasFixedSize(true);

            OrderItemAdapter adapter = new OrderItemAdapter(viewModel, token);
            binding.itemOrderRecyclerView.setAdapter(adapter);
            adapter.differ.submitList(order.getProducts());
        }
    }
}
