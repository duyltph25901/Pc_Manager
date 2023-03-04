package com.example.ad.adapter.view_pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ad.screens.fragment_screens.details_product_screens.InfoProductFragment;
import com.example.ad.screens.fragment_screens.details_product_screens.IntroduceProductFragment;

public class TabInfoProductAdapter extends FragmentStateAdapter {
    public TabInfoProductAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new InfoProductFragment();
            } case 1: {
                return new IntroduceProductFragment();
            } default: {
                return new InfoProductFragment();
            }
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
