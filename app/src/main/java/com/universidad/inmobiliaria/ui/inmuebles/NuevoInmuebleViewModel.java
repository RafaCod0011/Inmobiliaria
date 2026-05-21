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

    public void setImagen(Uri uri) {
        imagenMutable.setValue(uri);
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

        try{
            if (!(ambientes.isEmpty() || direccion.isEmpty() || superficie.isEmpty() || latitud.isEmpty()
                    || longitud.isEmpty() || valor.isEmpty() || uso.isEmpty() || tipo.isEmpty())){

                Inmueble i = new Inmueble();
                i.setAmbientes(Integer.parseInt(ambientes));
                i.setDireccion(direccion);
                i.setSuperficie(Integer.parseInt(superficie));
                i.setLatitud(Double.parseDouble(latitud));
                i.setLongitud(Double.parseDouble(longitud));
                i.setValor(Double.parseDouble(valor));
                i.setUso(uso);
                i.setTipo(tipo);

                byte[] imagen = transformarImagen();
                if (imagen.length == 0) {
                    Toast.makeText(getApplication(), "Debe ingresar imagen", Toast.LENGTH_LONG).show();
                    return;
                }

                String inmuebleJson = new Gson().toJson(i);
                RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagen);

                MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);

                ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
                String token = ApiClient.usarToken(getApplication());

                Call<Inmueble> call = servicio.cargarInmueble(token, imagenPart, inmuebleBody);
                call.enqueue(new Callback<>() {

                    @Override
                    public void onFailure(Call<Inmueble> call, Throwable t) {
                        Toast.makeText(getApplication(), "Error al cargar inmueble", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Inmueble cargado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), "Error al cargar inmueble", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





            } else {
                Toast.makeText(getApplication(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e){

        }
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Intent data = result.getData();
            Uri uri = data.getData();
            Log.d("salada", uri.toString());
            imagenMutable.setValue(uri);
        }
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = imagenMutable.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            Toast.makeText(getApplication(), "Debe ingresar una foto", Toast.LENGTH_LONG).show();
            return new byte[]{};
        }

    }


}