package com.yamidev.drinkfinder.auth;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    UserEntity getLoggedInUser();

    @Query("UPDATE users SET isLoggedIn = 0")
    void clearLoggedInUser();

    @Update
    void updateUser(UserEntity user);
}