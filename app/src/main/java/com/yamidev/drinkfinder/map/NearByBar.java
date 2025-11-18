package com.yamidev.drinkfinder.map;

import com.google.android.gms.maps.model.LatLng;

public class NearByBar {

    private final String id;
    private final String name;
    private final String description;
    private final LatLng latLng;
    private final float distanceMeters;


    public NearByBar(String id, String name, String description, LatLng latLng, float distanceMeters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latLng = latLng;
        this.distanceMeters = distanceMeters;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public float getDistanceMeters() {
        return distanceMeters;
    }
}