package com.example.ad.adapter.view_pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ad.screens.fragment_screens.detail_ordered_screens.CustomerInfoFragment;
import com.example.ad.screens.fragment_screens.detail_ordered_screens.ListProductOrderedFragment;
import com.example.ad.screens.fragment_screens.detail_ordered_screens.OrderedInfoFragment;

public class DetailsOrderAdapter extends FragmentStateAdapter {
    public DetailsOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new CustomerInfoFragment();
            } case 1: {
                return new OrderedInfoFragment();
            } case 2: {
                return new ListProductOrderedFragment();
            } default: {
                return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
