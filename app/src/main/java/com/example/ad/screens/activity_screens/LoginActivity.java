package com.example.ad.screens.activity_screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ad.R;
import com.example.ad.database.db.Auth;
import com.example.ad.database.db_local.DatabaseLocal;
import com.example.ad.function.Const;
import com.example.ad.function.ShowDialog;
import com.example.ad.function.Validate;
import com.example.ad.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPass;
    private Button btnLogin;
    private TextView textForgot, textRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _init();
        _setOnclick();
    }

    private void _init() {
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);
        btnLogin = findViewById(R.id.btnLogin);
        textForgot = findViewById(R.id.textForgot);
        textRegister = findViewById(R.id.textRegister);

        progressDialog = new ProgressDialog(this);
    }

    private void _setOnclick() {
        btnLogin.setOnClickListener(view -> _login());
        textRegister.setOnClickListener(view -> _register());
    }

    private void _login() {
        String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();

        boolean isNull = Validate.isNull(email, pass);
        boolean isEmail = Validate.isEmail(email);
        boolean isPass = Validate.isTrueFormatPass(pass);
        boolean isBreaking = Validate.isBreakingLogin(LoginActivity.this, isNull, isEmail, isPass);

        if (isBreaking) return;

        User user = new User(email, pass);
        _handleLogin(user);
    }

    private void _handleLogin(final User user) {
        progressDialog.show();
        Auth.handleAuthentication(user);
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    boolean isTrueUser = Auth.isResult();
                    if (!isTrueUser) {
                        ShowDialog.handleShowDialog(LoginActivity.this, Const.flagErrorDialog, "Đăng nhập thất bại!");
                        return;
                    }

                    _saveUserCurrent(user);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finishAffinity();
                }, 3000);
    }

    private void _saveUserCurrent(final User user) {
        DatabaseLocal.saveUserCurrent(LoginActivity.this, user);
    }

    private void _register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}