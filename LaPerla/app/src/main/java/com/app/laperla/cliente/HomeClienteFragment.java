package com.app.laperla.cliente;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.laperla.DatabaseHelper;
import com.app.laperla.Producto;
import com.app.laperla.ProductosAdapter;
import com.app.laperla.R;
import com.app.laperla.admin.MainAdminActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeClienteFragment extends Fragment {

    private List<Producto> listaProductos;
    private List<Producto> listaProductosCarrito;
    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private DatabaseHelper dbHelper;

    Button btnMiCarrito;

    public HomeClienteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        // Inicializar la lista de productos
        listaProductos = obtenerListaProductos();

        // Inicializar la lista de productos del carrito
        listaProductosCarrito = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_cliente, container, false);

        // Configurar el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductosAdapter(listaProductos, listaProductosCarrito);
        recyclerView.setAdapter(adapter);

        btnMiCarrito = view.findViewById(R.id.btnMiCarrito);

        btnMiCarrito.setOnClickListener(v -> {
            if (listaProductosCarrito.isEmpty()) {
                // El carrito está vacío
                Toast.makeText(getContext(), "Primero debes seleccionar un producto", Toast.LENGTH_SHORT).show();
            } else {
                // Crear una instancia del fragment CarritoClienteFragment
                CarritoClienteFragment carritoFragment = new CarritoClienteFragment();

                // Crear un Bundle para pasar la lista de productos del carrito como argumento
                Bundle args = new Bundle();
                args.putParcelableArrayList("listaProductosCarrito", (ArrayList<Producto>) listaProductosCarrito);
                carritoFragment.setArguments(args);

                // Reemplazar el fragment actual con el fragment CarritoClienteFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerCliente, carritoFragment)
                        .addToBackStack(null)
                        .commit();

                // Ocultar las elementos del activity
                Activity activity = getActivity();
                if (activity instanceof MainClienteActivity) {
                    ((MainClienteActivity) activity).hideOptionsBottomBar();
                    ((MainClienteActivity) activity).hideHeader();
                }
            }
        });

        return view;
    }

    private List<Producto> obtenerListaProductos() {

        List<Producto> listaProductos = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM producto", null);

        // Obtener los índices de las columnas
        int idColumnIndex = cursor.getColumnIndex("idProducto");
        int nombreColumnIndex = cursor.getColumnIndex("nombreProd");
        int imagenColumnIndex = cursor.getColumnIndex("imagenProd");
        int detalleColumnIndex = cursor.getColumnIndex("detalleProd");
        int precioColumnIndex = cursor.getColumnIndex("precioProd");

        // Recorrer el cursor y construir los objetos Producto
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idColumnIndex);
                String nombre = cursor.getString(nombreColumnIndex);
                String imagen = cursor.getString(imagenColumnIndex);
                String detalle = cursor.getString(detalleColumnIndex);
                double precio = cursor.getDouble(precioColumnIndex);

                // Crear el objeto Producto y agregarlo a la lista
                Producto producto = new Producto(id, nombre, imagen, detalle, precio);
                listaProductos.add(producto);

                // Agregar logs para verificar los datos de cada producto
                Log.d("Producto", "ID: " + id);
                Log.d("Producto", "Nombre: " + nombre);
                Log.d("Producto", "Imagen: " + imagen);
                Log.d("Producto", "Detalle: " + detalle);
                Log.d("Producto", "Precio: " + precio);

            } while (cursor.moveToNext());

            // Cerrar el cursor
            cursor.close();
        }

        // Cerrar la conexión a la base de datos
        db.close();

        return listaProductos;
    }
}
