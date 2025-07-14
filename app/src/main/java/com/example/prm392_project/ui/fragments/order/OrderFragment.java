package com.example.prm392_project.ui.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.prm392_project.adapter.viewpagger2.OrderAdapter;
import com.example.prm392_project.databinding.FragmentOrderBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderAdapter = new OrderAdapter(this);
        binding.viewpager.setAdapter(orderAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Pending");
                    break;
                case 1:
                    tab.setText("Progress");
                    break;
                case 2:
                    tab.setText("Shipping");
                    break;
                case 3:
                    tab.setText("Delivered");
                    break;
                case 4:
                    tab.setText("Cancelled");
                    break;
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
