package com.aperezm.spotify2.callbacks;

/**
 * Callback que se utiliza para recibir el resultado de una consulta a la base de datos que devuelve
 * una URL en formato String.
 *
 * @author Adrián Pérez Morales
 */
public interface UrlCallback {
    void onUrlObtenida(String url);
}