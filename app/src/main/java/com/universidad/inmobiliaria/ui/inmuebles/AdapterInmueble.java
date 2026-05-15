package com.universidad.inmobiliaria.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.universidad.inmobiliaria.databinding.ItemInmuebleBinding;
import com.universidad.inmobiliaria.modelo.Inmueble;

import java.util.List;

public class AdapterInmueble extends RecyclerView.Adapter<AdapterInmueble.ViewHolder> {

    private List<Inmueble> lista;

    public AdapterInmueble(List<Inmueble> lista) {
        this.lista = lista;
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

        Inmueble inmueble = lista.get(position);

        holder.b.tvDireccion.setText(inmueble.getDireccion());
        holder.b.tvPrecio.setText(String.format("$ %.2f", inmueble.getValor()));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }


    public void setLista(List<Inmueble> lista) {
        this.lista = lista;
        // Le avisa al recycler que los datos cambiaron de nuevo dibuje la panalla"
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemInmuebleBinding b;

        public ViewHolder(ItemInmuebleBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}