package com.universidad.inmobiliaria.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.universidad.inmobiliaria.MainActivity;
import com.universidad.inmobiliaria.R;
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



        vm.getErrorUsuario().observe(this, error -> {
            binding.etUsuario.setError(error);
        });

        vm.getErrorPassword().observe(this, error -> {
            binding.etPassword.setError(error);
        });

        /*vm.getLoginExitoso().observe(this, exito -> {
            if (exito) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });*/

        binding.btnLogin.setOnClickListener(v -> {
            String user = binding.etUsuario.getText().toString();
            String pass = binding.etPassword.getText().toString();
            vm.verificarDatos(user, pass);
        });

    }
}