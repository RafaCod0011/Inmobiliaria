package com.universidad.inmobiliaria.ui.inquilinos;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.universidad.inmobiliaria.modelo.Contrato;
import com.universidad.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private final MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();
    private final MutableLiveData<String> errorMutable = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cargandoMutable = new MutableLiveData<>(false);

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    // ==================== GETTERS PARA LA VISTA ====================
    public LiveData<Contrato> getContratoMutable() {
        return contratoMutable;
    }

    public LiveData<String> getError() {
        return errorMutable;
    }

    public LiveData<Boolean> getCargando() {
        return cargandoMutable;
    }

    // ==================== MÉTODOS ====================
    public void obtenerContrato(int idInmueble) {
        // Evitamos llamadas repetidas si ya tenemos la info cargada
        if (contratoMutable.getValue() != null) {
            return;
        }

        String token = ApiClient.usarToken(getApplication());

        if (token == null || token.trim().isEmpty()) {
            errorMutable.setValue("No se encontró token de autenticación.");
            return;
        }

        cargandoMutable.setValue(true);
        errorMutable.setValue(null);

        // Llamamos al endpoint: /api/contratos/inmueble/{id}
        ApiClient.getServicio().getContratoPorInmueble(token, idInmueble)
                .enqueue(new Callback<Contrato>() { // <--- callback tipado con Contrato
                    @Override
                    public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                        cargandoMutable.setValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            contratoMutable.setValue(response.body());
                        } else {
                            String error = "Error al obtener contrato: " + response.code();
                            errorMutable.setValue(error);
                            Log.d("DetalleInquilinoVM", error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Contrato> call, Throwable t) {
                        cargandoMutable.setValue(false);
                        String error = "Error de red: " + t.getMessage();
                        errorMutable.setValue(error);
                        Log.d("DetalleInquilinoVM", error);
                    }
                });
    }
}