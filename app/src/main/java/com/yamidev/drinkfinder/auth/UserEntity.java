package com.yamidev.drinkfinder.auth;

import androidx.annotation.NonNull;
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

    /**
     * Campo para persistir la sesión.
     * Solo un usuario debe tener isLoggedIn = true.
     */
    public boolean isLoggedIn;
}