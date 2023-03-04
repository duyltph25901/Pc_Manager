package com.example.ad.database.db_local.Bill;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ad.model.Bill;

@Dao
public interface BillDAO {
    @Insert
    void insert(Bill bill);

    @Query("SELECT * FROM `Bill` ORDER BY `id` DESC LIMIT 1")
    Bill getBill();

    @Query("DELETE FROM `Bill`")
    void removeTable();
}
