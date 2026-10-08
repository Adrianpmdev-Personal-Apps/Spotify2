package com.aperezm.spotify2.callbacks;

/**
 * Callback que se utiliza para recibir el resultado de una consulta a la base de datos que verifica
 * si un usuario existe.
 *
 * @author Adrián Pérez Morales
 */
public interface UsuarioExistenteCallback {
    void onUsuarioExistenteResultado(boolean existe);
}