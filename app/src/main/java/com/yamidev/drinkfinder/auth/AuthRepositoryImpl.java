package com.yamidev.drinkfinder.auth;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yamidev.drinkfinder.auth.UserDAO;
import com.yamidev.drinkfinder.auth.UserEntity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepositoryImpl implements AuthRepository {

    private final UserDAO userDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<AuthResult<UserEntity>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<UserEntity>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signOutResult = new MutableLiveData<>();
    private final MutableLiveData<UserEntity> currentUser = new MutableLiveData<>();

    public AuthRepositoryImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public LiveData<AuthResult<UserEntity>> getLoginResult() {
        return loginResult;
    }

    @Override
    public LiveData<AuthResult<UserEntity>> getRegisterResult() {
        return registerResult;
    }

    @Override
    public LiveData<Boolean> getSignOutResult() {
        return signOutResult;
    }

    @Override
    public LiveData<UserEntity> getCurrentUser() {
        return currentUser;
    }

    @Override
    public void signUp(String email, String password, String confirmPassword) {
        executor.execute(() -> {

            // Validaciones básicas de los campos que vienen de:
            // et_email_register, et_password_register, et_confirm_password_register
            if (TextUtils.isEmpty(email)) {
                registerResult.postValue(AuthResult.error("El email no puede estar vacío"));
                return;
            }
            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                registerResult.postValue(AuthResult.error("La contraseña no puede estar vacía"));
                return;
            }
            if (!password.equals(confirmPassword)) {
                registerResult.postValue(AuthResult.error("Las contraseñas no coinciden"));
                return;
            }
            if (password.length() < 6) {
                registerResult.postValue(AuthResult.error("La contraseña debe tener al menos 6 caracteres"));
                return;
            }

            // Verificar si ya existe usuario con ese email
            UserEntity existing = userDao.getUserByEmail(email);
            if (existing != null) {
                registerResult.postValue(AuthResult.error("Ya existe una cuenta con ese correo"));
                return;
            }

            // Crear el nuevo usuario y marcarlo como logueado
            userDao.clearLoggedInUser();

            UserEntity newUser = new UserEntity();
            newUser.email = email;
            newUser.password = password; // OJO: en producción, siempre hashear
            newUser.isLoggedIn = true;

            long id = userDao.insertUser(newUser);
            newUser.id = id;

            currentUser.postValue(newUser);
            registerResult.postValue(AuthResult.success(newUser));
        });
    }

    @Override
    public void signIn(String email, String password) {
        executor.execute(() -> {

            // Campos provenientes de et_email_login y et_password_login
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                loginResult.postValue(AuthResult.error("Email y contraseña son obligatorios"));
                return;
            }

            UserEntity user = userDao.getUserByEmail(email);
            if (user == null) {
                loginResult.postValue(AuthResult.error("Usuario no encontrado"));
                return;
            }

            if (!user.password.equals(password)) {
                loginResult.postValue(AuthResult.error("Contraseña incorrecta"));
                return;
            }

            // Actualizar sesión: solo este usuario logueado
            userDao.clearLoggedInUser();
            user.isLoggedIn = true;
            userDao.updateUser(user);

            currentUser.postValue(user);
            loginResult.postValue(AuthResult.success(user));
        });
    }

    @Override
    public void signOut() {
        executor.execute(() -> {
            userDao.clearLoggedInUser();
            currentUser.postValue(null);
            signOutResult.postValue(true);
        });
    }

    @Override
    public void loadSession() {
        executor.execute(() -> {
            UserEntity user = userDao.getLoggedInUser();
            currentUser.postValue(user);
        });
    }
}