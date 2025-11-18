package com.yamidev.drinkfinder.map;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface NearbyBarsProvider {

    interface NearbyBarsCallback {
        void onResult(@NonNull List<NearByBar> bars);
        void onError(@NonNull Throwable t);
    }

    /**
     * @param userLocation Ubicación del usuario.
     * @param radiusMeters Radio de búsqueda en metros (ej. 5000f para 5km).
     */
    void getNearbyBars(@NonNull LatLng userLocation,
                       float radiusMeters,
                       @NonNull NearbyBarsCallback callback);
}
