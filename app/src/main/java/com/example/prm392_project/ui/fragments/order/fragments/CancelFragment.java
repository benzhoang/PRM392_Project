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
import com.example.prm392_project.databinding.FragmentCancelBinding;
import com.example.prm392_project.ui.activities.order_detail.OrderDetailActivity;
import com.example.prm392_project.ui.fragments.cart.CartViewModel;
import com.example.prm392_project.ui.fragments.order.OrderViewModel;
import com.example.prm392_project.util.SharedPrefUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CancelFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String param1;
    private FragmentCancelBinding binding;
    private OrderViewModel viewModel;
    private CartViewModel cartViewModel;

    public CancelFragment() {
        // Required empty public constructor
    }

    public static CancelFragment newInstance(String param1) {
        CancelFragment fragment = new CancelFragment();
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
        // ViewModels are initialized in onViewCreated with ViewModelProvider (Java style)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCancelBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        String token = SharedPrefUtils.getString(requireContext(), "accessToken", "");
        String bearerToken = "Bearer " + token;

        viewModel.getOrders("Cancelled", orders -> {
            if (orders == null || orders.isEmpty()) {
                binding.emptyOrderLayout.setVisibility(View.VISIBLE);
                binding.orderRecyclerView.setVisibility(View.GONE);
            } else {
                binding.emptyOrderLayout.setVisibility(View.GONE);
                binding.orderRecyclerView.setVisibility(View.VISIBLE);
                binding.orderRecyclerView.setHasFixedSize(true);
                OrderTotalAdapter adapter = new OrderTotalAdapter(cartViewModel, bearerToken);
                adapter.differ.submitList(reverseList(orders));
                adapter.setOnItemClickListener(order -> {
                    Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
                    intent.putExtra("orderId", order.getId());
                    startActivity(intent);
                });
                binding.orderRecyclerView.setAdapter(adapter);
            }
        });
    }

    // Helper function to reverse list (because Kotlin orders.reversed())
    private <T> java.util.List<T> reverseList(java.util.List<T> list) {
        java.util.List<T> reversed = new java.util.ArrayList<>(list);
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
