package com.universidad.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> listaMutable = new MutableLiveData<>();
    private final MutableLiveData<String> errorMutable = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cargandoMutable = new MutableLiveData<>(false);

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    // ==================== GETTERS ====================
    public LiveData<List<Inmueble>> getListaMutable() {
        return listaMutable;
    }

    public LiveData<String> getError() {
        return errorMutable;
    }

    public LiveData<Boolean> getCargando() {
        return cargandoMutable;
    }

    // ==================== MÉTODOS ====================
    public void cargarInmuebles() {
        String token = ApiClient.usarToken(getApplication());

        if (token == null || token.trim().isEmpty()) {
            errorMutable.setValue("No se encontró token de autenticación. Inicie sesión nuevamente.");
            return;
        }

        cargandoMutable.setValue(true);
        errorMutable.setValue(null); // Limpiar error anterior

        ApiClient.getServicio().getInmuebles(token)
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                        cargandoMutable.setValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            listaMutable.setValue(response.body());
                        } else {
                            String error = "Error " + response.code() + ": " + response.message();
                            errorMutable.setValue(error);
                            Log.d("InmueblesViewModel", error);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        cargandoMutable.setValue(false);
                        String error = "Error de conexión: " + t.getMessage();
                        errorMutable.setValue(error);
                        Log.d("InmueblesViewModel", error);
                    }
                });
    }

    // Método útil para refrescar la lista
    public void refresh() {
        cargarInmuebles();
    }
}