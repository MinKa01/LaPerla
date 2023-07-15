package com.app.laperla;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<Pedido> listaPedidos;

    public PedidosAdapter(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de los items del pedido
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        // Obtener el pedido en la posición actual
        Pedido pedido = listaPedidos.get(position);

        // Asignar los valores del pedido a los elementos de la vista del item_pedido
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = inputFormat.parse(pedido.getFecha());
            String fechaFormateada = outputFormat.format(date);
            holder.tvFecha.setText("Fecha: " + fechaFormateada);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //holder.tvFecha.setText("Fecha: " + pedido.getFecha());
        holder.tvCantidadProductos.setText("Cantidad de platos: " + String.valueOf(pedido.getCantidadProductos()));
        holder.tvTotal.setText("Costo: S/ " + String.format(Locale.getDefault(), "%.2f", pedido.getTotal()));

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {
        // Declarar los elementos de la vista del item_pedido
        TextView tvFecha, tvCantidadProductos, tvTotal;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar los elementos de la vista del item_pedido
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCantidadProductos = itemView.findViewById(R.id.tvCantidadProductos);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.listaPedidos = pedidos;
        notifyDataSetChanged();
    }

}
