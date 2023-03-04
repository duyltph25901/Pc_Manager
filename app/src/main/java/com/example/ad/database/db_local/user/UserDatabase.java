package com.example.ad.database.db_local.user;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ad.model.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static final String databaseName = "UserCurrent.db";
    private static UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context) {
        return (instance == null)
                ? Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, databaseName)
                .allowMainThreadQueries().build()
                : instance;
    }

    public abstract UserDAO userDAO();
}
