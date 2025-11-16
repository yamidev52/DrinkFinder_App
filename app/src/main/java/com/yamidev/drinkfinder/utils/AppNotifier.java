//Esta clase es solo para pruebas, cambiala por la que hagas tu para las notificaciones @Yamil
package com.yamidev.drinkfinder.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class AppNotifier {

    private final Context context;

    public AppNotifier(Context context) {
        this.context = context.getApplicationContext();
    }

    public void showDrinkOfferNotification(String drinkId, String drinkName) {
        String message = "Simulando notificación para: " + drinkName + " (ID: " + drinkId + ")";
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        Log.d("AppNotifier", "showDrinkOfferNotification() llamado con ID: " + drinkId + " y Nombre: " + drinkName);
    }
}