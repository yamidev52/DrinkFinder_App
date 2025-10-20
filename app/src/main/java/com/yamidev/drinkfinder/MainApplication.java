package com.yamidev.drinkfinder;

import android.app.Application;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.yamidev.drinkfinder.workers.SyncWorker;
import java.util.concurrent.TimeUnit;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MainApplication extends Application {

    public static final String UPDATES_CHANNEL_ID = "updates_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        schedulePeriodicSync();
        createNotificationChannels();
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

    private void createNotificationChannels() {
        NotificationChannel updatesChannel = new NotificationChannel(
                UPDATES_CHANNEL_ID,
                "Actualizaciones de Datos",
                NotificationManager.IMPORTANCE_LOW
        );
        updatesChannel.setDescription("Notificaciones para tareas de larga duración");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(updatesChannel);

    }
}