package com.example.prm392_project.adapter.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.databinding.ItemProductBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            double price = product.getPrice(); // hoặc nếu price là String thì: Double.parseDouble(product.getPrice())
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String priceVN = formatter.format(price) + "VNĐ";
            binding.CosmeticName.setText(product.getName());
            binding.CosmeticPrice.setText(priceVN);
            binding.CosmeticCategory.setText(product.getCategoryId());
            Glide.with(binding.getRoot()).load(product.getImageUrl()).into(binding.CosmeticImage);
        }
    }

    private final DiffUtil.ItemCallback<Product> differCallback = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };

    public final AsyncListDiffer<Product> differ = new AsyncListDiffer<>(this, differCallback);

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = differ.getCurrentList().get(position);
        holder.bind(product);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }
}
