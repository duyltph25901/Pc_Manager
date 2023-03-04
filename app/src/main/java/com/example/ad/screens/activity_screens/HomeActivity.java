package com.example.ad.screens.activity_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ad.R;
import com.example.ad.adapter.view_pager.HomeAdapter;
import com.example.ad.database.db.Auth;
import com.example.ad.function.Const;
import com.example.ad.function.ShowDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class HomeActivity extends AppCompatActivity {
    private ImageButton imageButtonCenter;
    private ConstraintLayout constraintLayout;
    private ViewPager2 layoutContainer;
    private LinearLayout menu1;
    private LinearLayout menu2;
    private LinearLayout menu3;
    private LinearLayout menu4;
    private LinearLayout menu5;
    private LinearLayout menu6;
    private LinearLayout menu7;
    private LinearLayout menu8;
    private Dialog dialogLogOut;
    private Button buttonNo, buttonYes;
    private TextView textTitle;
    private ProgressDialog progressDialog;

    private List<LinearLayout> viewList = new ArrayList<>();
    private List<String> menuTitleList = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        _init();
        _addTitle();
        _addViewList();
        _setListener();
    }

    private void _init() {
        menu1 = findViewById(R.id.layoutMenu1);
        menu2 = findViewById(R.id.layoutMenu2);
        menu3 = findViewById(R.id.layoutMenu3);
        menu6 = findViewById(R.id.layoutMenu6);
        menu8 = findViewById(R.id.layoutMenu8);
        imageButtonCenter = findViewById(R.id.imagetButtonCenter);
        constraintLayout = findViewById(R.id.constrainLayoutMenu);
        layoutContainer = findViewById(R.id.layoutContainer);

        progressDialog = new ProgressDialog(this);

        dialogLogOut = ShowDialog.getDialogWarning(HomeActivity.this);
        buttonNo = dialogLogOut.findViewById(R.id.btnNoWarningDialog);
        buttonYes = dialogLogOut.findViewById(R.id.btnYesWarningDialog);
        textTitle = dialogLogOut.findViewById(R.id.textWarningMessage);

        viewList = new ArrayList<>();
        menuTitleList = new ArrayList<>();

        _initLayoutContainer();

        constraintLayout.setVisibility(View.GONE);
    }

    private void _initLayoutContainer() {
        adapter = new HomeAdapter(this);
        layoutContainer.setUserInputEnabled(false);
        // set default
        _replaceFragment(0);
        layoutContainer.setAdapter(adapter);
    }

    private void _addTitle() {
        this.menuTitleList.add("List");
        this.menuTitleList.add("Add");
        this.menuTitleList.add("Order");
        this.menuTitleList.add("Account");
        this.menuTitleList.add("Log out");
    }

    private void _addViewList() {
        viewList.add(menu1);
        viewList.add(menu2);
        viewList.add(menu3);
        viewList.add(menu6);
        viewList.add(menu8);
    }

    private void _setListener() {
        if (imageButtonCenter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageButtonCenter");
        }

        imageButtonCenter.setOnClickListener(view -> _toggleCircleMenu());
        View.OnTouchListener listener = this._createTouchListener();
        if (constraintLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("constraintLayout");
        }

        constraintLayout.setOnTouchListener(listener);
        Iterator var3 = this.viewList.iterator();

        while(var3.hasNext()) {
            LinearLayout view = (LinearLayout)var3.next();
            view.setOnTouchListener(listener);
        }
    }

    private View.OnTouchListener _createTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = 0f;

                Integer var3 = motionEvent != null ? motionEvent.getAction() : null;
                boolean constraintLayout = false;
                if (var3 != null) {
                    if (var3 == 0) {
                        x = motionEvent.getX();
                        boolean imageButtonCenter;
                        imageButtonCenter = view instanceof LinearLayout || view instanceof ConstraintLayout;

                        return imageButtonCenter;
                    }
                }

                byte var7 = 2;
                if (var3 != null) {
                    if (var3 == var7) {
                        return true;
                    }
                }

                var7 = 1;
                if (var3 != null) {
                    if (var3 == var7) {
                        float result = motionEvent.getX() - x;
                        LinearLayout linearLayout;
                        Iterator var6;
                        if (result > (float)100) {
                            Log.d("???", "flip right");
                            var6 = viewList.iterator();

                            while(var6.hasNext()) {
                                linearLayout = (LinearLayout)var6.next();
                                _startRoute(linearLayout, false);
                            }
                        } else if (result < (float)-100) {
                            Log.d("???", "flip left");
                            var6 = viewList.iterator();

                            while(var6.hasNext()) {
                                linearLayout = (LinearLayout)var6.next();
                                _startRoute(linearLayout, true);
                            }
                        } else {
                            Log.d("???", "just touch");
                            if (view instanceof LinearLayout) {
                                _toggleClick((LinearLayout)view);
                            }
                        }

                        return false;
                    }
                }

                return false;
            }
        };
    }

    private void _startRoute(final LinearLayout currentView, boolean isLeft) {
        ViewGroup.LayoutParams imageButtonCenter = currentView.getLayoutParams();
        if (imageButtonCenter == null) {
            throw new NullPointerException("null cannot be cast to non-null type androidx.constraint layout.widget.ConstraintLayout.LayoutParams");
        } else {
            final androidx.constraintlayout.widget.ConstraintLayout.LayoutParams layoutParams = (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams)imageButtonCenter;
            float currentAngle = layoutParams.circleAngle;
            float targetAngle = currentAngle + (float)(isLeft ? -72 : 72);
            ValueAnimator angleAnimator = ValueAnimator.ofFloat(currentAngle, targetAngle);
            angleAnimator.addUpdateListener(it -> {
                Intrinsics.checkNotNullParameter(it, "it");
                Object var10001 = it.getAnimatedValue();
                if (var10001 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
                } else {
                    layoutParams.circleAngle = (Float)var10001;
                    currentView.setLayoutParams(layoutParams);
                }
            });
            Intrinsics.checkNotNullExpressionValue(angleAnimator, "angleAnimator");
            angleAnimator.setDuration(400L);
            angleAnimator.setInterpolator(new AnticipateOvershootInterpolator());
            angleAnimator.start();
        }
    }

    private void _toggleCircleMenu() {
        ConstraintLayout imageButtonCenter = this.constraintLayout;
        if (imageButtonCenter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("constraintLayout");
        }

        if (imageButtonCenter.getVisibility() == View.VISIBLE) {
            imageButtonCenter = this.constraintLayout;
            if (imageButtonCenter == null) {
                Intrinsics.throwUninitializedPropertyAccessException("constraintLayout");
            }

            imageButtonCenter.setVisibility(View.GONE);
        } else {
            imageButtonCenter = this.constraintLayout;
            if (imageButtonCenter == null) {
                Intrinsics.throwUninitializedPropertyAccessException("constraintLayout");
            }

            imageButtonCenter.setVisibility(View.VISIBLE);
        }

    }

    private void _toggleClick(LinearLayout currentView) {
        for(int i=0; i<viewList.size(); i++) {
            if (currentView.getId() == this.viewList.get(i).getId()) {
                if (i == viewList.size() - 1) {
                    _setTextDialog();
                    dialogLogOut.show();
                    buttonNo.setOnClickListener(view -> dialogLogOut.cancel());
                    buttonYes.setOnClickListener(view -> _handleLogOut());
                } else {
                    _replaceFragment(i);
                }
            }
        }
    }

    private void _setTextDialog() {
        textTitle.setText("Bạn có muốn đăng xuất?");
        buttonYes.setText("Có");
        buttonNo.setText("Không");
    }

    private void _handleLogOut() {
        progressDialog.show();
        Auth.logOutUserCurrent(HomeActivity.this);
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    if (Auth.isResult()) {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finishAffinity();
                    } else {
                        ShowDialog.handleShowDialog(
                                HomeActivity.this,
                                Const.flagErrorDialog,
                                "Đăng xuất thất bại!"
                        );
                    }
                }, 3000);
    }

    private void _replaceFragment(int i) {
        layoutContainer.setCurrentItem(i, true);
    }
}