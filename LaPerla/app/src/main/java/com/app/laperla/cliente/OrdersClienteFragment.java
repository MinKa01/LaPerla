package com.app.laperla.cliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.Pedido;
import com.app.laperla.PedidosAdapter;
import com.app.laperla.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersClienteFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<Pedido> listaPedidos;
    private PedidosAdapter pedidosAdapter;
    private DatabaseHelper dbHelper;

    public OrdersClienteFragment() {
        // Required empty public constructor
    }

    public static OrdersClienteFragment newInstance() {
        return new OrdersClienteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_cliente, container, false);

        // Obtén el RecyclerView del layout
        recyclerView = view.findViewById(R.id.recyclerViewPedidos);

        // Configura el LinearLayoutManager para mostrar los elementos en una lista vertical
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        // Crea una instancia del PedidosAdapter y configúralo en el RecyclerView
        pedidosAdapter = new PedidosAdapter(listaPedidos);
        recyclerView.setAdapter(pedidosAdapter);

        // Inicializar la lista de pedidos
        listaPedidos = new ArrayList<>();

        // Crea una instancia del PedidosAdapter y configúralo en el RecyclerView
        pedidosAdapter = new PedidosAdapter(listaPedidos);
        recyclerView.setAdapter(pedidosAdapter);

        // Obtener los pedidos del cliente
        listaPedidos = obtenerPedidosCliente();

        // Actualizar el adaptador con los pedidos obtenidos
        pedidosAdapter.setPedidos(listaPedidos);

        return view;
    }

    private List<Pedido> obtenerPedidosCliente() {
        // Obtén el ID del usuario desde SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        // Realizar la consulta a la base de datos para obtener los pedidos del cliente
        List<Pedido> listaPedidos = new ArrayList<>();

        // Obtener los pedidos del cliente mediante una consulta con INNER JOIN
        String sqlConsulta = "SELECT c.fechaCom, COUNT(dc.idDetalle) AS cantidadProductos, c.totalCom " +
                "FROM compra c " +
                "INNER JOIN detallecompra dc ON c.idCompra = dc.idCompra " +
                "WHERE c.idUsuario = " + usuarioId + " " +
                "GROUP BY c.idCompra";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlConsulta, null);

        if (cursor != null && cursor.moveToFirst()) {
            int fechaIndex = cursor.getColumnIndex("fechaCom");
            int cantidadProductosIndex = cursor.getColumnIndex("cantidadProductos");
            int totalIndex = cursor.getColumnIndex("totalCom");

            do {
                String fecha = cursor.getString(fechaIndex);
                int cantidadProductos = cursor.getInt(cantidadProductosIndex);
                double total = cursor.getDouble(totalIndex);

                Pedido pedido = new Pedido(fecha, cantidadProductos, total);
                listaPedidos.add(pedido);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Invertir el orden de los pedidos
        Collections.reverse(listaPedidos);

        // Cerrar la base de datos
        db.close();

        return listaPedidos;
    }

}
