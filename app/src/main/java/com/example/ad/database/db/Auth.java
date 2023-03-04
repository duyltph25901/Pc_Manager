package com.example.ad.database.db;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ad.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Auth {
    private static boolean result = false;
    private static boolean isSuccessUpdateUserName = false;
    private static boolean isSuccessUpdateEmail = false;

    public static boolean isIsSuccessUpdateUserName() {
        return isSuccessUpdateUserName;
    }

    public static void setIsSuccessUpdateUserName(boolean isSuccessUpdateUserName) {
        Auth.isSuccessUpdateUserName = isSuccessUpdateUserName;
    }

    public static boolean isIsSuccessUpdateEmail() {
        return isSuccessUpdateEmail;
    }

    public static void setIsSuccessUpdateEmail(boolean isSuccessUpdateEmail) {
        Auth.isSuccessUpdateEmail = isSuccessUpdateEmail;
    }

    public static boolean isResult() {
        return result;
    }

    private static void setResult(boolean result) {
        Auth.result = result;
    }

    public static void handleAuthentication(final User user) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPass())
                .addOnCompleteListener(task -> {
                    setResult(task.isSuccessful());
                });
    }

    public static void createAccount(final User user) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPass())
                .addOnCompleteListener(task -> {
                    setResult(task.isSuccessful());
                });
    }

    public static FirebaseUser getUserCurrent() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void handleUpdateAvatarUserCurrent(final Uri uri, final FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    setResult(task.isSuccessful());
                });
    }

    public static void handleUpdateProfileUser(String... profiles) {
        String userName = profiles[0];
        String email = profiles[1];

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            setIsSuccessUpdateUserName(false);
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        _handleUpdateEmailAddress(email, user);
                    } else {
                        setIsSuccessUpdateUserName(false);
                    }
                });
    }

    private static void _handleUpdateEmailAddress(String email, final FirebaseUser user) {
        user.updateEmail(email)
                .addOnCompleteListener(task -> {
                    setIsSuccessUpdateEmail(task.isSuccessful());
                });
    }

    public static void handleUpdatePassword(String pass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            setResult(false);
            return;
        }
        user.updatePassword(pass)
                .addOnCompleteListener(task -> setResult(task.isSuccessful()));

    }

    public static void logOutUserCurrent(Activity activity) {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(task -> setResult(task.isSuccessful()));
    }
}
