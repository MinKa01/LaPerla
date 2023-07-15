package com.app.laperla;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterSecondFragment extends Fragment {

    private RegisterViewModel viewModel;
    private DatabaseHelper dbHelper;

    private EditText etCorreo;
    private EditText etClave;
    private EditText etRepetirClave;

    public RegisterSecondFragment() {
        // Required empty public constructor
    }

    public static RegisterSecondFragment newInstance() {
        return new RegisterSecondFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener una instancia del ViewModel compartido
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_second, container, false);

        // Obtener referencias a las vistas
        etCorreo = view.findViewById(R.id.etCorreo);
        etClave = view.findViewById(R.id.etClave);
        etRepetirClave = view.findViewById(R.id.etRepetirClave);

        // Configurar los eventos de los botones
        setupEventListeners(view);

        return view;
    }

    private void setupEventListeners(View view) {
        Button btnAnterior = view.findViewById(R.id.btnAnterior);
        btnAnterior.setOnClickListener(v -> {
            // Navegar al fragmento anterior
            getParentFragmentManager().popBackStack();
        });

        Button btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String correo = etCorreo.getText().toString();
        String clave = etClave.getText().toString();
        String repetirClave = etRepetirClave.getText().toString();

        if (correo.isEmpty() || clave.isEmpty() || repetirClave.isEmpty()) {
            // Algún campo está vacío
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarCorreo(correo)) {
            // El correo no es válido
            Toast.makeText(requireContext(), "El correo no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarContrasena(clave)) {
            // La contraseña no es válida
            Toast.makeText(requireContext(), "La contraseña no es válida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!clave.equals(repetirClave)) {
            // Las contraseñas no coinciden
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener los datos del usuario desde el ViewModel
        String nombre = viewModel.getNombre().getValue();
        String apellido = viewModel.getApellido().getValue();
        String telefono = viewModel.getTelefono().getValue();
        String direccion = viewModel.getDireccion().getValue();

        // Registrar al usuario en la base de datos como cliente
        dbHelper.registrarUsuario(nombre, apellido, telefono, direccion, correo, clave, "cliente");

        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
        
        // Navegar a la actividad de inicio de sesión
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Finalizar la actividad actual
        requireActivity().finish();
    }

    private boolean validarCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            return false;
        }
        String regex = "^[\\w-]{6,}@(gmail|hotmail)\\.com$";
        return correo.matches(regex);
    }

    private boolean validarContrasena(String clave) {
        if (clave == null || clave.isEmpty()) {
            return false;
        }
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        return clave.matches(regex);
    }
}