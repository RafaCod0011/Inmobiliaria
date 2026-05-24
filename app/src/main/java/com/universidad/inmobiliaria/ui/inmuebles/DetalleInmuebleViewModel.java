package com.universidad.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> inmuebleMutable = new MutableLiveData<>();
    private MutableLiveData<String> textoDisponibilidadM = new MutableLiveData<>();
    private MutableLiveData<String> mensaje = new MutableLiveData<>();

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getUInmuebleMutable() {
        return inmuebleMutable;
    }

    public LiveData<String> getTextoDisponibilidad() {
        return textoDisponibilidadM;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void cargarDetalleInmueble(Bundle bundle) {
        Inmueble inmueble = bundle.getSerializable("inmueble", Inmueble.class);
        if (inmueble != null) {
            inmuebleMutable.setValue(inmueble);

            if (inmueble.isDisponible()) {
                textoDisponibilidadM.setValue("Disponible para alquilar");
            } else {
                textoDisponibilidadM.setValue("No disponible para alquilar");
            }
        }
    }

    public void cambiarDisponibilidad(boolean disponible) {
        Inmueble inmueble = inmuebleMutable.getValue();
        if (inmueble == null) return;

        inmueble.setDisponible(disponible);

        String token = ApiClient.usarToken(getApplication());

        ApiClient.getServicio().cambiarDisponibilidad(token, inmueble)
                .enqueue(new Callback<Inmueble>() {
                    @Override
                    public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            inmuebleMutable.postValue(response.body());
                            actualizarTextoDisponibilidad(response.body().isDisponible());
                            mensaje.postValue("Disponibilidad actualizada correctamente");
                        } else {
                            // ←←← MANEJO CENTRALIZADO DE 401/403
                            if (ApiClient.manejarErrorAutorizacion(getApplication(), response.code())) {
                                return;
                            }
                            mensaje.postValue("Error al actualizar: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable t) {
                        mensaje.postValue("Error de conexión");
                        Log.e("DetalleInmuebleVM", "Fallo en cambiar disponibilidad", t);
                    }
                });
    }

    private void actualizarTextoDisponibilidad(boolean disponible) {
        if (disponible) {
            textoDisponibilidadM.postValue("Disponible para alquilar");
        } else {
            textoDisponibilidadM.postValue("No disponible para alquilar");
        }
    }
}