package com.example.ad.database.db_local.product;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ad.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {
    private static final String databaseName = "productCurrent.db";
    private static ProductDatabase instance;

    public static synchronized ProductDatabase getInstance(Context context) {
        return (instance == null)
                ? Room.databaseBuilder(context.getApplicationContext(), ProductDatabase.class, databaseName)
                .allowMainThreadQueries().build()
                : instance;
    }

    public abstract ProductDAO productDAO();
}
