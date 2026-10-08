package com.aperezm.spotify2.callbacks;

import com.aperezm.spotify2.models.ui.ParaTi;

/**
 * Callback que se utiliza para recibir el resultado de una consulta a la base de datos que devuelve
 * un objeto ParaTi.
 *
 * @author Adrián Pérez Morales
 */
public interface ParaTiCallback {
    void onParaTiResultado(ParaTi paraTi);
}