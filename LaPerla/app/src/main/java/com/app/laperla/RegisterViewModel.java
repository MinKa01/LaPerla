package com.app.laperla;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<String> nombre = new MutableLiveData<>();
    private final MutableLiveData<String> apellido = new MutableLiveData<>();
    private final MutableLiveData<String> telefono = new MutableLiveData<>();
    private final MutableLiveData<String> direccion = new MutableLiveData<>();

    public void setNombre(String value) {
        nombre.setValue(value);
    }

    public LiveData<String> getNombre() {
        return nombre;
    }

    public void setApellido(String value) {
        apellido.setValue(value);
    }

    public LiveData<String> getApellido() {
        return apellido;
    }

    public void setTelefono(String value) {
        telefono.setValue(value);
    }

    public LiveData<String> getTelefono() {
        return telefono;
    }

    public void setDireccion(String value) {
        direccion.setValue(value);
    }

    public LiveData<String> getDireccion() {
        return direccion;
    }

}
