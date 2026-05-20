package com.universidad.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();
    private MutableLiveData<String> textoDisponibilidadM = new MutableLiveData<>();

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getUInmuebleMutable(){
        if(inmuebleMutable == null){
            inmuebleMutable= new MutableLiveData<>();
        }
        return inmuebleMutable;
    }
    public LiveData<String> getTextoDisponibilidad() {
        if (textoDisponibilidadM == null){
            textoDisponibilidadM = new MutableLiveData<>();
        }
        return textoDisponibilidadM;
    }

    public void cargarDetalleInmueble(Bundle bundle) {
        Inmueble bundleInmueble = bundle.getSerializable("inmueble", Inmueble.class);
        inmuebleMutable.setValue(bundleInmueble);

        if (inmuebleMutable.getValue().isDisponible()) {
            textoDisponibilidadM.postValue("Disponible para alquilar");
        }else{
            textoDisponibilidadM.postValue(("No disponible para alquilar"));
        }
    }

    public void cambiarDisponibilidad(boolean disponible) {
        Inmueble inmueble = inmuebleMutable.getValue();
        inmueble.setDisponible(disponible);

        String token = ApiClient.usarToken(getApplication());
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        Call<Inmueble> call = servicio.cambiarDisponibilidad(token, inmueble);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmuebleMutable.postValue(inmueble);
                    if (inmueble.isDisponible()) {
                        textoDisponibilidadM.postValue("Disponible para alquilar");
                        Toast.makeText(getApplication(), "Estado actualizado", Toast.LENGTH_SHORT).show();
                    }else {
                        textoDisponibilidadM.postValue(("No disponible para alquilar"));
                        Toast.makeText(getApplication(), "Estado actualizado", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("INMUEBLE_DETALLE", "Disponibilidad cambiada");
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {

            }
        });
    }
}