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

public class RegisterActivity extends AppCompatActivity {
    private EditText inputEmail, inputPass, inputConfirm;
    private Button btnLogin;
    private TextView textLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _init();
        _setOnclick();
    }

    private void _init() {
        inputEmail = findViewById(R.id.inputEmail);
        inputPass = findViewById(R.id.inputPass);
        inputConfirm = findViewById(R.id.inputConfirm);
        btnLogin = findViewById(R.id.btnLogin);
        textLogin = findViewById(R.id.textLogin);

        progressDialog = new ProgressDialog(this);
    }

    private void _setOnclick() {
        btnLogin.setOnClickListener(view -> _login());
        textLogin.setOnClickListener(view -> finish());
    }

    private void _login() {
        String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        String confirm = inputConfirm.getText().toString().trim();

        boolean isNull = Validate.isNull(email, pass);
        boolean isEmail = Validate.isEmail(email);
        boolean isPass = Validate.isTrueFormatPass(pass);
        boolean isMatch = Validate.isMatch(pass, confirm);
        boolean isBreaking = Validate.isBreakingRegister(RegisterActivity.this, isNull, isEmail, isPass, isMatch);

        if (isBreaking) return;

        User user = new User(email, pass);
        _handleRegister(user);
    }

    private void _handleRegister(final User user) {
        progressDialog.show();
        Auth.createAccount(user);
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    boolean canCreated = Auth.isResult();
                    if (!canCreated) {
                        ShowDialog.handleShowDialog(RegisterActivity.this, Const.flagErrorDialog, "Đăng kí thất bại!");
                        return;
                    }

                    _saveUserCurrent(user);
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finishAffinity();
                }, 3000);
    }

    private void _saveUserCurrent(final User user) {
        DatabaseLocal.saveUserCurrent(RegisterActivity.this, user);
    }
}