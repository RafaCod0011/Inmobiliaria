package com.universidad.inmobiliaria.ui.inmuebles;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.FragmentNuevoInmuebleBinding;

public class NuevoInmuebleFragment extends Fragment {

    private FragmentNuevoInmuebleBinding binding;
    private NuevoInmuebleViewModel vm;

    private ActivityResultLauncher<Intent> selectorImagenLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configurarSpinners();
        configurarSelectorDeImagen();
        configurarObservers();
        configurarBotones();
    }

    private void configurarSpinners() {
        // Ambientes
        ArrayAdapter<CharSequence> adapterAmbientes = ArrayAdapter.createFromResource(
                requireContext(), R.array.tipos_ambientes, android.R.layout.simple_spinner_item);
        adapterAmbientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spAmbientes.setAdapter(adapterAmbientes);

        // Uso
        ArrayAdapter<CharSequence> adapterUso = ArrayAdapter.createFromResource(
                requireContext(), R.array.tipos_uso, android.R.layout.simple_spinner_item);
        adapterUso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUso.setAdapter(adapterUso);

        // Tipo
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(
                requireContext(), R.array.tipos_inmueble, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTipo.setAdapter(adapterTipo);
    }

    private void configurarSelectorDeImagen() {
        selectorImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                        vm.recibirFoto(result.getData());
                    }
                });
    }

    private void configurarObservers() {
        // Observar imagen seleccionada
        vm.getImagenMutable().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                binding.ivImagen.setImageURI(uri);
            }
        });

        // Observar mensajes
        vm.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        // Observar si hay que limpiar el formulario
        vm.getLimpiarFormulario().observe(getViewLifecycleOwner(), limpiar -> {
            if (Boolean.TRUE.equals(limpiar)) {
                limpiarFormulario();
            }
        });
    }

    private void configurarBotones() {
        // Botón para seleccionar imagen
        binding.ivImagen.setOnClickListener(v -> abrirGaleria());

        // Botón Guardar Inmueble
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
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectorImagenLauncher.launch(intent);
    }

    private void limpiarFormulario() {
        binding.spAmbientes.setSelection(0);
        binding.spUso.setSelection(0);
        binding.spTipo.setSelection(0);
        binding.etDireccion.setText("");
        binding.etSuperficie.setText("");
        binding.etLatitud.setText("");
        binding.etLongitud.setText("");
        binding.etValor.setText("");
        binding.ivImagen.setImageResource(R.drawable.ic_menu_camera);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}