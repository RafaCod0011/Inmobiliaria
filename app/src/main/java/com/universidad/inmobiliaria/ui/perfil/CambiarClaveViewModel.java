package com.universidad.inmobiliaria.ui.perfil;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.universidad.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarClaveViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cambioExitoso = new MutableLiveData<>();

    public CambiarClaveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<Boolean> getCambioExitoso() {
        return cambioExitoso;
    }

    public void cambiarClave(String claveActual, String nuevaClave, String repetirClave) {

        // Validaciones
        if (claveActual.isEmpty() || nuevaClave.isEmpty() || repetirClave.isEmpty()) {
            mensaje.setValue("Complete todos los campos");
            return;
        }

        if (!nuevaClave.equals(repetirClave)) {
            mensaje.setValue("Las nuevas contraseñas no coinciden");
            return;
        }

        if (nuevaClave.length() < 6) {
            mensaje.setValue("La nueva contraseña debe tener al menos 6 caracteres");
            return;
        }

        String token = ApiClient.usarToken(getApplication());

        if (token == null || token.isEmpty()) {
            mensaje.setValue("Error de sesión. Por favor inicie sesión nuevamente");
            return;
        }

        // Llamada a Retrofit
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

        Call<Void> call = servicio.cambiarPassword(token, claveActual, nuevaClave);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cambioExitoso.setValue(true);
                } else {
                    mensaje.setValue("Error al cambiar la contraseña. Verifique la actual.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensaje.setValue("Error de conexión. Revise su internet.");
                t.printStackTrace();
            }
        });
    }
}