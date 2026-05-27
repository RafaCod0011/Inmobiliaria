package com.universidad.inmobiliaria.ui.contratos;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.universidad.inmobiliaria.modelo.Contrato;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {
    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<Contrato> contratoMutable = new MutableLiveData<>();
    private MutableLiveData<String> mensaje = new MutableLiveData<>();


    public LiveData<Contrato> getContratoMutable() {
        return contratoMutable;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void obtenerContratoPorInmueble(int idInmueble) {
        String token = ApiClient.usarToken(getApplication());

        if (token == null || token.trim().isEmpty()) {
            mensaje.setValue("No se encontró token de autenticación. Inicie sesión nuevamente.");
            return;
        }

        ApiClient.getServicio().getContratoPorInmueble(token, idInmueble)
                .enqueue(new Callback<Contrato>() {
                    @Override
                    public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            contratoMutable.setValue(response.body());
                        } else {
                            String error = "Error " + response.code() + ": " + response.message();
                            mensaje.setValue(error);
                            Log.d("DetalleContratoVM", error);
                        }
                    }


                    @Override
                    public void onFailure(Call<Contrato> call, Throwable t) {
                        String error = "Error de conexión: " + t.getMessage();
                        mensaje.setValue(error);
                        Log.d("DetalleContratoVM", error);
                    }
                });
    }

    public String formatearFecha(String fecha) {

        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatoArgentina = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return formatoArgentina.format(formatoEntrada.parse(fecha));
        } catch (Exception e) {
            return fecha;
        }

    }

    public String formatearMoneda(double monto) {

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance( new Locale("es", "AR"));
        return formatoMoneda.format(monto);

    }




}