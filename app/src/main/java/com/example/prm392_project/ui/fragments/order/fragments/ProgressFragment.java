package com.example.prm392_project.ui.fragments.order.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_project.adapter.order.OrderTotalAdapter;
import com.example.prm392_project.databinding.FragmentProgressBinding;
import com.example.prm392_project.ui.activities.order_detail.OrderDetailActivity;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;
import com.example.prm392_project.ui.fragments.order.OrderViewModel;
import com.example.prm392_project.util.SharedPrefUtils;

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProgressFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String param1;
    private FragmentProgressBinding binding;
    private OrderViewModel orderViewModel;
    private CartViewModel cartViewModel;

    public static ProgressFragment newInstance(String param1) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        String token = SharedPrefUtils.getString(requireContext(), "accessToken", "");
        String bearerToken = "Bearer " + token;

        orderViewModel.getOrders("Processing", new OrderViewModel.OrdersCallback() {
            @Override
            public void onResult(List<com.example.prm392_project.data.model.main.order.Order> orders) {
                if (orders == null || orders.isEmpty()) {
                    binding.emptyOrderLayout.setVisibility(View.VISIBLE);
                    binding.orderRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.emptyOrderLayout.setVisibility(View.GONE);
                    binding.orderRecyclerView.setVisibility(View.VISIBLE);
                    binding.orderRecyclerView.setHasFixedSize(true);

                    OrderTotalAdapter adapter = new OrderTotalAdapter(cartViewModel, bearerToken);
                    Collections.reverse(orders);
                    adapter.differ.submitList(orders);

                    adapter.setOnItemClickListener(order -> {
                        Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
                        intent.putExtra("orderId", order.getId());
                        startActivity(intent);
                    });

                    binding.orderRecyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
