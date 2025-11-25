package com.yamidev.drinkfinder.map;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesNearbyBarsProvider implements NearbyBarsProvider {

    private final PlacesClient placesClient;

    public PlacesNearbyBarsProvider(@NonNull Context context) {
        // Inicializa Places con tu API KEY AUTOMÁTICAMENTE del Manifest
        Places.initialize(context.getApplicationContext(),
                context.getString(
                        context.getResources().getIdentifier("google_maps_key", "string", context.getPackageName())
                )
        );

        placesClient = Places.createClient(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    @Override
    public void getNearbyBars(@NonNull LatLng userLocation, float radiusMeters, @NonNull NearbyBarsCallback callback) {

        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.TYPES
        );

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        placesClient.findCurrentPlace(request)
                .addOnSuccessListener((response) -> {
                    List<NearByBar> result = new ArrayList<>();

                    for (Place place : response.getPlaceLikelihoods()
                            .stream()
                            .map(likelihood -> likelihood.getPlace())
                            .toList()) {

                        if (place.getTypes() != null &&
                                (place.getTypes().contains(Place.Type.BAR) ||
                                        place.getTypes().contains(Place.Type.NIGHT_CLUB) ||
                                        place.getTypes().contains(Place.Type.RESTAURANT))) {

                            LatLng pos = place.getLatLng();
                            if (pos == null) continue;

                            float distance = distanceMeters(userLocation, pos);

                            if (distance <= radiusMeters) {
                                result.add(new NearByBar(
                                        place.getId(),
                                        place.getName(),
                                        place.getAddress(),
                                        pos,
                                        distance
                                ));
                            }
                        }
                    }

                    callback.onResult(result);
                })
                .addOnFailureListener(callback::onError);
    }

    private float distanceMeters(@NonNull LatLng from, @NonNull LatLng to) {
        float[] result = new float[1];
        android.location.Location.distanceBetween(
                from.latitude, from.longitude,
                to.latitude, to.longitude,
                result
        );
        return result[0];
    }
}