package com.yamidev.drinkfinder;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yamidev.drinkfinder.auth.FakeAuthService;

public class RegisterFragment extends Fragment {

    private TextInputLayout tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etEmail, etPassword, etConfirmPassword;
    private FakeAuthService authService;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilEmail = view.findViewById(R.id.til_email_register);
        tilPassword = view.findViewById(R.id.til_password_register);
        tilConfirmPassword = view.findViewById(R.id.til_confirm_password_register);
        etEmail = view.findViewById(R.id.et_email_register);
        etPassword = view.findViewById(R.id.et_password_register);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password_register);
        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView tvGoToLogin = view.findViewById(R.id.tv_go_to_login);

        authService = new FakeAuthService();

        btnRegister.setOnClickListener(v -> handleRegister());
        tvGoToLogin.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment));
    }

    private void handleRegister() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (!validateForm(email, password, confirmPassword)) {
            return;
        }

        authService.register(email, password, new FakeAuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "¡Registro exitoso! Por favor, inicia sesión.", Toast.LENGTH_LONG).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateForm(String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Correo inválido");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        if (password.length() < 6) {
            tilPassword.setError("La contraseña debe tener al menos 6 caracteres");
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Las contraseñas no coinciden");
            isValid = false;
        } else {
            tilConfirmPassword.setError(null);
        }
        return isValid;
    }
}
