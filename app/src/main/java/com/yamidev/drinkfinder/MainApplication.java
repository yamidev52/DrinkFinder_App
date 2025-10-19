package com.yamidev.drinkfinder;

import android.app.Application;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.yamidev.drinkfinder.workers.SyncWorker;
import java.util.concurrent.TimeUnit;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        schedulePeriodicSync();
    }

    private void schedulePeriodicSync() {
        PeriodicWorkRequest syncRequest =
                new PeriodicWorkRequest.Builder(SyncWorker.class, 12, TimeUnit.HOURS)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "periodic_sync_work",
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
        );
    }
}