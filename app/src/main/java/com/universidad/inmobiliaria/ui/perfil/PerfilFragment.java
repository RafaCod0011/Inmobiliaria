package com.universidad.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.universidad.inmobiliaria.databinding.FragmentPerfilBinding;
import com.universidad.inmobiliaria.modelo.Propietario;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        // Observer para actualizar los datos del perfil
        vm.getPropietarioMutable().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                binding.tvNombre.setText(propietario.getNombre());
                binding.tvApellido.setText(propietario.getApellido());
                binding.tvDni.setText(String.valueOf(propietario.getDni()));
                binding.tvTelefono.setText(String.valueOf(propietario.getTelefono()));
                binding.tvEmail.setText(propietario.getEmail());
            }
        });

        // Cargar los datos del propietario
        vm.cargarPerfil();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuración de botones (por ahora solo placeholders)
        binding.btnEditar.setOnClickListener(v -> {
            // TODO: Implementar edición de perfil (próxima entrega)
        });

        binding.btnCambiarClave.setOnClickListener(v -> {
            // TODO: Implementar cambio de contraseña (próxima entrega)
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}