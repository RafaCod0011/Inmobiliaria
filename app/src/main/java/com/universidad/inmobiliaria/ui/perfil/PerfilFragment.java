package com.universidad.inmobiliaria.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentPerfilBinding;
import com.universidad.inmobiliaria.databinding.ItemTransformBinding;
import com.universidad.inmobiliaria.modelo.Propietario;

import java.util.Arrays;
import java.util.List;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);


        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        vm.getPropietarioMutable().observe(getViewLifecycleOwner(), new Observer<Propietario>(){
            @Override
            public void onChanged(Propietario propietario) {

                binding.tvApellido.setText(propietario.getApellido());
                binding.tvClave.setText(propietario.getClave());
                binding.tvDni.setText(String.valueOf(propietario.getDni()));
                binding.tvEmail.setText(propietario.getEmail());
                binding.tvIdPropietario.setText(String.valueOf(propietario.getIdPropietario()));
                binding.tvNombre.setText(propietario.getNombre());
                binding.tvTelefono.setText(String.valueOf(propietario.getTelefono()));

            }
        });

        vm.cargarPerfil();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}