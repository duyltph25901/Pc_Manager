package com.example.ad.screens.activity_screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ad.R;
import com.example.ad.adapter.view_pager.DetailsOrderAdapter;
import com.example.ad.function.Const;
import com.example.ad.function.transform_viewpager.ZoomOutPageTransformer;
import com.example.ad.model.Order;
import com.example.ad.model.Product;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class DetailsOrderActivity extends AppCompatActivity {
    private Order mOrder;
    private List<Product> products;

    private TextView txtIdOrder;
    private TabLayout tabLayout;
    private ViewPager2 layoutContainer;

    private DetailsOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);

        _init();
        _getKey();

        _setData();
    }

    private void _init() {
        products = new ArrayList<>();
        adapter =  new DetailsOrderAdapter(DetailsOrderActivity.this);

        txtIdOrder = findViewById(R.id.textIdOrder);
        tabLayout = findViewById(R.id.tabBill);
        layoutContainer = findViewById(R.id.layoutContainerBill);

        layoutContainer.setAdapter(adapter);
        layoutContainer.setPageTransformer(new ZoomOutPageTransformer());
        new TabLayoutMediator(tabLayout, layoutContainer, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Thông tin khách hàng");
                    break;
                } case 1: {
                    tab.setText("Thông tin đơn hàng");
                    break;
                } case 2: {
                    tab.setText("Danh sách sản phẩm");
                    break;
                }
            }
        }).attach();
    }

    private void _getKey() {
        mOrder = (Order) getIntent().getSerializableExtra(Const.keyOrderDetails);
    }

    private void _setData() {
        txtIdOrder.setText(mOrder.getId());
    }
}