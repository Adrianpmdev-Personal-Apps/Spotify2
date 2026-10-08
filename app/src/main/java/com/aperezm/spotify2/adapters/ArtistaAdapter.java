package com.aperezm.spotify2.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.databinding.ItemArtistaBinding;
import com.aperezm.spotify2.models.api.Artista;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.util.List;

/**
 * Adaptador para mostrar una lista de artistas en un RecyclerView.
 *
 * @author Adrián Pérez Morales
 */
public class ArtistaAdapter extends RecyclerView.Adapter<ArtistaAdapter.ArtistaViewHolder> {
    private final List<Artista> artistas;

    public ArtistaAdapter(List<Artista> artistas) {
        this.artistas = artistas;
    }

    /**
     * Crea un nuevo ViewHolder para un artista.
     */
    @NonNull
    @Override
    public ArtistaAdapter.ArtistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArtistaBinding binding = ItemArtistaBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ArtistaAdapter.ArtistaViewHolder(binding);
    }

    /**
     * Vincula los datos de un artista al ViewHolder.
     *
     * @param holder ViewHolder del artista
     * @param position Índice del artista en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull ArtistaAdapter.ArtistaViewHolder holder, int position) {
        Artista artista = artistas.get(position);

        // Se establece el texto a mostrar y la imagen
        SpotifyUtils.cargarImagenGlide(holder.itemView.getContext(), artista.getUrlFoto(), holder.binding.artistImage, R.drawable.ic_user);
        holder.binding.artistName.setText(artista.getNombre());
    }

    /**
     * Devuelve el número de elementos en la lista de artistas.
     */
    @Override
    public int getItemCount() {
        return artistas.size();
    }

    /**
     * ViewHolder para un artista.
     */
    public static class ArtistaViewHolder extends RecyclerView.ViewHolder {
        ItemArtistaBinding binding;

        public ArtistaViewHolder(ItemArtistaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}