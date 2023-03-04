package com.example.ad.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserCurrent")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String email, pass;

    public User(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
