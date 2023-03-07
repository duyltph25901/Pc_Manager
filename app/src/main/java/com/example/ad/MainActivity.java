package com.example.ad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ad.database.db.Realtime;
import com.example.ad.function.CustomTextColor;
import com.example.ad.function.first_install_app.DataLocalManagement;
import com.example.ad.screens.activity_screens.HomeActivity;
import com.example.ad.screens.activity_screens.LoginActivity;
import com.example.ad.screens.activity_screens.OnBoardingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ImageView imageLogo;
    private TextView textShopName;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _init();
        _initDatabase();
        _startAnimation();

        // custom text color
        CustomTextColor.customTextColor(textShopName,
                ContextCompat.getColor(this, R.color.color_text_start),
                ContextCompat.getColor(this, R.color.color_text_end));

        // handle action
        _checkUseCase();
    }

    private void _init() {
        imageLogo = findViewById(R.id.imageLogo);
        textShopName = findViewById(R.id.textShopName);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void _initDatabase() {
        Realtime.getDatabaseOrder();
        Realtime.getDatabaseProduct("");
        Realtime.getDatabaseCart();
    }

    private void _startAnimation() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                // this == run
                _handleStartAnimation(this);
            }
        };

        _handleStartAnimation(run);
    }

    private void _handleStartAnimation(Runnable run) {
        imageLogo.animate().rotationBy(360)
                .withEndAction(run).setDuration(10000)
                .setInterpolator(new LinearInterpolator())
                .start();
    }

    private void _checkUseCase() {
        if (!DataLocalManagement.getFirstInstall()) {
            DataLocalManagement.setFirstInstall(true);
            _navigationScreen(OnBoardingActivity.class);
        } else {
            if (user == null) {
                _navigationScreen(LoginActivity.class);
            } else {
                _navigationScreen(HomeActivity.class);
            }
        }
    }

    private void _navigationScreen(final Class className) {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, className));
            finishAffinity();
        }, 5000);
    }
}