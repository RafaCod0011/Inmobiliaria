package com.universidad.inmobiliaria.ui.contratos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentPagosBinding;

public class PagosFragment extends Fragment {

    private PagosViewModel vm;
    private FragmentPagosBinding binding;

    public static PagosFragment newInstance() {
        return new PagosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PagosViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdapterPagos adapter = new AdapterPagos(null);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),1));
        binding.recyclerView.setAdapter(adapter);

        vm.getListaPago().observe(getViewLifecycleOwner(), pagos -> {
            if (pagos != null) {
                adapter.setLista(pagos);
            }
        });

        vm.getMensaje().observe(getViewLifecycleOwner(),
                mensaje -> Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show());

        int idContrato = requireArguments().getInt("idContrato");
        vm.obtenerPagosPorContrato(idContrato);

    }
}