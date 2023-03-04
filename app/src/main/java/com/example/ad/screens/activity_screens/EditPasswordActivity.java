package com.example.ad.screens.activity_screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ad.R;
import com.example.ad.database.db.Auth;
import com.example.ad.database.db_local.user.UserDatabase;
import com.example.ad.function.Const;
import com.example.ad.function.ShowDialog;
import com.example.ad.function.Validate;

public class EditPasswordActivity extends AppCompatActivity {
    private ImageView imageX;
    private TextView textTitle, textShowHidePass;
    private EditText inputCurrentPass, inputNewPass, inputConfirmPass;
    private Button buttonUpdate;
    private ProgressDialog progressDialog;

    private int currentHideShow = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        _init();
        _setOnclick();
    }

    private void _init() {
        imageX = findViewById(R.id.imageX);
        textTitle = findViewById(R.id.textTitle);
        textShowHidePass = findViewById(R.id.textHideShow);
        inputCurrentPass = findViewById(R.id.inputPassCurrent);
        inputNewPass = findViewById(R.id.inputNewPass);
        inputConfirmPass = findViewById(R.id.inputPassConfirm);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        progressDialog = new ProgressDialog(this);
    }

    private void _setOnclick() {
        buttonUpdate.setOnClickListener(view -> _update());
        imageX.setOnClickListener(view -> finish());
        textShowHidePass.setOnClickListener(view -> _handleShowHidePass());
    }

    private void _update() {
        String currentPass = inputCurrentPass.getText().toString().trim();
        String newPass = inputNewPass.getText().toString().trim();
        String confirmPass = inputConfirmPass.getText().toString().trim();

        boolean isNull = Validate.isNull(currentPass, newPass, confirmPass);
        boolean isPass = Validate.isTrueFormatPass(newPass);
        boolean isMatchPass = Validate.isMatch(newPass, confirmPass);
        boolean isTruePassword = currentPass.matches(getPassCurrent());
        boolean isBreaking = Validate.isBreakingUpdatePass(EditPasswordActivity.this, isNull, isPass, isMatchPass, isTruePassword);

        if (isBreaking) return;

        _handleUpdate(newPass);
    }

    private void _handleUpdate(String newPass) {
        progressDialog.show();
        Auth.handleUpdatePassword(newPass);
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    if (Auth.isResult()) {
                        ShowDialog.handleShowDialog(
                                EditPasswordActivity.this,
                                Const.flagSuccessDialog,
                                "Cập nhật thành công!"
                        );
                        // disable onclick button
                        buttonUpdate.setEnabled(false);
                        imageX.setEnabled(false);
                        Toast.makeText(this, "Hệ thống sẽ tự đăng xuất sau 5 giây nữa!!!", Toast.LENGTH_SHORT).show();
                        _logOutInFiveSeconds();
                    } else {
                        ShowDialog.handleShowDialog(
                                EditPasswordActivity.this,
                                Const.flagErrorDialog,
                                "Cập nhật không thành công!"
                        );
                    }
                }, 3000);
    }
    
    private void _logOutInFiveSeconds() {
        new Handler()
                .postDelayed(() -> {
                    Auth.logOutUserCurrent(EditPasswordActivity.this);
                    startActivity(new Intent(EditPasswordActivity.this, LoginActivity.class));
                    finishAffinity();
                }, 5000);
    }

    private void _handleShowHidePass() {
        ++currentHideShow;
        /*
        *   currentHideShow == 1 => hide
        *   currentHideShow == 2 => show
        * */
        if (currentHideShow % 2 == 0) {
            inputCurrentPass.setInputType(InputType.TYPE_CLASS_TEXT);
            textShowHidePass.setText("Ẩn");
        } else {
            inputCurrentPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            textShowHidePass.setText("Hiện");
        }
    }

    private String getPassCurrent() {
        return UserDatabase.getInstance(EditPasswordActivity.this).userDAO().getUserCurrent().getPass();
    }
}