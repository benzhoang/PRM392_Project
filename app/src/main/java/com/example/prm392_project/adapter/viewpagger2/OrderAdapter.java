package com.example.prm392_project.adapter.viewpagger2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.prm392_project.ui.fragments.order.fragments.CancelFragment;
import com.example.prm392_project.ui.fragments.order.fragments.DeliveredFragment;
import com.example.prm392_project.ui.fragments.order.fragments.PendingFragment;
import com.example.prm392_project.ui.fragments.order.fragments.ProgressFragment;
import com.example.prm392_project.ui.fragments.order.fragments.ShipFragment;

public class OrderAdapter extends FragmentStateAdapter {

    public OrderAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return PendingFragment.newInstance("Pending");
            case 1:
                return ProgressFragment.newInstance("Processing");
            case 2:
                return ShipFragment.newInstance("Shipping");
            case 3:
                return DeliveredFragment.newInstance("Delivered");
            default:
                return CancelFragment.newInstance("Cancelled");
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
