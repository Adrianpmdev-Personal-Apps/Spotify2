package com.aperezm.spotify2.callbacks;

/**
 * Callback que se utiliza para recibir el resultado de una consulta a la base de datos que verifica
 * las credenciales de inicio de sesión.
 *
 * @author Adrián Pérez Morales
 */
public interface LoginCallback {
    void onLoginResultado(boolean esValido);
}