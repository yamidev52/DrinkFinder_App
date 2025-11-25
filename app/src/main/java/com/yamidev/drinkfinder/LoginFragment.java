package com.yamidev.drinkfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yamidev.drinkfinder.auth.AuthViewModel;
import com.yamidev.drinkfinder.auth.AuthViewModelFactory;
import com.yamidev.drinkfinder.local.AppDatabase;
import com.yamidev.drinkfinder.auth.AuthRepository;
import com.yamidev.drinkfinder.auth.AuthRepositoryImpl;

public class LoginFragment extends Fragment {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;

    private AuthViewModel authViewModel;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilEmail = view.findViewById(R.id.til_email_login);
        tilPassword = view.findViewById(R.id.til_password_login);
        etEmail = view.findViewById(R.id.et_email_login);
        etPassword = view.findViewById(R.id.et_password_login);
        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView tvGoToRegister = view.findViewById(R.id.tv_go_to_register);

        // Crear repo + viewModel
        AppDatabase db = AppDatabase.getInstance(requireContext());
        AuthRepository repository = new AuthRepositoryImpl(db.userDAO());
        AuthViewModelFactory factory = new AuthViewModelFactory(repository);
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        setupObservers();

        // Cargar sesión si ya había usuario logueado
        authViewModel.loadSession();

        btnLogin.setOnClickListener(v -> handleLogin());
        tvGoToRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));
    }

    private void setupObservers() {
        // Resultado de login
        authViewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;

            if (result.isSuccess()) {
                if (!isAdded()) return;

                // Usuario logueado correctamente, ir a HomeActivity
                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Si ya hay sesión activa al abrir el fragment, saltar directo al Home
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null && isAdded()) {
                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void handleLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (!validateForm(email, password)) {
            return;
        }

        authViewModel.signIn(email, password);
    }

    private boolean validateForm(String email, String password) {
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
        return isValid;
    }
}