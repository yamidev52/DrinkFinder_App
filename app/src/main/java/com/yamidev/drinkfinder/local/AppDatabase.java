package com.yamidev.drinkfinder.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.yamidev.drinkfinder.auth.UserDAO;
import com.yamidev.drinkfinder.auth.UserEntity;

@Database(
        entities = {DrinkEntity.class, UserEntity.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract DrinkDao drinkDao();
    public abstract UserDAO userDAO();  // 👈 ya lo tenías, está bien

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "drink_finder_database"
                            )
                            // Como estamos en desarrollo, tiramos la BD anterior si cambia el esquema
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}