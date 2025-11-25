package com.yamidev.drinkfinder.map;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MockNearbyBarsProvider implements NearbyBarsProvider {

    private final Context context;

    public MockNearbyBarsProvider(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void getNearbyBars(@NonNull LatLng userLocation,
                              float radiusMeters,
                              @NonNull NearbyBarsCallback callback) {

        // Lista mock “alrededor” del usuario
        List<NearByBar> allBars = new ArrayList<>();

        LatLng bar1 = new LatLng(userLocation.latitude + 0.002, userLocation.longitude + 0.001);
        LatLng bar2 = new LatLng(userLocation.latitude - 0.003, userLocation.longitude - 0.002);
        LatLng bar3 = new LatLng(userLocation.latitude + 0.004, userLocation.longitude - 0.002);

        allBars.add(buildBar("1", "Cantina El Mapache",
                "Promo 2x1 en cervezas", userLocation, bar1));
        allBars.add(buildBar("2", "Cantina de Moe",
                "Happy Hour de 5 a 7", userLocation, bar2));
        allBars.add(buildBar("3", "El Buen Beber",
                "Música en vivo los viernes", userLocation, bar3));

        // Filtrar por radio (<= radiusMeters, por ejemplo 5000m = 5km)
        List<NearByBar> filtered = new ArrayList<>();
        for (NearByBar bar : allBars) {
            if (bar.getDistanceMeters() <= radiusMeters) {
                filtered.add(bar);
            }
        }

        callback.onResult(filtered);
    }

    private NearByBar buildBar(String id,
                               String name,
                               String description,
                               LatLng userLocation,
                               LatLng barLocation) {

        float distance = distanceMeters(userLocation, barLocation);
        return new NearByBar(id, name, description, barLocation, distance);
    }

    private float distanceMeters(@NonNull LatLng from, @NonNull LatLng to) {
        float[] result = new float[1];
        Location.distanceBetween(
                from.latitude, from.longitude,
                to.latitude, to.longitude,
                result
        );
        return result[0];
    }
}