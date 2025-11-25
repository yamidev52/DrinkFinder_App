package com.yamidev.drinkfinder.auth;

public class AuthResult<T> {

    private final boolean success;
    private final T data;
    private final String errorMessage;

    private AuthResult(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> AuthResult<T> success(T data) {
        return new AuthResult<>(true, data, null);
    }

    public static <T> AuthResult<T> error(String message) {
        return new AuthResult<>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
