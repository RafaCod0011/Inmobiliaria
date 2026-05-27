package com.universidad.inmobiliaria.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.universidad.inmobiliaria.modelo.Pago;
import com.universidad.inmobiliaria.databinding.ItemPagosBinding;


import java.util.List;

public class AdapterPagos extends RecyclerView.Adapter<AdapterPagos.ViewHolder> {


    private List<Pago> listaPagos;
    public AdapterPagos(List<Pago> listaPagos) {
        this.listaPagos = listaPagos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPagosBinding binding = ItemPagosBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = listaPagos.get(position);

        holder.binding.tvFechaPago.setText(pago.getFechaPago());
        holder.binding.tvCodigoPago.setText(String.valueOf(pago.getIdPago()));
        holder.binding.tvCodigoContrato.setText(String.valueOf(pago.getContrato().getIdContrato()));
        holder.binding.tvMonto.setText(String.format("$ %, .0f", pago.getMonto()));
        holder.binding.tvDetalle.setText(pago.getDetalle());
    }

    @Override
    public int getItemCount() {
        return listaPagos != null ? listaPagos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPagosBinding binding;

        public ViewHolder(ItemPagosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void setLista(List<Pago> nuevaLista) {
        this.listaPagos = nuevaLista;
        notifyDataSetChanged();
    }

}
