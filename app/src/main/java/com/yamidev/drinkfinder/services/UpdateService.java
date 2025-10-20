package com.yamidev.drinkfinder.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.yamidev.drinkfinder.MainApplication;
import com.yamidev.drinkfinder.R;

public class UpdateService extends Service {

    private static final String TAG = "UpdateService";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Servicio creado.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado.");

        Notification notification = new NotificationCompat.Builder(this, MainApplication.UPDATES_CHANNEL_ID)
                .setContentTitle("DrinkFinder")
                .setContentText("Sincronizando datos...")
                .setSmallIcon(R.drawable.hogar)
                .build();

        startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);

        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    Log.d(TAG, "Procesando... " + i + "/10");
                    Thread.sleep(1000);
                }
                Log.d(TAG, "Sincronización completada.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                stopSelf();
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Servicio destruido.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}