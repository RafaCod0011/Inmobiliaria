package com.universidad.inmobiliaria.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

        binding.etUsuario.setText("luisprofessor@gmail.com");
        binding.etPassword.setText("DEEKQW");

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

        //para probar, cuando mantenemos presionado el boton de login
        //metodo setOnLongClick
        binding.btnLogin.setOnLongClickListener(v -> {
            llamarEmergencia();
            return true;
        });



    }
    private void llamarEmergencia() {
        //si tenemos el permiso hacemos la llamada
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:3544409860")
            );
            startActivity(intent);
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            llamarEmergencia();
        }
    }
}