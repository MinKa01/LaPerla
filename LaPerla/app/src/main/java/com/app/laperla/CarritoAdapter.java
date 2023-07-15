package com.app.laperla;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.laperla.cliente.CarritoClienteFragment;

import java.util.List;
import java.util.Locale;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Producto> listaProductosCarrito;
    private CarritoClienteFragment fragment;

    private double subTotalCarrito;
    private double descuento;

    public CarritoAdapter(List<Producto> listaProductosCarrito, CarritoClienteFragment fragment) {
        this.listaProductosCarrito = listaProductosCarrito;
        this.fragment = fragment;
        this.subTotalCarrito = 0.0;
        this.descuento = 0.0;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de los items del carrito
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        // Obtener el producto del carrito en la posición actual
        Producto producto = listaProductosCarrito.get(position);

        // Asignar los valores del producto a los elementos de la vista del item_carrito
        String imagenName = producto.getImagen();
        int resourceId = holder.itemView.getContext().getResources()
                .getIdentifier(imagenName, "drawable", holder.itemView.getContext().getPackageName());

        // Verificar si se encontró el recurso de imagen
        if (resourceId != 0) {
            holder.ivProducto.setImageResource(resourceId);
        } else {
            // Si no se encuentra el recurso de imagen,muestra el icono
            holder.ivProducto.setImageResource(R.drawable.ic_food_menu_selected);
        }

        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "S/ %.2f", producto.getPrecio()));
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));

        // Implementar la lógica para gestionar la cantidad del producto en el carrito
        holder.ivAdd.setOnClickListener(v -> {
            producto.incrementarCantidad();
            // Actualizar la cantidad
            holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));
            // Actualizar el total del carrito
            fragment.actualizarValores();
        });

        holder.ivRemove.setOnClickListener(v -> {
            producto.decrementarCantidad();
            // Actualizar la cantidad
            holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));
            // Actualizar el total del carrito
            fragment.actualizarValores();
        });

        // Mostrar u ocultar el fondo en función de si el elemento está siendo deslizado o no
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float translationX = event.getX() - startX;
                        Log.d("Carrito", "translationX: " + translationX);
                        if (translationX < 0) {
                            holder.background.setVisibility(View.VISIBLE);
                        } else {
                            holder.background.setVisibility(View.GONE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.background.setVisibility(View.GONE);
                        // Llamar al método performClick cuando se detecta un clic
                        v.performClick();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.background.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaProductosCarrito.size();
    }

    public double calcularSubTotal() {
        double subTotal = 0;
        for (Producto producto : listaProductosCarrito) {
            subTotal += producto.getPrecio() * producto.getCantidad();
        }
        return subTotal;
    }

    public double calcularTotal() {
        double subTotal = calcularSubTotal();
        double total = subTotal - descuento;
        return total;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }


    public class CarritoViewHolder extends RecyclerView.ViewHolder {
        // Declarar los elementos de la vista del item_carrito
        ImageView ivProducto, ivRemove, ivAdd;
        TextView tvNombre, tvPrecio, tvCantidad;

        LinearLayout background;
        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar los elementos de la vista del item_carrito
            ivProducto = itemView.findViewById(R.id.ivProducto);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            ivAdd = itemView.findViewById(R.id.ivAdd);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            background = itemView.findViewById(R.id.background);
        }
    }
}
