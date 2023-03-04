package com.example.ad.function;

import android.app.Activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static boolean isNull(String... input) {
        for (String s : input) {
            if (s.isEmpty()) return true;
        }

        return false;
    }

    public static boolean isEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(phoneNumber);
        return (m.matches());
    }

    public static boolean isTrueFormatPass(String pass) {
        return pass.length() >= 6;
    }

    public static boolean isMatch(String pass, String confirm) {
        return pass.matches(confirm);
    }

    public static boolean isDiscount(String input) {
        double discount = input.isEmpty() ? 0 : Double.parseDouble(input);

        return discount>=0 && discount <= 100;
    }

    public static boolean isPrice(String input) {
        double price = input.isEmpty() ? 0 : Double.parseDouble(input);

        return price > 0;
    }

    public static boolean isSum(String input) {
        int sum = input.isEmpty() ? 0 : Integer.parseInt(input);

        return sum > 0;
    }

    public static boolean isBreakingLogin(Activity activity , boolean... input) {
        boolean isNull = input[0];
        boolean isEmail = input[1];
        boolean isPass = input[2];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isEmail) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Email không đúng định dạng!");
            return true;
        } if (!isPass) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu phải chứa ít nhất 6 kí tự!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingRegister(Activity activity , boolean... input) {
        boolean isNull = input[0];
        boolean isEmail = input[1];
        boolean isPass = input[2];
        boolean isMatchConfirm = input[3];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isEmail) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Email không đúng định dạng!");
            return true;
        } if (!isPass) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu phải chứa ít nhất 6 kí tự!");
            return true;
        } if (!isMatchConfirm) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu không trùng khớp!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingAdd(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isPrice = var[1];
        boolean isDiscount = var[2];
        boolean isSum = var[3];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isPrice) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Giá sản phẩm không hợp lệ!");
            return true;
        } if (!isDiscount) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Triết khấu không hợp lệ!");
            return true;
        } if (!isSum) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Số lượng sản phẩm không hợp lệ!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingUpdateProfile(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isEmail = var[1];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isEmail) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Email không đúng định dạng!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingUpdatePass(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isPass = var[1];
        boolean isMatched = var[2];
        boolean isTruePassword = var[3];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isPass) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu phải chứa ít nhất 6 kí tự!");
            return true;
        } if (!isMatched) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu không trùng khớp!");
            return true;
        } if (!isTruePassword) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu cũ không đúng!");
            return true;
        }

        return false;
    }
}
