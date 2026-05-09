package com.universidad.inmobiliaria.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.universidad.inmobiliaria.MainActivity;
import com.universidad.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> errorUsuario = new MutableLiveData<>();
    private MutableLiveData<String> errorPassword = new MutableLiveData<>();

    private MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getErrorUsuario() { return errorUsuario; }
    public LiveData<String> getErrorPassword() { return errorPassword; }
    public LiveData<Boolean> getLoginExitoso() { return loginExitoso; }


    public void verificarDatos(String usuario, String password) {
        errorUsuario.setValue(null);
        errorPassword.setValue(null);

        if (usuario.isEmpty()) {
            errorUsuario.setValue("Ingrese su usuario");
            return;
        }
        if (password.isEmpty()) {
            errorPassword.setValue("Ingrese la contraseña");
            return;
        }

        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<String> call = servicio.login(usuario, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();
                    ApiClient.crearToken(getApplication(), token);

                    loginExitoso.postValue(true);   // ← Esto activa la navegación
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(getApplication(), "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión. Revise su internet", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}