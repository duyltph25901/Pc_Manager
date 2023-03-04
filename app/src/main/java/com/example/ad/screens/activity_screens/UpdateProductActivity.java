package com.example.ad.screens.activity_screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ad.R;
import com.example.ad.database.db.Realtime;
import com.example.ad.function.Const;
import com.example.ad.function.Validate;
import com.example.ad.model.Product;

import java.io.IOException;

public class UpdateProductActivity extends AppCompatActivity {

    private Product mProduct;
    private ImageView imageBack;
    private TextView textProductName;
    private EditText inputNewPrice, inputNewDiscount, inputNewSum;
    private Button buttonUpdate;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        _init();
        _setData();
        _setOnclick();
    }

    private void _init() {
        imageBack = findViewById(R.id.imageBack);
        textProductName = findViewById(R.id.textProductName);
        inputNewPrice = findViewById(R.id.inputPriceProduct);
        inputNewDiscount = findViewById(R.id.inputDiscount);
        inputNewSum = findViewById(R.id.inputSum);
        buttonUpdate = findViewById(R.id.btnUpdateProduct);
        progressDialog = new ProgressDialog(this);

        mProduct = (Product) getIntent().getSerializableExtra(Const.keyProductUpdate);
    }

    private void _setData() {
        textProductName.setText(mProduct.getName());
        inputNewPrice.setText(mProduct.getPrice() + "");
        inputNewDiscount.setText(mProduct.getDiscount() + "");
        inputNewSum.setText(mProduct.getSum() + "");
    }

    private void _setOnclick() {
        buttonUpdate.setOnClickListener(view -> _update());
        imageBack.setOnClickListener(view -> finish());
    }

    private void _update() {
        String price = inputNewPrice.getText().toString().trim();
        String discount = inputNewDiscount.getText().toString().trim();
        String sum = inputNewSum.getText().toString().trim();

        boolean isNull = Validate.isNull(price, discount, sum);
        boolean isDiscount = Validate.isDiscount(discount);
        boolean isPrice = Validate.isPrice(price);
        boolean isSum = Validate.isSum(sum);
        boolean isBreaking = Validate.isBreakingAdd(UpdateProductActivity.this, isNull, isPrice, isDiscount, isSum);
        if (isBreaking) return;

        _handleUpdate(price, discount, sum);
    }

    private void _handleUpdate(String... input) {
        progressDialog.show();
        double price = Double.parseDouble(input[0]);
        int discount = Integer.parseInt(input[1]);
        int sum = Integer.parseInt(input[2]);

        mProduct.setPrice(price);
        mProduct.setDiscount(discount);
        mProduct.setSum(sum);

        Realtime.updateProduct(mProduct);
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    finish();
                }, 3000);
    }
}