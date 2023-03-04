package com.example.ad.screens.fragment_screens.home_screens;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ad.R;
import com.example.ad.database.db.Auth;
import com.example.ad.function.Const;
import com.example.ad.function.CustomTextColor;
import com.example.ad.function.ShowDialog;
import com.example.ad.screens.activity_screens.EditPasswordActivity;
import com.example.ad.screens.activity_screens.EditProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private ImageView imageBackground;
    private CircleImageView avatarUser;
    private TextView textUserName, textUserName2, textEmail;
    private Button buttonEditProfile;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private Dialog dialogOption;
    private TextView textTitle;
    private Button buttonUpdateProfile, buttonUpdatePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        _init();
        _randomImageBackground();
        _setDataShow();
        _setOnClick();

        return view;
    }

    private void _init() {
        // get user current
        user = Auth.getUserCurrent();

        imageBackground = view.findViewById(R.id.imageBackground);
        avatarUser = view.findViewById(R.id.avatarUser);
        textUserName = view.findViewById(R.id.textUserName);
        textUserName2 = view.findViewById(R.id.textUserName2);
        textEmail = view.findViewById(R.id.textEmailUser);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        progressDialog = new ProgressDialog(getActivity());

        dialogOption = ShowDialog.getDialogOptional(getActivity());
        buttonUpdateProfile = dialogOption.findViewById(R.id.btnYesWarningDialog);
        buttonUpdatePass = dialogOption.findViewById(R.id.btnNoWarningDialog);
        textTitle = dialogOption.findViewById(R.id.textWarningMessage);

        CustomTextColor.customTextColor(textUserName,
                ContextCompat.getColor(requireContext(), R.color.color_text_gradient_start),
                ContextCompat.getColor(requireContext(), R.color.color_text_gradient_end));
    }

    private void _randomImageBackground() {
        int[] images = {R.drawable.image_city, R.drawable.image_forest,
                R.drawable.image_mountain, R.drawable.image_sky,
                R.drawable.image_sea, R.drawable.image_sunset,
                R.drawable.image_desert};
        Random random = new Random();
        int result = images[random.nextInt(images.length)];
        imageBackground.setImageResource(result);
    }

    private void _setDataShow() {
        Uri avatarUser = user.getPhotoUrl();
        String userName = user.getDisplayName();
        String email = user.getEmail();

        userName = (userName == null) ? "null" : userName;

        Log.d("CheckAvatarUser", (avatarUser)+"");
        if (avatarUser != null) {
            Glide.with(requireActivity())
                    .load(avatarUser)
                    .error(R.drawable.icon_user_default)
                    .into(this.avatarUser);
        } else {
            this.avatarUser.setImageResource(R.drawable.icon_user_default);
        }
        if (userName == null) {
            textUserName.setVisibility(View.GONE);
        } else {
            textUserName.setVisibility(View.VISIBLE);
            textUserName.setText(userName);
        }
        textUserName2.setText(userName);
        textEmail.setText(email);
    }

    private void _setOnClick() {
        buttonEditProfile.setOnClickListener(view -> _openDialog());
        avatarUser.setOnClickListener(view -> _openGallery());
        buttonUpdateProfile.setOnClickListener(view -> _updateProfile());
        buttonUpdatePass.setOnClickListener(view -> _updatePass());
    }

    private void _openDialog() {
        _setText();
        dialogOption.show();
    }

    private void _updateProfile() {
        startActivity(new Intent(getActivity(),EditProfileActivity.class));
        dialogOption.cancel();
    }

    private void _updatePass() {
        startActivity(new Intent(getActivity(), EditPasswordActivity.class));
    }

    private void _setText() {
        textTitle.setText("Vui lòng chọn chức năng");
        buttonUpdateProfile.setText("Cập nhât thông tin");
        buttonUpdatePass.setText("Cập nhât mật khẩu");
    }

    private void _openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    final private ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    progressDialog.show();

                    Intent i = result.getData();
                    if (i == null) return;
                    Uri uri = i.getData();

                    Auth.handleUpdateAvatarUserCurrent(uri, user);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            if (Auth.isResult()) {
                                Glide.with(requireActivity()) // show image after chosen
                                        .load(uri)
                                        .error(R.mipmap.ic_launcher)
                                        .into(avatarUser);
                                ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Bạn đã thay đổi ảnh đại diện!");
                            } else {
                                ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Có lỗi trong quá trình xử lý!");
                            }
                        }
                    }, 3000);
                }
            });

    @Override
    public void onResume() {
        super.onResume();

        dialogOption.cancel();
        _setDataShow();
        _randomImageBackground();
    }
}