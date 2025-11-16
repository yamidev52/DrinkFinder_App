package com.yamidev.drinkfinder.auth;

import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Map;

public class FakeAuthService {

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("test@test.com", "123456");
    }

    public void login(String email, String password, AuthCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (users.containsKey(email)) {

                if (users.get(email).equals(password)) {
                    callback.onSuccess();
                } else {
                    callback.onError("Contraseña incorrecta.");
                }
            } else {
                callback.onError("El usuario no existe.");
            }
        }, 1500);
    }

    public void register(String email, String password, AuthCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (users.containsKey(email)) {
                callback.onError("Este correo electrónico ya está registrado.");
            } else {

                users.put(email, password);
                callback.onSuccess();
            }
        }, 1500);
    }
}