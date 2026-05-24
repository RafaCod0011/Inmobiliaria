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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observador para los datos del inmueble
        vm.getUInmuebleMutable().observe(getViewLifecycleOwner(), inmueble -> {
            if (inmueble != null) {
                cargarDatosInmueble(inmueble);
            }
        });

        // Observador para el texto dinámico de disponibilidad
        vm.getTextoDisponibilidad().observe(getViewLifecycleOwner(), texto -> {
            if (texto != null) {
                binding.tvTextoDisponibilidad.setText(texto);
                binding.tvTextoDisponibilidad.setVisibility(View.VISIBLE);
            }
        });

        // Observador para mensajes
        vm.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar datos del inmueble desde el Bundle
        vm.cargarDetalleInmueble(getArguments());

        // Listener del checkbox
        binding.cbDisponibleInmueble.setOnClickListener(v -> {
            vm.cambiarDisponibilidad(binding.cbDisponibleInmueble.isChecked());
        });
    }

    private void cargarDatosInmueble(Inmueble inmueble) {
        Glide.with(this)
                .load(ApiClient.BASE_URL + inmueble.getImagen())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.ivImagenInmueble);

        binding.etAmbientesInmueble.setText(String.valueOf(inmueble.getAmbientes()));
        binding.etDireccionInmueble.setText(inmueble.getDireccion());
        binding.etSuperficieInmueble.setText(String.valueOf(inmueble.getSuperficie()));
        binding.etLatitudInmueble.setText(String.valueOf(inmueble.getLatitud()));
        binding.etLongitudInmueble.setText(String.valueOf(inmueble.getLongitud()));
        binding.etValorInmueble.setText(String.valueOf(inmueble.getValor()));
        binding.etUsoInmueble.setText(inmueble.getUso());
        binding.etTipoInmueble.setText(inmueble.getTipo());
        binding.cbDisponibleInmueble.setChecked(inmueble.isDisponible());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}