package com.app.laperla.admin;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.R;
import com.app.laperla.Usuario;
import com.app.laperla.UsuarioAdapter;
import com.app.laperla.cliente.MainClienteActivity;

import java.util.ArrayList;
import java.util.List;

public class UsersAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> listaUsuarios;

    private DatabaseHelper dbHelper;

    public UsersAdminFragment() {
        // Required empty public constructor
    }

    public static UsersAdminFragment newInstance() {
        return new UsersAdminFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_admin, container, false);

        // Inicializar RecyclerView y adaptador
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usuarioAdapter = new UsuarioAdapter();
        recyclerView.setAdapter(usuarioAdapter);

        // Obtener la lista de usuarios desde la base de datos o cualquier otra fuente de datos
        listaUsuarios = obtenerListaUsuarios();

        // Configurar el listener para los eventos de clic en los elementos de la lista
        usuarioAdapter.setOnItemClickListener(new UsuarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Usuario usuario) {
                // Lógica para manejar el clic en un usuario

            }

            @Override
            public void onEliminarClick(Usuario usuario) {
                // Lógica para manejar el clic en el botón de eliminar
                eliminarUsuario(usuario);
            }

            @Override
            public void onEditarClick(Usuario usuario) {
                UserEditAdminFragment fragment = UserEditAdminFragment.newInstance(usuario.getId());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit();

                // Ocultar las elementos del activity
                Activity activity = getActivity();
                if (activity instanceof MainAdminActivity) {
                    ((MainAdminActivity) activity).hideOptionsBottomBar();
                }
            }

        });

        // Actualizar la lista de usuarios en el adaptador
        usuarioAdapter.setListaUsuarios(listaUsuarios);

        Button btnAgregarUsuario = view.findViewById(R.id.btnAgregarUsuario);
        btnAgregarUsuario.setOnClickListener(v -> {

            UsersAddAdminFragment usersAddAdminFragment = new UsersAddAdminFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, usersAddAdminFragment)
                    .addToBackStack(null)
                    .commit();

            // Ocultar las elementos del activity
            Activity activity = getActivity();
            if (activity instanceof MainAdminActivity) {
                ((MainAdminActivity) activity).hideOptionsBottomBar();
            }
        });

        return view;
    }

    private List<Usuario> obtenerListaUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();

        // Obtener una instancia del DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());

        // Obtener una referencia a la base de datos en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Definir la consulta SQL para obtener todos los usuarios
        String sql = "SELECT idUsuario, nombreUsu, apellidoUsu, correoUsu FROM usuario";

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery(sql, null);


        // Verificar si las columnas existen en el cursor
        int columnIndexId = cursor.getColumnIndexOrThrow("idUsuario");
        int columnIndexNombre = cursor.getColumnIndexOrThrow("nombreUsu");
        int columnIndexApellido = cursor.getColumnIndexOrThrow("apellidoUsu");
        int columnIndexCorreo = cursor.getColumnIndexOrThrow("correoUsu");

        // Recorrer el cursor y agregar los usuarios a la lista
        if (cursor.moveToFirst()) {
            do {
                int idUsuario = cursor.getInt(columnIndexId);
                String nombreUsu = cursor.getString(columnIndexNombre);
                String apellidoUsu = cursor.getString(columnIndexApellido);
                String correoUsu = cursor.getString(columnIndexCorreo);

                // Crear un objeto Usuario y agregarlo a la listaUsuarios
                Usuario usuario = new Usuario(idUsuario, nombreUsu, apellidoUsu, correoUsu);
                listaUsuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        return listaUsuarios;
    }

    private void eliminarUsuario(Usuario usuario) {
        // Eliminar el usuario de la lista
        listaUsuarios.remove(usuario);
        usuarioAdapter.notifyDataSetChanged();

        // Eliminar el usuario de la base de datos
        dbHelper.eliminarUsuario(usuario.getId());
    }
}
