package com.aperezm.spotify2.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.databinding.ItemCancionReproductorBinding;
import com.aperezm.spotify2.models.api.Cancion;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.util.List;

/**
 * Adaptador para ViewPager2 que muestra las canciones como páginas deslizable.
 * Cada página muestra la portada, título y artista de la canción.
 *
 * @author Adrián Pérez Morales
 */
public class CancionReproductorAdapter extends RecyclerView.Adapter<CancionReproductorAdapter.CancionReproductorViewHolder> {
    private final List<Cancion> canciones;

    public CancionReproductorAdapter(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    /**
     * Crea un nuevo ViewHolder para cada página/canción.
     */
    @NonNull
    @Override
    public CancionReproductorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCancionReproductorBinding binding = ItemCancionReproductorBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CancionReproductorViewHolder(binding);
    }

    /**
     * Vincula los datos de la canción a la página correspondiente.
     *
     * @param holder ViewHolder de la página
     * @param position Índice de la canción en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull CancionReproductorViewHolder holder, int position) {
        Cancion cancion = canciones.get(position);
        holder.binding.pagerTitle.setText(cancion.getNombre());
        holder.binding.pagerArtist.setText(cancion.getArtista().getNombre());
        SpotifyUtils.cargarImagenGlide(holder.itemView.getContext(), cancion.getAlbum().getUrlFoto(), holder.binding.pagerCover, R.drawable.ic_audio);
    }

    /**
     * Devuelve el número total de canciones/páginas.
     *
     * @return Número de canciones en la lista.
     */
    @Override
    public int getItemCount() {
        return canciones.size();
    }

    /**
     * ViewHolder para cada página de canción en el ViewPager2 en Reproductor.
     */
    public static class CancionReproductorViewHolder extends RecyclerView.ViewHolder {
        ItemCancionReproductorBinding binding;

        public CancionReproductorViewHolder(ItemCancionReproductorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}