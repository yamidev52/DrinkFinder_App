package com.yamidev.drinkfinder.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.yamidev.drinkfinder.data.local.dao.DrinkDao;
import com.yamidev.drinkfinder.data.local.entity.DrinkEntity;

@Database(entities = {DrinkEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract DrinkDao drinkDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "drink_finder_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
