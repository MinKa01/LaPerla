package com.app.laperla.admin;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.R;
import com.app.laperla.Usuario;

public class UserEditAdminFragment extends Fragment {

    private EditText etNombre;
    private EditText etApellido;
    private EditText etTelefono;
    private EditText etDireccion;
    private Spinner spinnerRol;
    private EditText etCorreo;
    private EditText etClave;

   public UserEditAdminFragment() {
        // Required empty public constructor
    }

    public static UserEditAdminFragment newInstance(int idUsuario) {
        UserEditAdminFragment fragment = new UserEditAdminFragment();
        Bundle args = new Bundle();
        args.putInt("idUsuario", idUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_edit_admin, container, false);

        spinnerRol = view.findViewById(R.id.spinnerRol);

        String[] roles = {"Cliente", "Administrador"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);

        // Obtener los campos de texto y el spinner
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        etCorreo = view.findViewById(R.id.etCorreo);
        etClave = view.findViewById(R.id.etClave);

        // Obtener el ID del usuario del Bundle
        Bundle args = getArguments();
        if (args != null) {
            int idUsuario = args.getInt("idUsuario");

            // Obtener los datos del usuario desde la base de datos utilizando su ID
            Usuario usuario = obtenerUsuarioPorId(idUsuario);

            // Mostrar los datos del usuario en los campos correspondientes
            if (usuario != null) {
                etNombre.setText(usuario.getNombre());
                etApellido.setText(usuario.getApellido());
                etTelefono.setText(usuario.getTelefono());
                etDireccion.setText(usuario.getDireccion());
                etCorreo.setText(usuario.getCorreo());
                etClave.setText(usuario.getClave());

                // Seleccionar el rol del usuario en el spinner
                String rol = usuario.getRol();
                if (rol.equals("cliente")) {
                    spinnerRol.setSelection(0);
                } else if (rol.equals("admin")) {
                    spinnerRol.setSelection(1);
                }

            }
        }

        Button btnActualizarUsuario = view.findViewById(R.id.btnActualizarUsuario);
        btnActualizarUsuario.setOnClickListener(v -> actualizarUsuario());

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

    private Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;

        // Obtener una instancia del DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        // Obtener una referencia a la base de datos en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Definir la consulta SQL para obtener el usuario con el ID especificado
        String sql = "SELECT nombreUsu, apellidoUsu, telefonoUsu, direccionUsu, correoUsu, claveUsu, rolUsu FROM usuario WHERE idUsuario = ?";

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idUsuario)});

        // Verificar si se encontró un registro
        if (cursor.moveToFirst()) {
            // Obtener los índices de las columnas
            int columnIndexNombre = cursor.getColumnIndex("nombreUsu");
            int columnIndexApellido = cursor.getColumnIndex("apellidoUsu");
            int columnIndexTelefono = cursor.getColumnIndex("telefonoUsu");
            int columnIndexDireccion = cursor.getColumnIndex("direccionUsu");
            int columnIndexCorreo = cursor.getColumnIndex("correoUsu");
            int columnIndexClave = cursor.getColumnIndex("claveUsu");
            int columnIndexRol = cursor.getColumnIndex("rolUsu");

            // Verificar si todas las columnas existen en el cursor
            if (columnIndexNombre != -1 && columnIndexApellido != -1 && columnIndexTelefono != -1 &&
                    columnIndexDireccion != -1 && columnIndexCorreo != -1 && columnIndexClave != -1 &&
                    columnIndexRol != -1) {
                // Obtener los datos del usuario desde el cursor
                String nombreUsu = cursor.getString(columnIndexNombre);
                String apellidoUsu = cursor.getString(columnIndexApellido);
                String telefonoUsu = cursor.getString(columnIndexTelefono);
                String direccionUsu = cursor.getString(columnIndexDireccion);
                String correoUsu = cursor.getString(columnIndexCorreo);
                String claveUsu = cursor.getString(columnIndexClave);
                String rolUsu = cursor.getString(columnIndexRol);

                // Crear un objeto Usuario con los datos obtenidos
                usuario = new Usuario(idUsuario, nombreUsu, apellidoUsu, telefonoUsu, direccionUsu, correoUsu, claveUsu, rolUsu);
            }
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        return usuario;
    }

    private void actualizarUsuario() {
        // Obtener los valores de los campos de texto y del spinner
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String telefono = etTelefono.getText().toString();
        String direccion = etDireccion.getText().toString();
        String correo = etCorreo.getText().toString();
        String clave = etClave.getText().toString();
        String rolSeleccionado = spinnerRol.getSelectedItem().toString();

        // Asignar el valor correspondiente al campo "rolUsu"
        String rolUsu="";
        if (rolSeleccionado.equals("Cliente")) {
            rolUsu = "cliente";
        } else if (rolSeleccionado.equals("Administrador")) {
            rolUsu = "admin";
        }

        // Obtener el ID del usuario del Bundle
        Bundle args = getArguments();
        if (args != null) {
            int idUsuario = args.getInt("idUsuario");

            // Crear un objeto Usuario con los valores obtenidos
            Usuario usuario = new Usuario(idUsuario, nombre, apellido, telefono, direccion, correo, clave, rolUsu);

            // Actualizar el usuario en la base de datos
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
            dbHelper.actualizarUsuario(usuario);

            // Mostrar un mensaje Toast
            Toast.makeText(getActivity(), "Usuario actualizado", Toast.LENGTH_SHORT).show();

            // Volver al fragmento anterior
            requireActivity().getSupportFragmentManager().popBackStack();

            // Mostrar las elementos del activity
            Activity activity = getActivity();
            if (activity instanceof MainAdminActivity) {
                ((MainAdminActivity) activity).showOptionsBottomBar();
            }
        }
    }

}