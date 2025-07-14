package com.example.prm392_project.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.prm392_project.R;
import com.example.prm392_project.adapter.category.CategoryAdapter;
import com.example.prm392_project.adapter.product.ProductAdapter;
import com.example.prm392_project.adapter.viewpagger2.BannerAdapter;
import com.example.prm392_project.data.model.main.category.Category;
import com.example.prm392_project.databinding.FragmentHomeBinding;

import com.example.prm392_project.ui.activities.product_detail.ProductDetailActivity;
import com.example.prm392_project.ui.activities.search.SearchActivity;
import com.example.prm392_project.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

import java.util.ArrayList;
import java.util.List;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private String categoryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Banner
        List<Integer> banners = new ArrayList<>();
        banners.add(R.drawable.banner1);
        banners.add(R.drawable.banner2);
        banners.add(R.drawable.banner3);
        binding.bannerViewpager.setAdapter(new BannerAdapter(banners));
        binding.indicator.setViewPager2(binding.bannerViewpager);
        binding.productRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Token
        String token = SharedPrefUtils.getString(requireContext(), "accessToken", "");
        String bearerToken = "Bearer " + (token != null ? token : "");

        // Search bar click
        binding.searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            intent.putExtra("token", bearerToken);
            startActivity(intent);
        });

        // Get categories
        viewModel.getCategoriesHome( categories -> {
            if (categories != null) {
                CategoryAdapter categoryAdapter = new CategoryAdapter();

                // Add "All" category
                Category allCategory = new Category("0", "All");
                List<Category> allCategories = new ArrayList<>();
                allCategories.add(allCategory);
                allCategories.addAll(categories);

                categoryAdapter.differ.submitList(allCategories);
                categoryAdapter.setOnItemClickListener(category -> {
                    categoryId = category.getId();
                    if ("0".equals(categoryId)) {
                        viewModel.getProductsHome( products -> {
                            if (products != null) {
                                ProductAdapter productAdapter = new ProductAdapter();
                                productAdapter.differ.submitList(products);
                                productAdapter.setOnItemClickListener(product -> {
                                    intentToProductDetailActivity(product.getId(), bearerToken, product.getCategoryId());
                                });
                                binding.productRv.setAdapter(productAdapter);
                            }
                        });
                    } else {
                        viewModel.getProductsByCategoryHome(bearerToken, categoryId, products -> {
                            if (products != null) {
                                ProductAdapter productAdapter = new ProductAdapter();
                                productAdapter.differ.submitList(products);
                                productAdapter.setOnItemClickListener(product -> {
                                    intentToProductDetailActivity(product.getId(), bearerToken, product.getCategoryId());
                                });
                                binding.productRv.setAdapter(productAdapter);
                            }
                        });
                    }
                });
                binding.categoryRv.setAdapter(categoryAdapter);
            }
        });

        // Get all products at start
        viewModel.getProductsHome(products -> {
            requireActivity().runOnUiThread(() -> {
                if (products != null) {
                    ProductAdapter productAdapter = new ProductAdapter();
                    productAdapter.differ.submitList(products);
                    productAdapter.setOnItemClickListener(product -> {
                        intentToProductDetailActivity(product.getId(), bearerToken, product.getCategoryId());
                    });
                    binding.productRv.setAdapter(productAdapter);
                }
            });
        });
    }

    private void intentToProductDetailActivity(String id, String bearerToken, String categoryId) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("token", bearerToken);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}