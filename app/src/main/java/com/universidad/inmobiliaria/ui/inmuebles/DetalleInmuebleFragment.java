package com.universidad.inmobiliaria.ui.inmuebles;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;

public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel vm;
    private FragmentDetalleInmuebleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= FragmentDetalleInmuebleBinding.inflate(inflater,container,false);
        vm = new ViewModelProvider(requireActivity()).get(DetalleInmuebleViewModel.class);

        vm.getUInmuebleMutable().observe(getViewLifecycleOwner(),new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                Glide.with(getContext())
                        .load(ApiClient.BASE_URL + inmueble.getImagen())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(binding.ivImagenInmueble);

                binding.etAmbientesInmueble.setText(String.valueOf(inmueble.getAmbientes()));
                binding.etSuperficieInmueble.setText(String.valueOf(inmueble.getSuperficie()));
                binding.etDireccionInmueble.setText(inmueble.getDireccion());
                binding.etLatitudInmueble.setText(String.valueOf(inmueble.getLatitud()));
                binding.etLongitudInmueble.setText(String.valueOf(inmueble.getLongitud()));
                binding.etValorInmueble.setText(String.valueOf(inmueble.getValor()));
                binding.etUsoInmueble.setText(inmueble.getUso());
                binding.etTipoInmueble.setText(inmueble.getTipo());
                binding.cbDisponibleInmueble.setChecked(inmueble.isDisponible());
            }
        });

        // Mostrar texto de disponibilidad.
        vm.getTextoDisponibilidad().observe(getViewLifecycleOwner(), message -> {
            if(message != null && !message.isEmpty()){
                binding.tvTextoDisponibilidad.setVisibility(View.VISIBLE);
                binding.tvTextoDisponibilidad.setText(message);
            }else{
                binding.tvTextoDisponibilidad.setVisibility(View.GONE);
            }

        });

        // Valor del bundle recibido a traves de getArguments.
        vm.cargarDetalleInmueble(getArguments());

        // Boton para cambiar disponibilidad.
        binding.cbDisponibleInmueble.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Actualizando estado......", Toast.LENGTH_SHORT).show();
            vm.cambiarDisponibilidad(binding.cbDisponibleInmueble.isChecked());
        });

        return binding.getRoot();
    }


}