package com.yamidev.drinkfinder.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    public interface OnShakeListener {
        void onShake();
    }

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final OnShakeListener listener;

    // Sensibilidad del shake
    private static final float SHAKE_THRESHOLD = 12.0f;
    private static final int SHAKE_COOLDOWN = 1000; // 1 segundo
    private long lastShakeTime = 0;

    public ShakeDetector(Context context, OnShakeListener listener) {
        this.listener = listener;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /** Inicia la escucha del sensor */
    public void start() {
        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    /** Detiene la escucha del sensor */
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

        long now = System.currentTimeMillis();

        if (acceleration > SHAKE_THRESHOLD) {
            if (now - lastShakeTime > SHAKE_COOLDOWN) {
                lastShakeTime = now;
                if (listener != null) {
                    listener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se usa
    }
}