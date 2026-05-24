package com.universidad.inmobiliaria.ui.perfil;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.universidad.inmobiliaria.modelo.Propietario;
import com.universidad.inmobiliaria.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Propietario> propietarioMutable = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeMutable = new MutableLiveData<>();
    private final MutableLiveData<Boolean> perfilActualizadoMutable = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getPropietarioMutable() {
        return propietarioMutable;
    }

    public LiveData<String> getMensajeMutable() {
        return mensajeMutable;
    }

    public LiveData<Boolean> getPerfilActualizadoMutable() {
        return perfilActualizadoMutable;
    }

    public void cargarPerfil() {
        String token = ApiClient.usarToken(getApplication());

        if (token == null || token.trim().isEmpty()) {
            mensajeMutable.setValue("Sesión expirada. Inicie sesión nuevamente.");
            return;
        }

        ApiClient.getServicio().getPropietario(token)
                .enqueue(new Callback<Propietario>() {
                    @Override
                    public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            propietarioMutable.postValue(response.body());
                        } else {
                            // ←←← MANEJO CENTRALIZADO DE 401/403
                            if (ApiClient.manejarErrorAutorizacion(getApplication(), response.code())) {
                                return;
                            }
                            mensajeMutable.postValue("Error al cargar perfil: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Propietario> call, Throwable t) {
                        mensajeMutable.postValue("Error de conexión");
                        Log.e("PerfilViewModel", "Fallo al cargar perfil", t);
                    }
                });
    }

    public void guardarPerfil(String nombre, String apellido, String dni, String telefono, String email) {
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            mensajeMutable.postValue("Complete todos los campos solicitados");
            return;
        }

        Propietario propietario = propietarioMutable.getValue();
        if (propietario == null) {
            mensajeMutable.postValue("No se pudo obtener el perfil");
            return;
        }

        propietario.setNombre(nombre);
        propietario.setApellido(apellido);
        propietario.setDni(Integer.parseInt(dni));
        propietario.setTelefono(telefono);
        propietario.setEmail(email);
        propietario.setClave(null);


        actualizarPerfil(propietario);
    }

    private void actualizarPerfil(Propietario propietario) {
        String token = ApiClient.usarToken(getApplication());

        ApiClient.getServicio().actualizarPropietario(token, propietario)
                .enqueue(new Callback<Propietario>() {
                    @Override
                    public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            propietarioMutable.postValue(response.body());
                            mensajeMutable.postValue("Perfil actualizado correctamente");
                            perfilActualizadoMutable.postValue(true);
                        } else {
                            if (ApiClient.manejarErrorAutorizacion(getApplication(), response.code())) {
                                return;
                            }
                            mensajeMutable.postValue("No se pudo actualizar el perfil");
                        }
                    }

                    @Override
                    public void onFailure(Call<Propietario> call, Throwable t) {
                        mensajeMutable.postValue("Error de conexión al actualizar el perfil");
                        Log.e("PerfilViewModel", "Fallo en actualizar", t);
                    }
                });
    }

    public void limpiarMensaje() {
        mensajeMutable.setValue(null);
    }

    public void resetPerfilActualizado() {
        perfilActualizadoMutable.setValue(false);
    }
}