package com.yamidev.drinkfinder.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.yamidev.drinkfinder.local.DrinkEntity;

import java.util.List;

@Dao
public interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(DrinkEntity drink);

    @Query("SELECT * FROM favorite_drinks ORDER BY name ASC")
    LiveData<List<DrinkEntity>> getAllFavorites();

    @Query("DELETE FROM favorite_drinks WHERE id = :drinkId")
    void deleteFavoriteById(String drinkId);

    @Query("SELECT * FROM favorite_drinks WHERE id = :drinkId")
    LiveData<DrinkEntity> getFavoriteById(String drinkId);
}
