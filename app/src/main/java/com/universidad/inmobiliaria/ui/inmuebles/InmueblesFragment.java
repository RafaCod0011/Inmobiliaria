package com.universidad.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.universidad.inmobiliaria.databinding.FragmentInmueblesBinding;

import java.util.ArrayList;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding b;
    private InmueblesViewModel vm;
    private AdapterInmueble adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        b = FragmentInmueblesBinding.inflate(inflater, container, false);

        vm = new ViewModelProvider(this)
                .get(InmueblesViewModel.class);

        b.recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2)
        );

        // Adapter vacío inicialmente
        adapter = new AdapterInmueble(new ArrayList<>());

        b.recyclerView.setAdapter(adapter);

        vm.getListaMutable().observe(getViewLifecycleOwner(), lista -> {

            adapter.setLista(lista);

        });

        vm.cargarInmuebles();

        b.btnAltaInmueble.setOnClickListener(v -> {

            Toast.makeText(getContext(),
                    "Ir a Alta de Inmueble",
                    Toast.LENGTH_SHORT).show();
        });

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}