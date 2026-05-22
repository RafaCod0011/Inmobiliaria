package com.universidad.inmobiliaria.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.universidad.inmobiliaria.R;
import com.universidad.inmobiliaria.databinding.ItemInmuebleBinding;
import com.universidad.inmobiliaria.modelo.Inmueble;
import com.universidad.inmobiliaria.request.ApiClient;

import java.util.List;

public class AdapterInmueble extends RecyclerView.Adapter<AdapterInmueble.ViewHolder> {

    private List<Inmueble> listaInmuebles;
    private OnItemClickListener listener;   // ← Para navegar al detalle

    public AdapterInmueble(List<Inmueble> listaInmuebles) {
        this.listaInmuebles = listaInmuebles;
    }

    // Interface para detectar clicks
    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleBinding binding = ItemInmuebleBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = listaInmuebles.get(position);


        // Imagen
        Glide.with(holder.itemView.getContext())
                .load(ApiClient.BASE_URL + inmueble.getImagen())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.binding.imgInmueble);

        // Dirección
        holder.binding.tvDireccion.setText(inmueble.getDireccion() != null ?
                inmueble.getDireccion() : "Sin dirección");

        // Precio formateado (mejor formato)
        holder.binding.tvPrecio.setText(String.format("$ %, .0f", inmueble.getValor()));

        // Tipo + Ambientes
        String tipoAmbientes = (inmueble.getTipo() != null ? inmueble.getTipo() : "") +
                " • " + inmueble.getAmbientes() + " ambientes";
        holder.binding.tvTipoAmbientes.setText(tipoAmbientes.trim());

        // Estado
        if (inmueble.isDisponible()) {
            holder.binding.tvEstado.setText("Disponible");
            holder.binding.tvEstado.setBackgroundResource(R.drawable.bg_estado_disponible);
        } else {
            holder.binding.tvEstado.setText("No disponible");
            holder.binding.tvEstado.setBackgroundResource(R.drawable.bg_estado_no_disponible);
        }

        // Click para ver detalle
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(inmueble);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaInmuebles != null ? listaInmuebles.size() : 0;
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemInmuebleBinding binding;

        public ViewHolder(ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    // Actualizar lista completa
    public void setLista(List<Inmueble> nuevaLista) {
        this.listaInmuebles = nuevaLista;
        notifyDataSetChanged();
    }
}