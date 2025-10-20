package com.yamidev.drinkfinder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                Toast.makeText(context, "Conexión a Internet restaurada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Se ha perdido la conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }
}