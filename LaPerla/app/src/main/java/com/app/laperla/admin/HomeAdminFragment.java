package com.app.laperla.admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.R;

public class HomeAdminFragment extends Fragment {

    private DatabaseHelper dbHelper;
    TextView tvUserCant;
    TextView tvFoodCant;
    TextView tvOrderCant;
    TextView tvAdminCant;
    TextView tvRankingFood;

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mostrarCantCliente();
        mostrarCantProductos();
        mostrarCantCompras();
        mostrarCantAdmins();
        obtenerProductoMasComprado();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        // Obtener referencia
        tvUserCant = view.findViewById(R.id.tvUserCant);
        tvFoodCant = view.findViewById(R.id.tvFoodCant);
        tvOrderCant = view.findViewById(R.id.tvOrderCant);
        tvAdminCant = view.findViewById(R.id.tvAdminCant);
        tvRankingFood = view.findViewById(R.id.tvRankingFood);

        return view;
    }

    private void mostrarCantCliente() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM usuario WHERE rolUsu = 'cliente'";
        Cursor cursor = db.rawQuery(sql, null);

        int cantidadClientes = 0;
        if (cursor.moveToFirst()) {
            cantidadClientes = cursor.getInt(0);
        }
        cursor.close();

        String mensaje = "Clientes: " + cantidadClientes;
        tvUserCant.setText(mensaje);
    }

    private void mostrarCantProductos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM producto";
        Cursor cursor = db.rawQuery(sql, null);

        int cantidadProductos = 0;
        if (cursor.moveToFirst()) {
            cantidadProductos = cursor.getInt(0);
        }
        cursor.close();

        String mensaje = "Productos: " + cantidadProductos;
        tvFoodCant.setText(mensaje);
    }

    private void mostrarCantCompras() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM compra";
        Cursor cursor = db.rawQuery(sql, null);

        int cantidadCompras = 0;
        if (cursor.moveToFirst()) {
            cantidadCompras = cursor.getInt(0);
        }
        cursor.close();

        String mensaje = "Compras: " + cantidadCompras;
        tvOrderCant.setText(mensaje);
    }

    private void mostrarCantAdmins() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM usuario WHERE rolUsu = 'admin'";
        Cursor cursor = db.rawQuery(sql, null);

        int cantidadAdmins = 0;
        if (cursor.moveToFirst()) {
            cantidadAdmins = cursor.getInt(0);
        }
        cursor.close();

        String mensaje = "Admins: " + cantidadAdmins;
        tvAdminCant.setText(mensaje);
    }

    private void obtenerProductoMasComprado() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta para obtener el ID y el nombre del producto con el recuento más alto
        String sql = "SELECT p.idProducto, p.nombreProd, COUNT(dc.idProducto) AS cantidad " +
                "FROM producto p " +
                "LEFT JOIN detallecompra dc ON p.idProducto = dc.idProducto " +
                "GROUP BY p.idProducto, p.nombreProd " +
                "ORDER BY cantidad DESC " +
                "LIMIT 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            int idProductoColumnIndex = cursor.getColumnIndex("idProducto");
            int nombreProductoColumnIndex = cursor.getColumnIndex("nombreProd");
            int cantidadComprasColumnIndex = cursor.getColumnIndex("cantidad");

            int idProducto = cursor.getInt(idProductoColumnIndex);
            String nombreProducto = cursor.getString(nombreProductoColumnIndex);
            int cantidadCompras = cursor.getInt(cantidadComprasColumnIndex);

            String mensaje = "Producto más comprado: " + nombreProducto + "\n(Compras: " + cantidadCompras + ")";
            // Mostrar el mensaje en algún lugar (por ejemplo, en un TextView)
            tvRankingFood.setText(mensaje);
        }

        cursor.close();
    }


}