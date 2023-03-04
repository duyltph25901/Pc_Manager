package com.example.ad.adapter.view_pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ad.screens.fragment_screens.home_screens.AccountFragment;
import com.example.ad.screens.fragment_screens.home_screens.AddFragment;
import com.example.ad.screens.fragment_screens.home_screens.AnalyticsFragment;
import com.example.ad.screens.fragment_screens.home_screens.ChatFragment;
import com.example.ad.screens.fragment_screens.home_screens.ListFragment;
import com.example.ad.screens.fragment_screens.home_screens.OrderFragment;
import com.example.ad.screens.fragment_screens.home_screens.SettingFragment;

public class HomeAdapter extends FragmentStateAdapter {
    private static final int fragmentSum = 4;

    public HomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new ListFragment();
            } case 1: {
                return new AddFragment();
            } case 2: {
                return new OrderFragment();
            } case 3: {
                return new AccountFragment();
            } default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return fragmentSum;
    }
}
