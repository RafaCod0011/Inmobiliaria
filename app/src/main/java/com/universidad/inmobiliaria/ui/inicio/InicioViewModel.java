package com.universidad.inmobiliaria.ui.inicio;

import android.app.Application;


import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.universidad.inmobiliaria.R;

public class InicioViewModel extends AndroidViewModel {

    private MutableLiveData<MapaActual> mapaActual;

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<MapaActual> getMapaActual() {
        if (mapaActual == null) {
            mapaActual = new MutableLiveData<>();
        }
        return mapaActual;
    }

    public void cargarMapa() {
        MapaActual mapaActualNuevo = new MapaActual();
        mapaActual.setValue(mapaActualNuevo);
    }

    public class MapaActual implements OnMapReadyCallback {
        LatLng inmobiliaria = new LatLng(-33.322811649042265, -66.31181521867478);

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {

            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            // Configuración del marcador de la inmobiliaria

            MarkerOptions marker = new MarkerOptions()
                    .position(inmobiliaria)
                    .title("Nuestra Inmobiliaria");

            googleMap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(inmobiliaria)
                    .zoom(18)
                    .bearing(0)
                    .tilt(30)
                    .build();
            CameraUpdate cameraUpdate= CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);
        }
    }
}