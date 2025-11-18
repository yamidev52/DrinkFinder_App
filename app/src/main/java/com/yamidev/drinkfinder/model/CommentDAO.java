package com.yamidev.drinkfinder.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDAO {

    @Query("SELECT * FROM comments WHERE drinkId = :drinkId ORDER BY createdAt DESC")
    LiveData<List<CommentEntity>> getCommentsForDrink(String drinkId);

    @Query("SELECT * FROM comments ORDER BY createdAt DESC")
    LiveData<List<CommentEntity>> getAllComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CommentEntity comment);

    @Update
    void update(CommentEntity comment);

    @Delete
    void delete(CommentEntity comment);

    @Query("DELETE FROM comments WHERE drinkId = :drinkId")
    void deleteAllForDrink(String drinkId);
}