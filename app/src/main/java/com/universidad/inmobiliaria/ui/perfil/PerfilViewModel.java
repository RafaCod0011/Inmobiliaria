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

    private MutableLiveData<Propietario> propietarioMutable =new MutableLiveData<>();
    private MutableLiveData<String> mensajeMutable = new MutableLiveData<>();
    private MutableLiveData<Boolean> perfilActualizadoMutable = new MutableLiveData<>();

        public PerfilViewModel(@NonNull Application application){
            super(application);
        }

        public LiveData<Propietario>getPropietarioMutable() {
            return propietarioMutable;
        }

        public LiveData<String> getMensajeMutable() {
            return mensajeMutable;
        }

        public LiveData<Boolean>getPerfilActualizadoMutable() {
            return perfilActualizadoMutable;
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

    public void guardarPerfil(String nombre, String apellido, String dni, String telefono, String email) {

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            mensajeMutable.postValue("Complete todos los campos solicitados");
            return;
        }

        if (!nombre.matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            mensajeMutable.postValue("El nombre no puede contener números o simbolos");
            return;
        }

        if (!apellido.matches("^[a-zA-ZÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            mensajeMutable.postValue("El apellido no puede contener números o simbolos");
            return;
        }

        if (!dni.matches("^\\d{8}$")) {
            mensajeMutable.postValue("El DNI debe contener 8 dígitos");
            return;
        }

        if (!telefono.matches("^\\d+$")) {
            mensajeMutable.postValue("El teléfono solo puede contener números");
            return;
        }
        if (telefono.length() < 8 || telefono.length() > 15) {
            mensajeMutable.postValue("El teléfono debe tener entre 8 y 15 dígitos");
            return;
        }
        // Casos como "11111111" o "000000"
        if (telefono.matches("^(\\d)\\1+$")) {
            mensajeMutable.postValue("Ingrese un teléfono válido");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mensajeMutable.postValue("Ingrese un email válido");
            return;
        }

        try {
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

        } catch (NumberFormatException e) {
            mensajeMutable.postValue("El DNI o el teléfono tienen un formato inválido");
        } catch (Exception e) {
            mensajeMutable.postValue("Ocurrió un error al guardar el perfil");
        }
    }

        private void actualizarPerfil(Propietario propietario) {

            String token = ApiClient.usarToken(getApplication());

            if (token == null || token.isEmpty()) {
                mensajeMutable.postValue("No se pudo obtener el token");
                return;
            }

            ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();

            Call<Propietario> call = servicio.actualizarPropietario(token,propietario);

            call.enqueue(new Callback<Propietario>() {

                        @Override
                        public void onResponse(Call<Propietario> call, Response<Propietario> response) {

                            if (response.isSuccessful()) {
                                propietarioMutable.postValue( response.body()
                                );
                                mensajeMutable.postValue("Perfil actualizado correctamente");
                                perfilActualizadoMutable.postValue(true);

                            } else {

                               mensajeMutable.postValue("No se pudo actualizar el perfil");
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<Propietario> call, Throwable t
                        ) {
                            mensajeMutable.postValue("Error de conexión al actualizar el perfil");
                            Log.e("ActualizarPerfil", "Fallo en la llamada", t);
                        }
                    });
        }
        public void limpiarMensaje(){
            mensajeMutable.setValue(null);
        }

        public void resetPerfilActualizado(){
            perfilActualizadoMutable.setValue(false);
        }


}