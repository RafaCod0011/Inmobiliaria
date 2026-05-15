package com.universidad.inmobiliaria;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.universidad.inmobiliaria.databinding.ActivityMainBinding;
import com.universidad.inmobiliaria.request.ApiClient;
import com.universidad.inmobiliaria.ui.perfil.PerfilViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private PerfilViewModel perfilViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio,
                R.id.nav_perfil,
                R.id.nav_inmuebles,
                R.id.nav_inquilinos,
                R.id.nav_contratos
        )
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
// Menu lateral
        // Menu lateral
        if (binding.navView != null) {
            NavigationUI.setupWithNavController(binding.navView, navController);

            binding.navView.setNavigationItemSelectedListener(item -> {
                if (item.getItemId() == R.id.nav_logout) {
                    mostrarDialogLogout();
                    binding.drawerLayout.closeDrawers();
                    return true;
                }

                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    binding.drawerLayout.closeDrawers();
                }
                return handled;
            });
        }

//Menu inferior
        if (binding.appBarMain.contentMain.bottomNavView != null) {
            NavigationUI.setupWithNavController(binding.appBarMain.contentMain.bottomNavView, navController);
        }

        // CARGAR HEADER
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        View headerView = binding.navView.getHeaderView(0);
        TextView tvNombreHeader = headerView.findViewById(R.id.tvHeaderNombre);
        TextView tvEmailHeader = headerView.findViewById(R.id.tvHeaderEmail);

        perfilViewModel.getPropietarioMutable().observe(this, propietario -> {
            if (propietario != null) {
                tvNombreHeader.setText(propietario.getNombre());
                tvEmailHeader.setText(propietario.getEmail());
            }
        });

        perfilViewModel.cargarPerfil();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);

        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
             getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void mostrarDialogLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Está seguro de que desea cerrar la sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    // ====================== LIMPIAR TOKEN ======================
                    ApiClient.borrarToken(this);   // ← Nuevo método que vamos a crear

                    // Ir al Login y limpiar la pila de actividades
                    Intent intent = new Intent(MainActivity.this, com.universidad.inmobiliaria.ui.login.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        String token = ApiClient.usarToken(this);
        if (token == null || token.isEmpty()) {
            // Si no hay token, volver al login
            Intent intent = new Intent(this, com.universidad.inmobiliaria.ui.login.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}