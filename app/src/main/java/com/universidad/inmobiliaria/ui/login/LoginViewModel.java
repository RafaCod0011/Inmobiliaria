package com.universidad.inmobiliaria.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> errorUsuario = new MutableLiveData<>();
    private MutableLiveData<String> errorPassword = new MutableLiveData<>();

    private MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();

    public LiveData<String> getErrorUsuario() { return errorUsuario; }
    public LiveData<String> getErrorPassword() { return errorPassword; }
    public LiveData<Boolean> getLoginExitoso() { return loginExitoso; }


    public void verificarDatos(String usuario, String password) {
        errorUsuario.setValue(null);
        errorPassword.setValue(null);

        if (usuario.isEmpty()) {
            errorUsuario.setValue("Ingrese su usuario");
        } else if (password.isEmpty()) {
            errorPassword.setValue("Ingrese la contraseña");
        } else {
            loginExitoso.setValue(true);
        }
    }
}