package com.universidad.inmobiliaria.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class InicioViewModel extends ViewModel {

    private MutableLiveData<LatLng> location = new MutableLiveData<>();

    public LiveData<LatLng> getLocation(){
        return location;
    }

    public void cargarUbicacion(){
        LatLng inmobiliaria = new LatLng(-33.322811649042265, -66.31181521867478);
        location.setValue(inmobiliaria);
    }
}
