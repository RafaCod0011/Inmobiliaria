package com.universidad.inmobiliaria.ui.inicio;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

            // --- Lógica del Punto Azul (Ubicación) ---
            // Verificamos permisos antes de activar la capa de ubicación
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                googleMap.setMyLocationEnabled(true);
            }

            // Configuración del marcador de la inmobiliaria
            googleMap.addMarker(new MarkerOptions()
                    .position(inmobiliaria)
                    .title("Nuestra Inmobiliaria"));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(inmobiliaria, 15f));
        }
    }
}