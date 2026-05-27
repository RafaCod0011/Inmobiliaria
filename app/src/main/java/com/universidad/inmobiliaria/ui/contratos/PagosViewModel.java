package com.universidad.inmobiliaria.ui.contratos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.universidad.inmobiliaria.modelo.Pago;
import com.universidad.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> listaPago= new MutableLiveData<>();
    private MutableLiveData<String> mensaje= new MutableLiveData<>();
    public PagosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Pago>> getListaPago(){return listaPago;}
    public LiveData<String> getMensaje(){return mensaje;}

    public void obtenerPagosPorContrato(int idContrato){
        String token = ApiClient.usarToken(getApplication());

        if (token == null || token.trim().isEmpty()) {
            mensaje.setValue("No se encontró token de autenticación. Inicie sesión nuevamente.");
            return;
        }

        ApiClient.getServicio().obtenerPagosPorContrato(token, idContrato)
                .enqueue(new Callback<List<Pago>>() {
                    @Override
                    public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                        if(response.isSuccessful() && response.body() != null){
                            listaPago.setValue(response.body());
                        }else{
                            String error = "Error " + response.code() + ": " + response.message();
                            mensaje.setValue(error);
                            Log.d("PagosVM", error);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Pago>> call, Throwable t) {
                        String error = "Error de conexión: " + t.getMessage();
                        mensaje.setValue(error);
                        Log.d("PagosVM", error);
                    }

                });
    }
}