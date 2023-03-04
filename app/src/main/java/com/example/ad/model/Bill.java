package com.example.ad.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Bill")
public class Bill {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String idOrder;

    public Bill(String idOrder) {
        this.idOrder = idOrder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }
}
