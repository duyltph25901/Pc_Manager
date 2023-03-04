package com.example.ad.database.db_local;

import android.app.Activity;

import com.example.ad.database.db_local.Bill.BillDatabase;
import com.example.ad.database.db_local.product.ProductDatabase;
import com.example.ad.database.db_local.user.UserDatabase;
import com.example.ad.model.Bill;
import com.example.ad.model.Product;
import com.example.ad.model.User;

public class DatabaseLocal {
    public static void saveUserCurrent(Activity activity, final User user) {
        // clear table
        UserDatabase.getInstance(activity).userDAO().removeTable();
        // add user
        UserDatabase.getInstance(activity).userDAO().insert(user);
    }

    public static User getUserCurrent(Activity activity) {
        return UserDatabase.getInstance(activity).userDAO().getUserCurrent();
    }

    public static void saveProductCurrent(Activity activity, final Product product) {
        // clear table
        ProductDatabase.getInstance(activity).productDAO().removeTable();
        // add product
        ProductDatabase.getInstance(activity).productDAO().insert(product);
    }

    public static Product getProductCurrent(Activity activity) {
        return ProductDatabase.getInstance(activity).productDAO().getProductCurrent();
    }

    public static void saveBillCurrent(Activity activity, final Bill bill) {
        BillDatabase.getInstance(activity).billDAO().removeTable();
        BillDatabase.getInstance(activity).billDAO().insert(bill);
    }

    public static Bill getBillCurrent(Activity activity) {
        return BillDatabase.getInstance(activity).billDAO().getBill();
    }
}
