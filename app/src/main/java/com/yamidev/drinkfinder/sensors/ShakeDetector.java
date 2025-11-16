package com.yamidev.drinkfinder.sensors;

import android.content.Context;
import android.util.Log;

// Esta clase es unicamente para pruebas, cambiala por la que haras @Yamil
public class ShakeDetector {

    private static final String TAG = "FakeShakeDetector";

    public interface OnShakeListener {
        void onShake();
    }

    private OnShakeListener listener;

    public ShakeDetector(Context context, OnShakeListener listener) {
        this.listener = listener;
    }

    public void startListening() {
        Log.d(TAG, "Simulación: Empezando a 'escuchar' sacudidas.");
    }

    public void stopListening() {
        Log.d(TAG, "Simulación: Dejando de 'escuchar' sacudidas.");
    }

    public void simulateShake() {
        Log.d(TAG, "¡Simulando una sacudida!");
        if (listener != null) {
            listener.onShake();
        }
    }
}