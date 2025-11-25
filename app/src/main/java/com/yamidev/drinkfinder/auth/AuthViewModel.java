package com.yamidev.drinkfinder.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yamidev.drinkfinder.auth.UserEntity;
import com.yamidev.drinkfinder.auth.AuthRepository;
import com.yamidev.drinkfinder.auth.AuthResult;

public class AuthViewModel extends ViewModel {

    private final AuthRepository repository;

    public AuthViewModel(AuthRepository repository) {
        this.repository = repository;
    }

    public LiveData<AuthResult<UserEntity>> getLoginResult() {
        return repository.getLoginResult();
    }

    public LiveData<AuthResult<UserEntity>> getRegisterResult() {
        return repository.getRegisterResult();
    }

    public LiveData<Boolean> getSignOutResult() {
        return repository.getSignOutResult();
    }

    public LiveData<UserEntity> getCurrentUser() {
        return repository.getCurrentUser();
    }

    public void signIn(String email, String password) {
        repository.signIn(email, password);
    }

    public void signUp(String email, String password, String confirmPassword) {
        repository.signUp(email, password, confirmPassword);
    }

    public void signOut() {
        repository.signOut();
    }

    public void loadSession() {
        repository.loadSession();
    }
}