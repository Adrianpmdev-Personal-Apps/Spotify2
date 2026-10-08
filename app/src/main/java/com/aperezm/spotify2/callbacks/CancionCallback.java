package com.aperezm.spotify2.callbacks;

import com.aperezm.spotify2.models.api.Cancion;

/**
 * Callback que se utiliza para recibir el resultado de una llamada a la API de Deezer que obtiene
 * la canción solicitada.
 *
 * @author Adrián Pérez Morales
 */
public interface CancionCallback {
    void onCancionObtenida(Cancion cancion);
}