package com.aperezm.spotify2.callbacks;

import com.aperezm.spotify2.models.api.Artista;

/**
 * Callback que se utiliza para recibir el resultado de una llamada a la API de Deezer que obtiene
 * el artista solicitado.
 *
 * @author Adrián Pérez Morales
 */
public interface ArtistaCallback {
    void onArtistaObtenido(Artista artista);
}