package com.app.laperla.admin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.R;
import com.app.laperla.cliente.EditProfileClienteFragment;

import org.jetbrains.annotations.Nullable;

public class EditProfileAdminFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditProfileClienteFragment.OnNombreActualizadoListener onNombreActualizadoListener;

    public EditProfileAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupEventListeners();
        mostrarInfo();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onNombreActualizadoListener = (EditProfileClienteFragment.OnNombreActualizadoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar OnNombreActualizadoListener");
        }
    }

    private void setupEventListeners() {
        ImageView ivAtras = getView().findViewById(R.id.ivAtras);
        ivAtras.setOnClickListener(v -> onBackButtonClick());

        Button btnActualizarPerfil = getView().findViewById(R.id.btnActualizarPerfil);
        btnActualizarPerfil.setOnClickListener(v -> actualizarPerfil());
    }

    private void onBackButtonClick() {
        Activity activity = getActivity();
        if (activity instanceof MainAdminActivity) {
            ((MainAdminActivity) activity).showOptionsBottomBar();
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }

    private void mostrarInfo() {
        // Recuperar el ID del usuario desde SharedPreferences
        int usuarioId = obtenerUsuarioId();

        // Obtener la información del usuario desde la base de datos
        Cursor cursor = obtenerInfoUsuario(usuarioId);

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String apellido = cursor.getString(1);
            String telefono = cursor.getString(2);
            String direccion = cursor.getString(3);
            String correo = cursor.getString(4);
            String clave = cursor.getString(5);

            EditText etNombre = getView().findViewById(R.id.etNombre);
            etNombre.setText(nombre);
            EditText etApellido = getView().findViewById(R.id.etApellido);
            etApellido.setText(apellido);
            EditText etTelefono = getView().findViewById(R.id.etTelefono);
            etTelefono.setText(telefono);
            EditText etDireccion = getView().findViewById(R.id.etDireccion);
            etDireccion.setText(direccion);
            EditText etCorreo = getView().findViewById(R.id.etCorreo);
            etCorreo.setText(correo);
            EditText etClave = getView().findViewById(R.id.etClave);
            etClave.setText(clave);
        }

        cursor.close();
    }

    private void actualizarPerfil() {
        EditText etNombre = getView().findViewById(R.id.etNombre);
        String nombre = etNombre.getText().toString();
        EditText etApellido = getView().findViewById(R.id.etApellido);
        String apellido = etApellido.getText().toString();
        EditText etTelefono = getView().findViewById(R.id.etTelefono);
        String telefono = etTelefono.getText().toString();
        EditText etDireccion = getView().findViewById(R.id.etDireccion);
        String direccion = etDireccion.getText().toString();
        EditText etCorreo = getView().findViewById(R.id.etCorreo);
        String correo = etCorreo.getText().toString();
        EditText etClave = getView().findViewById(R.id.etClave);
        String clave = etClave.getText().toString();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombreUsu", nombre);
        values.put("apellidoUsu", apellido);
        values.put("telefonoUsu", telefono);
        values.put("direccionUsu", direccion);
        values.put("correoUsu", correo);
        values.put("claveUsu", clave);

        int usuarioId = obtenerUsuarioId();

        String selection = "idUsuario = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};
        db.update("usuario", values, selection, selectionArgs);

        // Llamar al método onNombreActualizado() en la instancia de la interfaz
        onNombreActualizadoListener.onNombreActualizado(nombre);

        Toast.makeText(getActivity(), "Información actualizada", Toast.LENGTH_SHORT).show();
    }


    private int obtenerUsuarioId() {
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return prefs.getInt("usuario_id", -1);
    }

    private Cursor obtenerInfoUsuario(int usuarioId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT nombreUsu, apellidoUsu, telefonoUsu, direccionUsu, correoUsu, claveUsu FROM usuario WHERE idUsuario = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};
        return db.rawQuery(sql, selectionArgs);
    }

    public interface OnNombreActualizadoListener {
        void onNombreActualizado(String nuevoNombre);
    }

}