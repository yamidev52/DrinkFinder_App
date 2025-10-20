package com.yamidev.drinkfinder.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;
import com.yamidev.drinkfinder.MainApplication;
import com.yamidev.drinkfinder.R;

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 1001;
    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public void showDrinkNotification(String drinkId, String drinkName) {
        Bundle args = new Bundle();
        args.putString("drinkId", drinkId);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setComponentName(com.yamidev.drinkfinder.HomeActivity.class)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.detailFragment)
                .setArguments(args)
                .createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainApplication.UPDATES_CHANNEL_ID)
                .setSmallIcon(R.drawable.favorito)
                .setContentTitle("¡Mira lo nuevo de hoy!")
                .setContentText("Prueba un delicioso " + drinkName)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("No te pierdas lo nuevo de hoy, " + drinkName + ". Toca para ver la receta."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}