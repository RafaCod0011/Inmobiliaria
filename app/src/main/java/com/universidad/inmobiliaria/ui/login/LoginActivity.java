package com.universidad.inmobiliaria.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.universidad.inmobiliaria.MainActivity;
import com.universidad.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel vm;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observers
        vm.getErrorUsuario().observe(this, error -> {
            binding.etUsuario.setError(error);
        });

        vm.getErrorPassword().observe(this, error -> {
            binding.etPassword.setError(error);
        });

        // Observer de login exitoso
        vm.getLoginExitoso().observe(this, exito -> {
            if (Boolean.TRUE.equals(exito)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String usuario = binding.etUsuario.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            vm.verificarDatos(usuario, password);
        });
    }
}