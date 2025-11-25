package com.yamidev.drinkfinder.auth;

import androidx.lifecycle.LiveData;

import com.yamidev.drinkfinder.auth.UserEntity;

public interface AuthRepository {

    LiveData<AuthResult<UserEntity>> getLoginResult();

    LiveData<AuthResult<UserEntity>> getRegisterResult();

    LiveData<Boolean> getSignOutResult();

    LiveData<UserEntity> getCurrentUser();

    void signUp(String email, String password, String confirmPassword);

    void signIn(String email, String password);

    void signOut();

    /**
     * Cargar sesión guardada al iniciar la app.
     */
    void loadSession();
}