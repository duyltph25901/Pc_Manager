package com.example.ad.screens.activity_screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.example.ad.R;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ramotion.paperonboarding.R.layout.onboarding_main_layout);

        PaperOnboardingEngine engine = new PaperOnboardingEngine(
                findViewById(com.ramotion.paperonboarding.R.id.onboardingRootView),
                _getPaperOnBoardingPageData(),
                OnBoardingActivity.this
        );

        engine.setOnRightOutListener(() -> {
            startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
            finishAffinity();
        });
    }

    private ArrayList<PaperOnboardingPage> _getPaperOnBoardingPageData() {
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Laptop",
                "Nâng tầm trải nghiệm, đón đầu công nghệ",
                Color.parseColor("#678FB4"), R.drawable.icon_imac, R.drawable.icon_search_onboarding);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Vận chuyển",
                "Giao hàng thần tốc, tiết kiệm thời gian",
                Color.parseColor("#65B0B4"), R.drawable.icon_ship, R.drawable.icon_location);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Thanh toán",
                "Chất lượng đỉnh cao, giá cả tốt nhất",
                Color.parseColor("#9B90BC"), R.drawable.icon_pay, R.drawable.icon_wallet);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

        return elements;
    }
}