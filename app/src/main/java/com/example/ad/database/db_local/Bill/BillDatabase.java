package com.example.ad.database.db_local.Bill;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ad.model.Bill;

@Database(entities = {Bill.class}, version = 1, exportSchema = false)
public abstract class BillDatabase extends RoomDatabase {
    private static final String databaseName = "Bill.db";
    private static BillDatabase instance;

    public static synchronized BillDatabase getInstance(Context context) {
        return (instance == null)
                ? Room.databaseBuilder(context.getApplicationContext(), BillDatabase.class, databaseName)
                .allowMainThreadQueries().build()
                : instance;
    }

    public abstract BillDAO billDAO();
}
