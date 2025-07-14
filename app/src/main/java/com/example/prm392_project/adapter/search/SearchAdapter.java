package com.example.prm392_project.adapter.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.data.model.main.product.Product;
import com.example.prm392_project.databinding.ItemSearchProductBinding;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final AsyncListDiffer<Product> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public AsyncListDiffer<Product> getDiffer() {
        return differ;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchProductBinding binding = ItemSearchProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
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

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchProductBinding binding;

        public SearchViewHolder(ItemSearchProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.productNameTv.setText(product.getName());
            binding.productPriceTv.setText(product.getPrice() + " USD");
            if (product.getCategoryId() != null && product.getCategoryId() != null) {
                binding.productCategoryTv.setText(product.getCategoryId());
            } else {
                binding.productCategoryTv.setText("");
            }
            Glide.with(binding.getRoot()).load(product.getImageUrl()).into(binding.ivProduct);
        }
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };
}
