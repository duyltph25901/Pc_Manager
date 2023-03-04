package com.example.ad.database.db_local.product;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ad.model.Product;

@Dao
public interface ProductDAO {
    @Insert
    void insert(Product product);

    @Query("select * from `productcurrent` limit 1")
    Product getProductCurrent();

    @Query("delete from `productcurrent`")
    void removeTable();
}
