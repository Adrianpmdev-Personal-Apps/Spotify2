package com.aperezm.spotify2.callbacks;

import com.aperezm.spotify2.models.ui.Usuario;

/**
 * Callback que se utiliza para recibir el resultado de una consulta a la base de datos que devuelve
 * un objeto Usuario.
 *
 * @author Adrián Pérez Morales
 */
public interface UsuarioCallback {
    void onUsuarioResultado(Usuario usuario);
}