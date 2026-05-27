package com.universidad.inmobiliaria.ui.contratos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentDetalleContratoBinding;
import com.universidad.inmobiliaria.modelo.Contrato;
import com.universidad.inmobiliaria.modelo.Inmueble;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetalleContratoFragment extends Fragment {

    private DetalleContratoViewModel vm;
    private FragmentDetalleContratoBinding binding;
    private Contrato contratoActual;

    public static DetalleContratoFragment newInstance() {
        return new DetalleContratoFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);

        vm = new ViewModelProvider(this).get(DetalleContratoViewModel.class);

        //Deshabilitar el boton hasta que cargue el contrato
        binding.btnPagos.setEnabled(false);

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bloquearCampos();

        vm.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        vm.getContratoMutable().observe(getViewLifecycleOwner(), contrato -> {

            if (contrato != null) {
                contratoActual = contrato;
                cargarDatosContrato(contrato);
                //Habilitar el boton cuando cargue el contrato
                binding.btnPagos.setEnabled(true);
            }

        });

        if (getArguments() != null) {
            Inmueble inmueble;
            inmueble = getArguments().getSerializable("inmueble", Inmueble.class);
            if (inmueble != null) {
                vm.obtenerContratoPorInmueble(inmueble.getIdInmueble());
            }
        }

        binding.btnPagos.setOnClickListener(v -> {
            if (contratoActual != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("idContrato", contratoActual.getIdContrato());
                Navigation.findNavController(v)
                        .navigate(R.id.action_detalleContratoFragment_to_pagosFragment, bundle);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void cargarDatosContrato(Contrato contrato) {

        binding.etFechaInicio.setText(vm.formatearFecha(contrato.getFechaInicio()));
        binding.etFechaFin.setText(vm.formatearFecha(contrato.getFechaFinalizacion()));
        binding.etMontoAlquiler.setText(vm.formatearMoneda(contrato.getMontoAlquiler()));
        if (contrato.getInquilino() != null) {
            String inquilino =contrato.getInquilino().getNombre()+ " "+ contrato.getInquilino().getApellido();
            binding.etInquilino.setText(inquilino);
        }
        if (contrato.getInmueble() != null) {
            binding.etInmueble.setText(contrato.getInmueble().getDireccion());
        }
    }

    private void bloquearCampos(){
        binding.etFechaInicio.setFocusable(false);
        binding.etFechaFin.setFocusable(false);
        binding.etMontoAlquiler.setFocusable(false);
        binding.etInquilino.setFocusable(false);
        binding.etInmueble.setFocusable(false);
    }

}