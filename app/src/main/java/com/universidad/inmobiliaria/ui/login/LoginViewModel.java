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
        } else if (password.isEmpty()) {
            errorPassword.setValue("Ingrese la contraseña");
        } else {
            ApiClient.MiServicioInmobiliaria servicio =  ApiClient.getServicio();
            Call<String> call = servicio.login(usuario,password);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        // Revisar colocar o no el TOAST
                        Toast.makeText(context, "Ingresando....", Toast.LENGTH_SHORT).show();
                        String token = response.body();
                        ApiClient.crearToken(context,token);
                        Log.d("token",token);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }else{
                        Log.d("Error", response.message()); // Mensaje de error que devuelve
                        Log.d("Error", response.code()+""); // Codigo del error
                        Log.d("Error", response.errorBody().toString()+""); // junto
                        if (response.code() == 400)
                            Toast.makeText(context, "Contraseña y/o Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Mensaje",t.getMessage());
                }
            });
        }
    }

}