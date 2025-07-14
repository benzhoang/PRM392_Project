package com.example.prm392_project.adapter.category;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.data.model.main.category.Category;
import com.example.prm392_project.databinding.ItemCategoryBinding;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public int selectedPosition = 0;
    public boolean isDataLoaded = false;
    private OnItemClickListener onItemClickListener;

    // DiffUtil
    private final DiffUtil.ItemCallback<Category> differCallback = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.equals(newItem);
        }
    };

    public final AsyncListDiffer<Category> differ = new AsyncListDiffer<>(this, differCallback);

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = differ.getCurrentList().get(position);
        holder.bind(category, position);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category, int position) {
            binding.categoryName.setText(category.getName());
            if (isDataLoaded) {
                if (position == selectedPosition) {
                    binding.categoryName.setTypeface(null, Typeface.BOLD);
                } else {
                    binding.categoryName.setTypeface(null, Typeface.NORMAL);
                }
            }
            binding.categoryItem.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(category);
                }
                selectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }

    // Interface click
    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
