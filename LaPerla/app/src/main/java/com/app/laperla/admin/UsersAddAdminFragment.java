package com.app.laperla.admin;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.R;
import com.app.laperla.Usuario;

public class UsersAddAdminFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private Spinner spinnerRol;

    public UsersAddAdminFragment() {
        // Required empty public constructor
    }


    public static UsersAddAdminFragment newInstance() {
        return new UsersAddAdminFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_add_admin, container, false);

        // Obtén la referencia al Spinner y configura su adaptador y evento de selección
        spinnerRol = view.findViewById(R.id.spinnerRol);

        String[] roles = {"Cliente", "Administrador"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);

        // Obtén referencias a los otros elementos de tu diseño
        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etApellido = view.findViewById(R.id.etApellido);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etDireccion = view.findViewById(R.id.etDireccion);
        EditText etCorreo = view.findViewById(R.id.etCorreo);
        EditText etClave = view.findViewById(R.id.etClave);
        Button btnRegistrarUsuario = view.findViewById(R.id.btnRegistrarUsuario);

        btnRegistrarUsuario.setOnClickListener(v -> {
            // Obtén los valores de los campos de texto
            String nombre = etNombre.getText().toString();
            String apellido = etApellido.getText().toString();
            String telefono = etTelefono.getText().toString();
            String direccion = etDireccion.getText().toString();
            String correo = etCorreo.getText().toString();
            String clave = etClave.getText().toString();

            // Obtener el valor seleccionado del Spinner
            String rolSeleccionado = spinnerRol.getSelectedItem().toString();

            // Asignar el valor correspondiente al campo "rolUsu"
            String rolUsu="";
            if (rolSeleccionado.equals("Cliente")) {
                rolUsu = "cliente";
            } else if (rolSeleccionado.equals("Administrador")) {
                rolUsu = "admin";
            }

            // Crea un objeto Usuario con los valores obtenidos
            Usuario usuario = new Usuario(nombre, apellido, telefono, direccion, correo, clave, rolUsu);

            // Guarda el usuario en la base de datos o realiza la acción correspondiente
            guardarUsuario(usuario);

            // Mostrar las elementos del activity
            Activity activity = getActivity();
            if (activity instanceof MainAdminActivity) {
                ((MainAdminActivity) activity).showOptionsBottomBar();
            }
        });

        // Volver al fragmento anterior
        ImageView ivAtras = view.findViewById(R.id.ivAtras);
        ivAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();

            // Mostrar las elementos del activity
            Activity activity = getActivity();
            if (activity instanceof MainAdminActivity) {
                ((MainAdminActivity) activity).showOptionsBottomBar();
            }
        });

        return view;
    }

    private void guardarUsuario(Usuario usuario) {

        // Obtener una instancia de la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los valores de los campos
        ContentValues values = new ContentValues();
        values.put("nombreUsu", usuario.getNombre());
        values.put("apellidoUsu", usuario.getApellido());
        values.put("telefonoUsu", usuario.getTelefono());
        values.put("direccionUsu", usuario.getDireccion());
        values.put("correoUsu", usuario.getCorreo());
        values.put("claveUsu", usuario.getClave());
        values.put("rolUsu", usuario.getRol());

        // Insertar los datos en la tabla "usuario"
        long id = db.insert("usuario", null, values);

        if (id != -1) {
            // El usuario se ha guardado exitosamente
            Toast.makeText(getActivity(), "Usuario guardado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            // Hubo un error al guardar el usuario
            Toast.makeText(getActivity(), "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
        }

        // Obtener el FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Retroceder al fragmento anterior
        fragmentManager.popBackStack();
    }

}