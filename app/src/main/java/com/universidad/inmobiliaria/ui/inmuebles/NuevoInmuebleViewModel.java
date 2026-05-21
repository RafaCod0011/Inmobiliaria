package com.universidad.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
            String tipo,
            boolean disponible
    ) {

        // API

        mensajeMutable.setValue("Método guardar");
    }
}