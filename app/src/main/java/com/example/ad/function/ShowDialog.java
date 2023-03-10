package com.example.ad.function;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import com.example.ad.R;

public class ShowDialog {
    public static void handleShowDialog(Activity activity, int flag, String message) {
        /*
         *  flag == -1 => dialog error
         *  flag == 1 => dialog success
         * */
        if (activity == null) return;
        int layout = (flag == 1)
                ? R.layout.dialog_success
                : R.layout.dialog_error;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // init
        TextView textMessage;
        Button btnOkay;
        if (flag == 1) { // success
            textMessage = dialog.findViewById(R.id.textSuccessMessage);
            btnOkay = dialog.findViewById(R.id.btnCloseDialogSuccess);
        } else { // error
            textMessage = dialog.findViewById(R.id.textErrorMessage);
            btnOkay = dialog.findViewById(R.id.btnCloseDialogError);
        }

        textMessage.setText(message);
        btnOkay.setOnClickListener(view -> dialog.cancel());
    }

    public static Dialog getDialogWarning (Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning);
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    public static Dialog getDialogOptional(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_optional);
        Window window = dialog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }
}
