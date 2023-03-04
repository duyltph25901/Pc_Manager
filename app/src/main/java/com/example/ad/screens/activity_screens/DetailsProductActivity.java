package com.example.ad.screens.activity_screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ad.R;
import com.example.ad.adapter.view_pager.TabInfoProductAdapter;
import com.example.ad.function.Const;
import com.example.ad.function.CustomTextColor;
import com.example.ad.model.Product;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailsProductActivity extends AppCompatActivity {
    private Product mProduct;

    private ImageView imageBack, imageProduct;
    private TextView textTitle;
    private TabLayout tabBar;
    private ViewPager2 layoutContainer;
    private TextView textProductName;

    private TabInfoProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);

        mProduct = (Product) getIntent().getSerializableExtra(Const.keyProductDetails);

        _init();
        _setDefault();
        _setOnclick();
    }

    private void _init() {
        imageBack = findViewById(R.id.imageBack);
        imageProduct = findViewById(R.id.imageProduct);
        textTitle = findViewById(R.id.textDetails);
        tabBar = findViewById(R.id.tabInfo);
        layoutContainer = findViewById(R.id.layoutContainer);
        textProductName = findViewById(R.id.textProductName);

        adapter = new TabInfoProductAdapter(DetailsProductActivity.this);

        _setTextColor();

        _setTabLayout();
    }

    private void _setTabLayout() {
        layoutContainer.setAdapter(adapter);
        new TabLayoutMediator(tabBar, layoutContainer, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Thông tin sản phẩm");
                    break;
                } case 1: {
                    tab.setText("Giới thiệu sản phẩm");
                    break;
                }
            }
        }).attach();
    }

    private void _setTextColor() {
        CustomTextColor.customTextColor(textProductName,
                ContextCompat.getColor(this, R.color.color_text_start),
                ContextCompat.getColor(this, R.color.color_text_end));
        CustomTextColor.customTextColor(textTitle,
                ContextCompat.getColor(this, R.color.color_text_start),
                ContextCompat.getColor(this, R.color.color_text_end));
    }

    private void _setDefault() {
        Glide.with(DetailsProductActivity.this)
                .load(mProduct.getImage())
                .error(R.mipmap.ic_launcher)
                .into(imageProduct);
        textProductName.setText(mProduct.getName());
    }

    private void _setOnclick() {
        imageBack.setOnClickListener(view -> _handleBack());
    }

    private void _handleBack() {
        finish();
    }
}