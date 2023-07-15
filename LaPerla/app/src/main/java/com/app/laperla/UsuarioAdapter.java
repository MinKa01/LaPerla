package com.app.laperla;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> listaUsuarios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Usuario usuario);
        void onEliminarClick(Usuario usuario);
        void onEditarClick(Usuario usuario);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.bind(usuario);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombreApellido;
        private TextView tvCorreo;
        private TextView btnEliminar;
        private TextView btnEditar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreApellido = itemView.findViewById(R.id.tvNombreApellido);
            tvCorreo = itemView.findViewById(R.id.tvCorreo);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnEditar = itemView.findViewById(R.id.btnEditar);

            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Usuario usuario = listaUsuarios.get(position);
                        listener.onEliminarClick(usuario);
                    }
                }
            });

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Usuario usuario = listaUsuarios.get(position);
                        listener.onEditarClick(usuario);
                    }
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Usuario usuario = listaUsuarios.get(position);
                        listener.onItemClick(usuario);
                    }
                }
            });
        }
        public void bind(Usuario usuario) {
            String nombreApellido = usuario.getNombre() + " " + usuario.getApellido();
            tvNombreApellido.setText(nombreApellido);
            tvCorreo.setText(usuario.getCorreo());
        }
    }
}
