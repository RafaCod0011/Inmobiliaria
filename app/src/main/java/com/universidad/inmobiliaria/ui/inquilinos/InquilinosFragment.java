package com.universidad.inmobiliaria.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.universidad.inmobiliaria.databinding.FragmentInquilinosBinding;
import com.universidad.inmobiliaria.ui.inmuebles.AdapterInmueble;

public class InquilinosFragment extends Fragment {

    private FragmentInquilinosBinding binding;
    private InquilinosViewModel vm;
    private AdapterInmueble adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       binding= FragmentInquilinosBinding.inflate(inflater, container, false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm= new ViewModelProvider(this).get(InquilinosViewModel.class);

        configurarRecyclerView();
        configurarObservadores();

        vm.cargarInmuebles();
    }

    private void configurarRecyclerView() {
        adapter = new AdapterInmueble(null);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        adapter.setOnItemClickListener(inmueble -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idInmueble", inmueble.getIdInmueble());
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_inquilinos_to_detalleInquilinoFragment, bundle);
        });

    }
    private void configurarObservadores() {
        // Observar la lista de inmuebles
        vm.getListaMutable().observe(getViewLifecycleOwner(), inmuebles -> {
            if (inmuebles != null) {
                adapter.setLista(inmuebles);
            }
        });

        // Observar errores
        vm.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        // Observar estado de carga (opcional - podemos agregar un ProgressBar después)
        vm.getCargando().observe(getViewLifecycleOwner(), cargando -> {
            // binding.progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}