package com.universidad.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.universidad.inmobiliaria.databinding.FragmentNuevoInmuebleBinding;

public class NuevoInmuebleFragment extends Fragment {

    private NuevoInmuebleViewModel vm;
    private FragmentNuevoInmuebleBinding binding;
    private ActivityResultLauncher<String> seleccionarImagen;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNuevoInmuebleBinding.inflate(inflater,container, false);

        vm = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);

        // Launcher galeria
        seleccionarImagen = registerForActivityResult(new ActivityResultContracts.GetContent(),uri -> {
            if (uri != null) {
                vm.setImagen(uri);
            }
        });

        vm.getImagenMutable().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                binding.ivImagen.setImageURI(uri);
            }
        });

        vm.getMensajeMutable().observe(getViewLifecycleOwner(), mensaje -> {
                if (mensaje != null) {
                    Toast.makeText(getContext(), mensaje,Toast.LENGTH_SHORT                  ).show();
                }
            });

        binding.ivImagen.setOnClickListener(v -> {
            seleccionarImagen.launch("image/*");
        });

        binding.btnGuardarInmueble.setOnClickListener(v -> {
            vm.guardarInmueble(
                    binding.etAmbientes.getText().toString(),
                    binding.etDireccion.getText().toString(),
                    binding.etSuperficie.getText().toString(),
                    binding.etLatitud.getText().toString(),
                    binding.etLongitud.getText().toString(),
                    binding.etValor.getText().toString(),
                    binding.etUso.getText().toString(),
                    binding.etTipo.getText().toString(),
                    binding.cbDisponible.isChecked()
            );
        });

        return binding.getRoot();
    }
}