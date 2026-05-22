package com.universidad.inmobiliaria.ui.inmuebles;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentNuevoInmuebleBinding;

public class NuevoInmuebleFragment extends Fragment {

    private NuevoInmuebleViewModel vm;

    private Intent intent;
    private FragmentNuevoInmuebleBinding binding;
    //private ActivityResultLauncher<String> seleccionarImagen;

    private ActivityResultLauncher<Intent> selectorImagen2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNuevoInmuebleBinding.inflate(inflater,container, false);

        vm = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);

        // Launcher galeria
//        seleccionarImagen = registerForActivityResult(new ActivityResultContracts.GetContent(),uri -> {
//            if (uri != null) {
//                vm.setImagen(uri);
//            }
//        });
//
        vm.getImagenMutable().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                binding.ivImagen.setImageURI(uri);
            }
        });

        cargarSpinners();

        vm.getMensajeMutable().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(getContext(), mensaje,Toast.LENGTH_SHORT                  ).show();
            }
        });

        vm.getLimpiarMutable().observe(
                getViewLifecycleOwner(),
                limpiar -> {
                    if(Boolean.TRUE.equals(limpiar)){
                        limpiarPantalla();
                        vm.resetLimpiar();
                    }
                });

        binding.ivImagen.setOnClickListener(v -> {
            //seleccionarImagen.launch("image/*");
            selectorImagen2.launch(intent);
        });

        binding.btnGuardarInmueble.setOnClickListener(v -> {
            vm.guardarInmueble(
                    binding.spAmbientes.getSelectedItem().toString(),
                    binding.etDireccion.getText().toString(),
                    binding.etSuperficie.getText().toString(),
                    binding.etLatitud.getText().toString(),
                    binding.etLongitud.getText().toString(),
                    binding.etValor.getText().toString(),
                    binding.spUso.getSelectedItem().toString(),
                    binding.spTipo.getSelectedItem().toString()
            );

        });

        abrirGaleria();

        return binding.getRoot();
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectorImagen2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult resultado) {
                vm.recibirFoto(resultado);
            }
        });
    }
    private void cargarSpinners() {

        ArrayAdapter<CharSequence> adapterAmbientes =
                ArrayAdapter.createFromResource(requireContext(),R.array.tipos_ambientes, android.R.layout.simple_spinner_item
        );

        adapterAmbientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spAmbientes.setAdapter(adapterAmbientes);

        ArrayAdapter<CharSequence> adapterUso =
                ArrayAdapter.createFromResource(requireContext(),R.array.tipos_uso, android.R.layout.simple_spinner_item
        );
        adapterUso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUso.setAdapter(adapterUso);

        ArrayAdapter<CharSequence> adapterTipo =
                ArrayAdapter.createFromResource(requireContext(),R.array.tipos_inmueble, android.R.layout.simple_spinner_item
        );
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTipo.setAdapter(adapterTipo);
    }

    private void limpiarPantalla() {
        binding.spAmbientes.setSelection(0);
        binding.etDireccion.setText("");
        binding.etSuperficie.setText("");
        binding.etLatitud.setText("");
        binding.etLongitud.setText("");
        binding.etValor.setText("");
        binding.spUso.setSelection(0);
        binding.spTipo.setSelection(0);
        binding.ivImagen.setImageResource(R.drawable.ic_menu_camera);
    }

}