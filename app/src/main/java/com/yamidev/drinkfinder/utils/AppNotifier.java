//Esta clase es solo para pruebas, cambiala por la que hagas tu para las notificaciones @Yamil
package com.yamidev.drinkfinder.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.yamidev.drinkfinder.R;

public class AppNotifier {

    private static final String CHANNEL_ID = "drinkfinder_channel";
    private static final String CHANNEL_NAME = "DrinkFinder Notifications";
    private static final int NOTIFICATION_ID = 2001;

    private final Context context;

    public AppNotifier(Context context) {
        this.context = context.getApplicationContext();
        createChannel();
    }

    private void createChannel() {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Solo se crea el canal una vez (a partir de Android O)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Uri soundUri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.drink_sound);

            AudioAttributes audioAttrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Notificaciones de DrinkFinder con sonido especial");
            channel.setSound(soundUri, audioAttrs);
            channel.enableLights(true);
            channel.enableVibration(true);

            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Lanza una notificación estándar.
     */
    public void show(String title, String message) {

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.favorito)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_ID, notification);
    }
}