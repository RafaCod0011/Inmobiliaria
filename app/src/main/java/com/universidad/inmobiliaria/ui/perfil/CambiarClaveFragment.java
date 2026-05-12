package com.universidad.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.universidad.inmobiliaria.databinding.FragmentCambiarClaveBinding;

public class CambiarClaveFragment extends Fragment {

    private FragmentCambiarClaveBinding binding;
    private CambiarClaveViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCambiarClaveBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CambiarClaveViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observers
        vm.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        vm.getCambioExitoso().observe(getViewLifecycleOwner(), exito -> {
            if (Boolean.TRUE.equals(exito)) {
                Toast.makeText(requireContext(), "¡Contraseña cambiada correctamente!", Toast.LENGTH_LONG).show();
                requireActivity().onBackPressed(); // Volver al Perfil
            }
        });

        // Botón Cambiar Contraseña
        binding.btnCambiarClave.setOnClickListener(v -> {
            String claveActual = binding.etClaveActual.getText().toString().trim();
            String nuevaClave = binding.etNuevaClave.getText().toString().trim();
            String repetirClave = binding.etRepetirClave.getText().toString().trim();

            vm.cambiarClave(claveActual, nuevaClave, repetirClave);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}