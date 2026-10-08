package com.aperezm.spotify2.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.activities.ReproductorActivity;
import com.aperezm.spotify2.databinding.ItemCancionBibliotecaBinding;
import com.aperezm.spotify2.models.api.Cancion;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Adaptador para mostrar una lista de canciones en un RecyclerView
 *
 * @author Adrián Pérez Morales
 */
public class CancionBibliotecaAdapter extends RecyclerView.Adapter<CancionBibliotecaAdapter.CancionBibliotecaViewHolder> {
    private final List<Cancion> canciones;

    public CancionBibliotecaAdapter(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    /**
     * Crea un nuevo ViewHolder para una canción.
     */
    @NonNull
    @Override
    public CancionBibliotecaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCancionBibliotecaBinding binding = ItemCancionBibliotecaBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CancionBibliotecaViewHolder(binding);
    }

    /**
     * Vincula los datos de una canción al ViewHolder.
     *
     * @param holder ViewHolder de la canción
     * @param position Índice de la canción en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull CancionBibliotecaViewHolder holder, int position) {
        Cancion cancion = canciones.get(position);

        // Se establecen los textos a mostrar y la imagen
        SpotifyUtils.cargarImagenGlide(holder.itemView.getContext(), cancion.getAlbum().getUrlFoto(), holder.binding.cover, R.drawable.ic_audio);
        holder.binding.title.setText(cancion.getNombre());
        holder.binding.artist.setText(cancion.getArtista().getNombre());

        // Se establece el listener de la canción
        holder.binding.song.setOnClickListener(v -> abrirReproductor(position, holder));
    }

    /**
     * Devuelve el número de canciones en la lista.
     *
     * @return Número de canciones
     */
    @Override
    public int getItemCount() {
        return canciones.size();
    }

    /**
     * Abre la actividad del reproductor de música con la lista de canciones y la canción seleccionada.
     *
     * @param posicionCancion Índice de la canción seleccionada
     * @param holder ViewHolder de la canción
     */
    private void abrirReproductor(int posicionCancion, CancionBibliotecaViewHolder holder) {
        Intent intent = new Intent(holder.binding.getRoot().getContext(), ReproductorActivity.class);
        intent.putExtra("listaCanciones", (Serializable) canciones);
        intent.putExtra("posicionCancion", posicionCancion);
        holder.binding.getRoot().getContext().startActivity(intent);
    }

    /**
     * ViewHolder para una canción en el RecyclerView de Biblioteca.
     */
    public static class CancionBibliotecaViewHolder extends RecyclerView.ViewHolder {
        ItemCancionBibliotecaBinding binding;

        public CancionBibliotecaViewHolder(ItemCancionBibliotecaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}