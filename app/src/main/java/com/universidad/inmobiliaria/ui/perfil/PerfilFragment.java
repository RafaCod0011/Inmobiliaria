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

import com.universidad.inmobiliaria.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;

    private boolean modoEdicion = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observer para actualizar los datos del perfil
        vm.getPropietarioMutable().observe(getViewLifecycleOwner(), propietario -> {

            if (propietario != null) {
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(String.valueOf(propietario.getDni()));
                binding.etTelefono.setText(String.valueOf(propietario.getTelefono()));
                binding.etEmail.setText(propietario.getEmail());
            }
        });

        vm.getMensajeMutable().observe(getViewLifecycleOwner(),mensaje -> {
            if (mensaje != null) {
                Toast.makeText(requireContext(), mensaje,Toast.LENGTH_SHORT ).show();
                vm.limpiarMensaje();
            }

        });

        vm.getPerfilActualizadoMutable().observe(getViewLifecycleOwner(), actualizado -> {
            if(Boolean.TRUE.equals(actualizado)){
                bloquearCampos();
                modoEdicion = false;
                binding.btnEditar.setText( "Editar Perfil");
                vm.resetPerfilActualizado();
            }
        });

        // Cargar los datos del propietario
        vm.cargarPerfil();

        bloquearCampos();


        binding.btnEditar.setOnClickListener(v -> {
            if(!modoEdicion){
                habilitarCampos();
                binding.btnEditar.setText("Guardar");
                modoEdicion = true;
            }else{
                guardarPerfil();
            }
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

    private void habilitarCampos() {

        binding.etNombre.setEnabled(true);
        binding.etApellido.setEnabled(true);
        binding.etTelefono.setEnabled(true);
        binding.etEmail.setEnabled(true);
        binding.etDni.setEnabled(true);
    }

    private void bloquearCampos() {

        binding.etNombre.setEnabled(false);
        binding.etApellido.setEnabled(false);
        binding.etTelefono.setEnabled(false);
        binding.etEmail.setEnabled(false);
        binding.etDni.setEnabled(false);
    }
    private void guardarPerfil() {

        String nombre = binding.etNombre.getText().toString().trim();
        String apellido = binding.etApellido.getText().toString().trim();
        String dni = binding.etDni.getText().toString().trim();
        String telefono = binding.etTelefono.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        vm.guardarPerfil(nombre,apellido,dni,telefono,email);

    }

}