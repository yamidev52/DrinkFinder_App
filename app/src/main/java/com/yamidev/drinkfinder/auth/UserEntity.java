package com.yamidev.drinkfinder.auth;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String email;

    @NonNull
    public String password;

    @ColumnInfo(name = "isLoggedIn")
    public boolean isLoggedIn;
}