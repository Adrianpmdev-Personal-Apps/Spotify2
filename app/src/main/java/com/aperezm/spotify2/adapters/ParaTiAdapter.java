package com.aperezm.spotify2.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.databinding.ItemParaTiBinding;
import com.aperezm.spotify2.models.ui.ParaTi;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.util.List;

/**
 * Adaptador para mostrar una lista de elementos "Para Ti" en un RecyclerView
 *
 * @author Adrián Pérez Morales
 */
public class ParaTiAdapter extends RecyclerView.Adapter<ParaTiAdapter.ParaTiViewHolder> {
    private final List<ParaTi> paraTiLista;

    public ParaTiAdapter(List<ParaTi> paraTiLista) {
        this.paraTiLista = paraTiLista;
    }

    /**
     * Crea un nuevo ViewHolder para un elemento "Para Ti".
     */
    @NonNull
    @Override
    public ParaTiAdapter.ParaTiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemParaTiBinding binding = ItemParaTiBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ParaTiAdapter.ParaTiViewHolder(binding);
    }

    /**
     * Vincula los datos de un elemento "Para Ti" al ViewHolder.
     *
     * @param holder ViewHolder del elemento
     * @param position Índice del elemento en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull ParaTiAdapter.ParaTiViewHolder holder, int position) {
        ParaTi paraTi = paraTiLista.get(position);

        // Se establecen los textos a mostrar y la imagen
        SpotifyUtils.cargarImagenGlide(holder.itemView.getContext(), paraTi.getUrlImagen(), holder.binding.imageCard, R.drawable.ic_audio);
        holder.binding.titleCard.setText(paraTi.getTitulo());
        holder.binding.subtitleCard.setText(paraTi.getSubtitulo());
    }

    /**
     * Devuelve el número de elementos en la lista "Para Ti".
     *
     * @return Número de elementos
     */
    @Override
    public int getItemCount() {
        return paraTiLista.size();
    }

    /**
     * ViewHolder para los elementos de la lista "Para Ti".
     */
    public static class ParaTiViewHolder extends RecyclerView.ViewHolder {
        ItemParaTiBinding binding;

        public ParaTiViewHolder(ItemParaTiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}