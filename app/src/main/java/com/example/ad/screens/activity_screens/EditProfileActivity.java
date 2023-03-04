package com.example.ad.screens.activity_screens;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ad.R;
import com.example.ad.database.db.Auth;
import com.example.ad.function.Const;
import com.example.ad.function.ShowDialog;
import com.example.ad.function.Validate;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView imageX;
    private TextView textTitle;
    private CircleImageView avatarUser;
    private EditText inputUserName, inputEmail;
    private Button buttonSaveUpdate;
    private FirebaseUser user;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        _init();
        _setData();
        _setOnclick();
    }

    private void _init() {
        imageX = findViewById(R.id.imageX);
        textTitle = findViewById(R.id.textTitle);
        avatarUser = findViewById(R.id.avatarUser);
        inputUserName = findViewById(R.id.inputUserName);
        buttonSaveUpdate = findViewById(R.id.buttonSaveUpdateProfile);
        inputEmail = findViewById(R.id.inputEmail);
        progressDialog = new ProgressDialog(this);

        user = Auth.getUserCurrent();
    }

    private void _setData() {
        String displayName = user.getDisplayName();
        String email = user.getEmail();
        Uri avatar = user.getPhotoUrl();

        displayName = (displayName == null || displayName.isEmpty()) ? "null" : displayName;

        // handle set default data
        inputUserName.setText(displayName);
        inputEmail.setText(email);
        if (avatar != null) {
            Glide.with(this)
                    .load(avatar)
                    .error(R.drawable.icon_user_default)
                    .into(avatarUser);
        } else {
            avatarUser.setImageResource(R.drawable.icon_user_default);
        }
    }

    private void _setOnclick() {
        imageX.setOnClickListener(view -> finish());
        buttonSaveUpdate.setOnClickListener(view -> _handleUpdate());
    }

    private void _handleUpdate() {
        String userName = inputUserName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        boolean isNull
                = Validate.isNull(userName, email)
                || userName.matches("null");
        boolean isEmail = Validate.isEmail(email);
        boolean isBreaking = Validate.isBreakingUpdateProfile(this, isNull, isEmail);
        if (isBreaking) return;


        Auth.handleUpdateProfileUser(userName, email);
        progressDialog.show();
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    if (!Auth.isIsSuccessUpdateUserName()) {
                        if (!Auth.isIsSuccessUpdateEmail()) {
                            ShowDialog.handleShowDialog(
                                    this,
                                    Const.flagErrorDialog,
                                    "Cập nhật email không thành công!"
                            );
                        } else {
                            ShowDialog.handleShowDialog(
                                    this,
                                    Const.flagSuccessDialog,
                                    "Cập nhật profile thành công!"
                            );
                        }
                    } else {
                        ShowDialog.handleShowDialog(
                                this,
                                Const.flagErrorDialog,
                                "Cập nhật profile không thành công!"
                        );
                    }
                }, 3000);
    }
}