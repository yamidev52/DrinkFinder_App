package com.yamidev.drinkfinder.auth;
import android.os.Handler;
import android.os.Looper;

public class FakeAuthService {

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    public void login(String email, String password, AuthCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if ("test@test.com".equals(email) && "test".equals(password)) {
                callback.onSuccess();
            } else {
                callback.onError("Credenciales inválidas.");
            }
        }, 1500);
    }
}
