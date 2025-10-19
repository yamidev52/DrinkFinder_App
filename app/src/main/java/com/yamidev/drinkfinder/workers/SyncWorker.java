package com.yamidev.drinkfinder.workers;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.yamidev.drinkfinder.Drink;
import com.yamidev.drinkfinder.drink.DrinkRepository;
import java.util.List;

public class SyncWorker extends Worker {

    private static final String TAG = "SyncWorker";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Tarea de sincronización iniciada...");

        try {
            DrinkRepository repo = new DrinkRepository(getApplicationContext());

            List<Drink> drinks = repo.searchByNameSync("Cocktail");

            if (!drinks.isEmpty()) {
                Log.d(TAG, "Sincronización exitosa. Se encontraron " + drinks.size() + " bebidas.");
                return Result.success();
            } else {
                Log.w(TAG, "Sincronización completada, pero no se encontraron bebidas.");
                return Result.success();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error durante la sincronización", e);
            return Result.failure();
        }
    }
}