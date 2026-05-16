package com.universidad.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.universidad.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.universidad.inmobiliaria.modelo.Inmueble;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesViewModel viewModel;
    private AdapterInmueble adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        configurarRecyclerView();
        configurarObservadores();
        configurarBotones();

        // Cargar los inmuebles al entrar al fragment
        viewModel.cargarInmuebles();
    }

    private void configurarRecyclerView() {
        adapter = new AdapterInmueble(null);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerView.setAdapter(adapter);

        // Click en un inmueble → ir a detalle
        adapter.setOnItemClickListener(inmueble -> {
            // TODO: Navegar al detalle
            Toast.makeText(requireContext(),
                    "Abrir detalle de: " + inmueble.getDireccion(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void configurarObservadores() {
        // Observar la lista de inmuebles
        viewModel.getListaMutable().observe(getViewLifecycleOwner(), inmuebles -> {
            if (inmuebles != null) {
                adapter.setLista(inmuebles);
            }
        });

        // Observar errores
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Observar estado de carga (opcional - podemos agregar un ProgressBar después)
        viewModel.getCargando().observe(getViewLifecycleOwner(), cargando -> {
            // binding.progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        });
    }

    private void configurarBotones() {
        // Botón flotante para agregar inmueble
        binding.btnAltaInmueble.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Próximamente: Formulario de Alta", Toast.LENGTH_SHORT).show();

            // Cuando creemos el fragment de alta:
            // NavHostFragment.findNavController(this)
            //     .navigate(R.id.action_nav_inmuebles_to_altaInmuebleFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}