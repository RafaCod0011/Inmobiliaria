package com.universidad.inmobiliaria.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.universidad.inmobiliaria.databinding.FragmentDetalleInquilinoBinding;
import com.universidad.inmobiliaria.modelo.Contrato;
import com.universidad.inmobiliaria.modelo.Inquilino;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding binding;
    private DetalleInquilinoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos usando ViewBinding respetando el XML que pasaste
        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializamos el ViewModel conectado a este Fragment
        vm = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        // Deshabilitamos la edición de los campos (ya que es una pantalla de solo detalle)
        bloquearCamposEdicion();

        // Configuramos la escucha de los LiveData
        configurarObservadores();

        // Recuperamos el ID del inmueble que pasamos desde el fragment anterior
        if (getArguments() != null) {
            int idInmueble = getArguments().getInt("idInmueble", -1);
            if (idInmueble != -1) {
                // Le pedimos al ViewModel que busque la información
                vm.obtenerContrato(idInmueble);
            } else {
                Toast.makeText(requireContext(), "Error al identificar el inmueble.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void configurarObservadores() {
        // Observamos cuando el ViewModel consiga con éxito el Contrato y su Inquilino anidado
        vm.getContratoMutable().observe(getViewLifecycleOwner(), contrato -> {
            if (contrato != null && contrato.getInquilino() != null) {
                Inquilino inquilino = contrato.getInquilino();

                // Mapeamos los campos usando las IDs
                binding.etNombre.setText(inquilino.getNombre());
                binding.etApellido.setText(inquilino.getApellido());
                binding.etDni.setText(inquilino.getDni());
                binding.etTelefono.setText(inquilino.getTelefono());
                binding.etEmail.setText(inquilino.getCorreo());
            }
        });

        // Observamos errores por si la API falla o no hay internet
        vm.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        vm.getCargando().observe(getViewLifecycleOwner(), cargando -> {
        });
    }

    private void bloquearCamposEdicion() {
        // Evita que el usuario edite la información o que se levante el teclado al clickear
        binding.etNombre.setFocusable(false);
        binding.etApellido.setFocusable(false);
        binding.etDni.setFocusable(false);
        binding.etTelefono.setFocusable(false);
        binding.etEmail.setFocusable(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Evitamos pérdidas de memoria limpiando el binding al destruir la vista
        binding = null;
    }
}