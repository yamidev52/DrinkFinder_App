package com.yamidev.drinkfinder.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.yamidev.drinkfinder.auth.UserDAO;
import com.yamidev.drinkfinder.auth.UserEntity;
import com.yamidev.drinkfinder.model.CommentDAO;
import com.yamidev.drinkfinder.model.CommentEntity;

@Database(
        entities = {
                DrinkEntity.class,
                UserEntity.class,
                CommentEntity.class
        },
        version = 3,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // === DAOs ===
    public abstract DrinkDao drinkDao();
    public abstract UserDAO userDAO();
    public abstract CommentDAO commentDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "drink_finder_database"
                            )
                            // En desarrollo, destruye la BD si cambia el esquema
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}