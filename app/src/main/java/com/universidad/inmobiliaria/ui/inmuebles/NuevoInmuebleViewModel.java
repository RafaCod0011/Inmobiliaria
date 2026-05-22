package com.universidad.inmobiliaria.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Uri> imagenMutable;
    private MutableLiveData<String> mensajeMutable;

    private MutableLiveData<Boolean> limpiarMutable;
    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getImagenMutable() {
        if (imagenMutable == null) {
            imagenMutable = new MutableLiveData<>();
        }
        return imagenMutable;
    }

    public LiveData<String> getMensajeMutable() {
        if (mensajeMutable == null) {
            mensajeMutable = new MutableLiveData<>();
        }
        return mensajeMutable;
    }

    public LiveData<Boolean> getLimpiarMutable() {
        if (limpiarMutable == null) {
            limpiarMutable = new MutableLiveData<>();
        }
        return limpiarMutable;
    }

    /*public void setImagen(Uri uri) {
        imagenMutable.setValue(uri);
    }
    */


    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Intent data = result.getData();
            Uri uri = data.getData();
            Log.d("salada", uri.toString());
            imagenMutable.setValue(uri);
        }
    }

    public void guardarInmueble(
            String ambientes,
            String direccion,
            String superficie,
            String latitud,
            String longitud,
            String valor,
            String uso,
            String tipo
    ) {

        try {
            ambientes = ambientes.trim();
            direccion = direccion.trim();
            superficie = superficie.trim();
            latitud = latitud.trim();
            longitud = longitud.trim();
            valor = valor.trim();
            uso = uso.trim();
            tipo = tipo.trim();

            if (ambientes.isEmpty() || direccion.isEmpty() ||
                    superficie.isEmpty() || latitud.isEmpty() ||
                    longitud.isEmpty() || valor.isEmpty() ||
                    uso.isEmpty() || tipo.isEmpty()) {

                mensajeMutable.postValue("Debe completar todos los campos");
                return;
            }

            int amb = Integer.parseInt(ambientes);
            int sup = Integer.parseInt(superficie);
            double lat = Double.parseDouble(latitud);
            double lon = Double.parseDouble(longitud);
            double val = Double.parseDouble(valor);

            if (amb <= 0) {
                mensajeMutable.postValue("Los ambientes deben ser mayores a 0");
                return;
            }

            if (sup <= 0) {
                mensajeMutable.postValue("La superficie debe ser mayor a 0");
                return;
            }

            if (val <= 0) {
                mensajeMutable.postValue("El valor debe ser mayor a 0");
                return;
            }

            if (direccion.length() < 5) {
                mensajeMutable.postValue("La dirección es demasiado corta");
                return;
            }

            byte[] imagen = transformarImagen();

            if (imagen.length == 0) {
                mensajeMutable.postValue("Debe ingresar una imágen del inmueble");
                return;
            }


            Inmueble i = new Inmueble();
            i.setAmbientes(amb);
            i.setDireccion(direccion);
            i.setSuperficie(sup);
            i.setLatitud(lat);
            i.setLongitud(lon);
            i.setValor(val);
            i.setUso(uso);
            i.setTipo(tipo);

            String inmuebleJson = new Gson().toJson(i);
            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),inmuebleJson);

            RequestBody requestFile =RequestBody.create(MediaType.parse("image/jpeg"),imagen);

            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData( "imagen","imagen.jpg",requestFile);
            ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
            String token = ApiClient.usarToken(getApplication());

            Call<Inmueble> call =servicio.cargarInmueble(token,imagenPart,inmuebleBody);
            call.enqueue(new Callback<Inmueble>() {

                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {

                    if (response.isSuccessful()) {

                        mensajeMutable.postValue("Inmueble cargado correctamente");
                        limpiarMutable.postValue(true);

                    } else {

                        mensajeMutable.postValue("Error al cargar inmueble");
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {

                    mensajeMutable.postValue("Error de conexión");
                }
            });

        } catch (NumberFormatException e) {

            mensajeMutable.postValue("Los campos numéricos son inválidos");

        } catch (Exception e) {

            mensajeMutable.postValue("Error: " + e.getMessage());
        }
    }

    private byte[] transformarImagen() {

        try {

            Uri uri = imagenMutable.getValue();

            if (uri == null) {
                return new byte[]{};
            }
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();

        } catch (Exception e) {
            mensajeMutable.postValue("Error al procesar la imagen");
            return new byte[]{};
        }
    }

    public void resetLimpiar() {
        limpiarMutable.setValue(false);
    }

}