package com.app.laperla;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterFirstFragment extends Fragment {

    private RegisterViewModel viewModel;

    private EditText etNombre;
    private EditText etApellido;
    private EditText etTelefono;
    private EditText etDireccion;

    public RegisterFirstFragment() {
        // Required empty public constructor
    }

    public static RegisterFirstFragment newInstance() {
        return new RegisterFirstFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener una instancia del ViewModel compartido
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_first, container, false);

        // Obtener referencias a las vistas
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);

        // Configurar los eventos de los botones
        setupEventListeners(view);

        return view;
    }

    private void setupEventListeners(View view) {
        Button btnSiguiente = view.findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(v -> {
            if (!validarCampos()) {
                // Algún campo no es válido
                return;
            }

            // Actualizar las propiedades del ViewModel con los datos ingresados por el usuario
            viewModel.setNombre(etNombre.getText().toString());
            viewModel.setApellido(etApellido.getText().toString());
            viewModel.setTelefono(etTelefono.getText().toString());
            viewModel.setDireccion(etDireccion.getText().toString());

            // Navegar al siguiente fragmento
            RegisterSecondFragment secondFragment = new RegisterSecondFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, secondFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private boolean validarCampos() {
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String telefono = etTelefono.getText().toString();
        String direccion = etDireccion.getText().toString();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            // Algún campo está vacío
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}