package com.universidad.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Uri> imagenMutable = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final MutableLiveData<Boolean> limpiarFormulario = new MutableLiveData<>(false);

    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getImagenMutable() { return imagenMutable; }
    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<Boolean> getLimpiarFormulario() { return limpiarFormulario; }

    public void recibirFoto(Intent data) {
        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            imagenMutable.setValue(uri);
        }
    }

    public void guardarInmueble(String ambientesStr, String direccion, String superficieStr,
                                String latitudStr, String longitudStr, String valorStr,
                                String uso, String tipo) {

        // === VALIDACIONES FUERTES ===
        if (direccion.trim().isEmpty() || uso.trim().isEmpty() || tipo.trim().isEmpty()) {
            mensaje.postValue("Los campos de texto son obligatorios");
            return;
        }

        if (ambientesStr.trim().isEmpty() || superficieStr.trim().isEmpty() ||
                latitudStr.trim().isEmpty() || longitudStr.trim().isEmpty() || valorStr.trim().isEmpty()) {
            mensaje.postValue("Todos los campos numéricos son obligatorios");
            return;
        }

        try {
            int ambientes = Integer.parseInt(ambientesStr);
            int superficie = Integer.parseInt(superficieStr);
            double latitud = Double.parseDouble(latitudStr);
            double longitud = Double.parseDouble(longitudStr);
            double valor = Double.parseDouble(valorStr);

            if (ambientes <= 0 || superficie <= 0 || valor <= 0) {
                mensaje.postValue("Los valores numéricos deben ser mayores a 0");
                return;
            }

            if (direccion.length() < 5) {
                mensaje.postValue("La dirección debe tener al menos 5 caracteres");
                return;
            }

            // === PROCESAR IMAGEN ===
            byte[] imagenBytes = transformarImagen();
            if (imagenBytes.length == 0) {
                mensaje.postValue("Debe seleccionar una imagen del inmueble");
                return;
            }

            // === CREAR INMUEBLE ===
            Inmueble inmueble = new Inmueble();
            inmueble.setAmbientes(ambientes);
            inmueble.setDireccion(direccion.trim());
            inmueble.setSuperficie(superficie);
            inmueble.setLatitud(latitud);
            inmueble.setLongitud(longitud);
            inmueble.setValor(valor);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setDisponible(false); // Por defecto deshabilitado

            // === CONVERTIR A JSON ===
            String inmuebleJson = new Gson().toJson(inmueble);
            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json"), inmuebleJson);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagenBytes);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "inmueble.jpg", requestFile);

            // === LLAMADA AL SERVIDOR ===
            String token = ApiClient.usarToken(getApplication());
            ApiClient.getServicio().cargarInmueble(token, imagenPart, inmuebleBody)
                    .enqueue(new Callback<Inmueble>() {
                        @Override
                        public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                            if (response.isSuccessful()) {
                                mensaje.postValue("¡Inmueble guardado correctamente!");
                                limpiarFormulario.postValue(true);
                            } else {
                                if (ApiClient.manejarErrorAutorizacion(getApplication(), response.code())) {
                                    return;
                                }
                                mensaje.postValue("Error al guardar: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Inmueble> call, Throwable t) {
                            mensaje.postValue("Error de conexión");
                            Log.e("NuevoInmuebleVM", "Error", t);
                        }
                    });

        } catch (NumberFormatException e) {
            mensaje.postValue("Los campos numéricos tienen formato inválido");
        } catch (Exception e) {
            mensaje.postValue("Error inesperado: " + e.getMessage());
        }
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = imagenMutable.getValue();
            if (uri == null) {
                mensaje.postValue("Debe seleccionar una imagen del inmueble");
                return new byte[0];
            }

            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                mensaje.postValue("No se pudo abrir la imagen");
                return new byte[0];
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            if (bitmap == null) {
                mensaje.postValue("No se pudo procesar la imagen");
                return new byte[0];
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);   // 80% es más estable
            byte[] bytes = baos.toByteArray();
            baos.close();

            Log.d("NuevoInmuebleVM", "✅ Imagen convertida correctamente: " + bytes.length + " bytes");
            return bytes;

        } catch (Exception e) {
            Log.e("NuevoInmuebleVM", "❌ Error al transformar imagen", e);
            mensaje.postValue("Error al procesar la imagen: " + e.getMessage());
            return new byte[0];
        }
    }

    public void resetLimpiarFormulario() {
        limpiarFormulario.setValue(false);
    }
}