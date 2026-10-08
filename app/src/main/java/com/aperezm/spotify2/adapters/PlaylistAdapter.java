package com.aperezm.spotify2.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.databinding.ItemTusPlaylistsBinding;
import com.aperezm.spotify2.models.ui.Playlist;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.util.List;

/**
 * Adaptador para mostrar una lista de playlists en un RecyclerView.
 *
 * @author Adrián Pérez Morales
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private final List<Playlist> playlists;

    public PlaylistAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * Crea un nuevo ViewHolder para una playlist.
     */
    @NonNull
    @Override
    public PlaylistAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTusPlaylistsBinding binding = ItemTusPlaylistsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PlaylistAdapter.PlaylistViewHolder(binding);
    }

    /**
     * Vincula los datos de una playlist al ViewHolder.
     *
     * @param holder ViewHolder de la playlist
     * @param position Índice de la playlist en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);

        // Se establece el texto a mostrar y la imagen
        SpotifyUtils.cargarImagenGlide(holder.itemView.getContext(), playlist.getUrlImagen(), holder.binding.imagenPlaylist, R.drawable.ic_audio);
        holder.binding.nombrePlaylist.setText(playlist.getNombre());
    }

    /**
     * Devuelve el número de playlists en la lista.
     *
     * @return Número de playlists
     */
    @Override
    public int getItemCount() {
        return playlists.size();
    }

    /**
     * ViewHolder para una playlist en el RecyclerView.
     */
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ItemTusPlaylistsBinding binding;

        public PlaylistViewHolder(ItemTusPlaylistsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}