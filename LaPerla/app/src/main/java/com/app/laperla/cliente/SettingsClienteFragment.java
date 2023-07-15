package com.app.laperla.cliente;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.LoginActivity;
import com.app.laperla.R;
import com.app.laperla.admin.EditProfileAdminFragment;
import com.app.laperla.admin.MainAdminActivity;

public class SettingsClienteFragment extends Fragment {

    private DatabaseHelper dbHelper;

    public SettingsClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_cliente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupEventListeners();
        showUserInfo();
    }

    private void setupEventListeners() {
        Button btnCerrarSesion = getView().findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

        Button btnEditarPerfil = getView().findViewById(R.id.btnEditarPerfil);

        btnEditarPerfil.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerCliente, new EditProfileClienteFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            Activity activity = getActivity();
            if (activity instanceof MainClienteActivity) {
                ((MainClienteActivity) activity).hideOptionsBottomBar();
            }
        });
    }

    private void showUserInfo() {
        // Recuperar el ID del usuario desde SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        // Obtener la información del usuario desde la base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT nombreUsu, apellidoUsu, correoUsu FROM usuario WHERE idUsuario = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String apellido = cursor.getString(1);
            String correo = cursor.getString(2);

            // Mostrar la información del usuario en la interfaz
            TextView tvNomApel = getView().findViewById(R.id.tvNomApel);
            tvNomApel.setText(getString(R.string.adminFsNomApe, nombre, apellido));

            TextView tvEmail = getView().findViewById(R.id.tvEmail);
            tvEmail.setText(correo);
        }
        cursor.close();
    }


    private void cerrarSesion() {
        // Eliminar el ID del usuario de SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("usuario_id");
        editor.apply();

        // Derivar a la actividad de inicio de sesión
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Finalizar la actividad actual
        getActivity().finish();
    }

}