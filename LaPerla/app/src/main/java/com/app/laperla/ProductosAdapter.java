package com.app.laperla;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private List<Producto> listaProductosCarrito;

    public ProductosAdapter(List<Producto> listaProductos, List<Producto> listaProductosCarrito) {
        this.listaProductos = listaProductos;
        this.listaProductosCarrito = listaProductosCarrito;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        // Cargar la imagen desde el drawable utilizando su nombre
        int resourceId = holder.itemView.getContext().getResources()
                .getIdentifier(producto.getImagen(), "drawable", holder.itemView.getContext().getPackageName());

        // Verificar si se encontró el recurso de imagen
        if (resourceId != 0) {
            holder.ivProducto.setImageResource(resourceId);
        }
        holder.tvNombre.setText(producto.getNombre());
        //holder.tvPrecio.setText(String.valueOf(producto.getPrecio()));
        String precioString = String.format(Locale.getDefault(), "S/%.2f", producto.getPrecio());
        holder.tvPrecio.setText(precioString);
        // Guardar el ID del producto como una etiqueta en el botón "Agregar"
        holder.btnAgregar.setTag(producto.getId());
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProducto;
        TextView tvNombre, tvPrecio;
        TextView btnAgregar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);

            btnAgregar.setOnClickListener(view -> {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Producto producto = listaProductos.get(position);

                    // Verificar si el producto ya está en la lista de productos del carrito
                    if (listaProductosCarrito.contains(producto)) {
                        // Si el producto ya está en la lista, mostrar un mensaje de Toast
                        Toast.makeText(view.getContext(), "Producto ya agregado al carrito", Toast.LENGTH_SHORT).show();
                    } else {
                        // Si el producto no está en la lista, agregarlo y mostrar un mensaje de Toast
                        listaProductosCarrito.add(producto);
                        Toast.makeText(view.getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}



