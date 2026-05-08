package com.universidad.inmobiliaria.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.universidad.inmobiliaria.modelo.Propietario;
import com.universidad.inmobiliaria.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> propietarioMutable;

        public PerfilViewModel(@NonNull Application application){
            super(application);
        }

        public LiveData<Propietario> getPropietarioMutable(){
            if(propietarioMutable == null){
                propietarioMutable = new MutableLiveData<>();
            }
            return propietarioMutable;
        }

        public void cargarPerfil(){

            String token = ApiClient.usarToken(getApplication());

            ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

            Call<Propietario> call = servicio.getPropietario(token);

            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if( response.isSuccessful()){
                        Propietario p = response.body();
                        propietarioMutable.postValue(p);
                    } else {
                        Toast.makeText(getApplication(), "No se pudo encontrar al Propietario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });


        }
}