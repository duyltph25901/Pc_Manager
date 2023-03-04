package com.example.ad.database.db_local.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ad.model.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("select * from `usercurrent` order by `id` desc limit 1")
    User getUserCurrent();

    @Query("delete from `usercurrent`")
    void removeTable();
}
