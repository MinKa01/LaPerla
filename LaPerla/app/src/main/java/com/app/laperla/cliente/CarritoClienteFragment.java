package com.app.laperla.cliente;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.laperla.CarritoAdapter;
import com.app.laperla.DatabaseHelper;
import com.app.laperla.Producto;
import com.app.laperla.R;

import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;


public class CarritoClienteFragment extends Fragment {

    private List<Producto> listaProductosCarrito;
    private CarritoAdapter carritoAdapter;

    private TextView tvSubTotal;
    private TextView tvDescuento;
    private TextView tvTotal;
    private double total;
    private DatabaseHelper dbHelper;

    Button btnRealizarPedido;
    public CarritoClienteFragment() {
        // Required empty public constructor
    }

    public static CarritoClienteFragment newInstance() {
        return new CarritoClienteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperar la lista de productos del carrito desde los argumentos
        if (getArguments() != null) {
            listaProductosCarrito = getArguments().getParcelableArrayList("listaProductosCarrito");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito_cliente, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Obtén el RecyclerView del layout
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCarrito);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvDescuento = view.findViewById(R.id.tvDescuento);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnRealizarPedido = view.findViewById(R.id.btnRealizarPedido);

        // Crea una instancia del CarritoAdapter y configúralo en el RecyclerView
        carritoAdapter = new CarritoAdapter(listaProductosCarrito, this);
        recyclerView.setAdapter(carritoAdapter);

        // Configura el LinearLayoutManager para mostrar los elementos en una lista vertical
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Configurar el ItemTouchHelper para permitir eliminar productos del carrito con un gesto de deslizamiento
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtener la posición del elemento deslizado
                int position = viewHolder.getAdapterPosition();

                // Eliminar el producto del carrito en la posición deslizada
                listaProductosCarrito.remove(position);

                // Notificar al adaptador que se ha eliminado un elemento
                carritoAdapter.notifyItemRemoved(position);

                // Actualizar el total del carrito
                //actualizarTotal();
                actualizarValores();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Imprimir un mensaje de log para verificar que se muestra el fragmento y se reciben los datos
        Log.d("Carrito", "Fragmento del carrito mostrado. Productos en el carrito: " + listaProductosCarrito.size());

        // Regresar al fragment anterior
         ImageView ivAtras = view.findViewById(R.id.ivAtras);
         ivAtras.setOnClickListener(v -> onBackButtonClick());

        // Actualizar los valores del subtotal, descuento y total
        actualizarValores();

        btnRealizarPedido.setOnClickListener(v -> {
            String totalText = tvTotal.getText().toString();
            if (totalText.equals(getString(R.string.total_default))) {
                // El carrito está vacío
                Toast.makeText(getContext(), "Carrito vacío", Toast.LENGTH_SHORT).show();
                onBackButtonClick();
            } else {
                // Realizar el pedido
                realizarPedido();
            }
        });

        return view;
    }

    private void onBackButtonClick() {

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }

        // Mostrar elementos del activity
        Activity activity = getActivity();
        if (activity instanceof MainClienteActivity) {
            ((MainClienteActivity) activity).showOptionsBottomBar();
            ((MainClienteActivity) activity).showHeader();
        }
    }

    public void actualizarValores() {
        if (listaProductosCarrito.isEmpty()) {
            // El carrito está vacío
            tvSubTotal.setText(getString(R.string.subtotal_default));
            tvDescuento.setText(getString(R.string.descuento_default));
            tvTotal.setText(getString(R.string.total_default));
        } else {
            // Calcular el subtotal
            double subTotal = carritoAdapter.calcularSubTotal();
            tvSubTotal.setText(String.format(getString(R.string.subtotal_label), subTotal));

            // Asignar el valor del descuento
            double descuento = 2.0; // Aquí puedes establecer el valor del descuento deseado
            carritoAdapter.setDescuento(descuento);
            // Obtener el descuento del adaptador
            tvDescuento.setText(String.format(getString(R.string.descuento_label), descuento));

            // Calcular el total
            double total = carritoAdapter.calcularTotal();
            tvTotal.setText(String.format(getString(R.string.total_label), total));
        }
    }



    private void realizarPedido() {

        // Recuperar el ID del usuario desde SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        total = carritoAdapter.calcularTotal();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insertar la compra en la tabla "compra" y obtener su ID generado automáticamente
        String sqlInsercionCompra = "INSERT INTO compra (idUsuario, fechaCom, totalCom) VALUES (" +
                usuarioId + ", '" + obtenerFechaActual() + "', " + total + ")";
        db.execSQL(sqlInsercionCompra);

        // Obtener el ID de la compra generada automáticamente
        String sqlObtenerIdCompra = "SELECT last_insert_rowid()";
        Cursor cursor = db.rawQuery(sqlObtenerIdCompra, null);
        int idCompra = 0;
        if (cursor != null && cursor.moveToFirst()) {
            idCompra = cursor.getInt(0);
            cursor.close();
        }

        // Insertar los detalles de compra en la tabla "detallecompra" para cada producto en el carrito
        for (Producto producto : listaProductosCarrito) {
            String sqlInsercionDetalle = "INSERT INTO detallecompra (idProducto, idCompra, cantidad) VALUES (" +
                    producto.getId() + ", " + idCompra + ", " + producto.getCantidad() + ")";
            db.execSQL(sqlInsercionDetalle);
        }

        // Vaciar el carrito
        vaciarCarrito();

        Toast.makeText(getContext(), "Pedido realizado", Toast.LENGTH_SHORT).show();

        // Regresar al fragment anterior
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }

        // Mostrar elementos del activity
        Activity activity = getActivity();
        if (activity instanceof MainClienteActivity) {
            ((MainClienteActivity) activity).showOptionsBottomBar();
            ((MainClienteActivity) activity).showHeader();
        }


    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void vaciarCarrito() {
        listaProductosCarrito.clear();
        carritoAdapter.notifyDataSetChanged();
    }

}

