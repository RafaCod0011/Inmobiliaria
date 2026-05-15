package com.universidad.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> listaMutable;

    public InmueblesViewModel(@NonNull Application application) {
        super(application);

        listaMutable = new MutableLiveData<>();
    }

    public LiveData<List<Inmueble>> getListaMutable() {

        if(listaMutable == null){
            listaMutable = new MutableLiveData<>();
        }

        return listaMutable;
    }

    public void cargarInmuebles(){

        String token = ApiClient.usarToken(getApplication());

        ApiClient.getServicio().getInmuebles(token)
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call,
                                           Response<List<Inmueble>> response) {

                        if(response.isSuccessful()){

                            listaMutable.postValue(response.body());

                        }else{

                            Log.d("salida", "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call,
                                          Throwable t) {

                        Log.d("salida", t.getMessage());
                    }
                });
    }
}