package com.example.prm392_project.ui.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.R;
import com.example.prm392_project.adapter.search.SearchAdapter;
import com.example.prm392_project.databinding.ActivitySearchBinding;
import com.example.prm392_project.ui.activities.product_detail.ProductDetailActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edge to edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        String tokenIntent = getIntent().getStringExtra("token");
        final String token = tokenIntent != null ? tokenIntent : "";

        binding.backIv.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    viewModel.searchProductsSearch(token, query, products -> {
                        if (products != null) {
                            SearchAdapter searchAdapter = new SearchAdapter();
                            searchAdapter.getDiffer().submitList(products);
                            binding.rcSearch.setAdapter(searchAdapter);

                            // show/hide empty view
                            if (products.isEmpty()) {
                                binding.llSearchEmpty.setVisibility(View.VISIBLE);
                                binding.rcSearch.setVisibility(View.GONE);
                            } else {
                                binding.llSearchEmpty.setVisibility(View.GONE);
                                binding.rcSearch.setVisibility(View.VISIBLE);
                            }

                            searchAdapter.setOnItemClickListener(product -> {
                                Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
                                intent.putExtra("id", product.getId());
                                intent.putExtra("token", token);
                                startActivity(intent);
                            });
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Nếu muốn realtime search thì handle ở đây
                return true;
            }
        });
    }
}
