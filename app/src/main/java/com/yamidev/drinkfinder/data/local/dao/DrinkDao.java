package com.yamidev.drinkfinder.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yamidev.drinkfinder.data.local.entity.DrinkEntity;
import java.util.List;


@Dao
public interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(DrinkEntity drink);

    @Query("SELECT * FROM favorite_drinks ORDER BY drink_name ASC")
    LiveData<List<DrinkEntity>> getAllFavorites();

    @Query("DELETE FROM favorite_drinks WHERE id_drink = :drinkId")
    void deleteFavoriteById(String drinkId);

    @Query("SELECT * FROM favorite_drinks WHERE id_drink = :drinkId")
    DrinkEntity getFavoriteById(String drinkId);
}
