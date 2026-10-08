package com.aperezm.spotify2.callbacks;

import com.aperezm.spotify2.models.ui.Playlist;

/**
 * Callback que se utiliza para recibir el resultado de una consulta a la base de datos que devuelve
 * un objeto Playlist.
 *
 * @author Adrián Pérez Morales
 */
public interface PlaylistCallback {
    void onPlaylistResultado(Playlist playlist);
}