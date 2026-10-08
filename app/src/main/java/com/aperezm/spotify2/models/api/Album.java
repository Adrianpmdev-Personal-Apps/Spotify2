package com.aperezm.spotify2.models.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase que representa un álbum de Deezer.
 * Utilizada para mapear la respuesta de la API de Deezer y obtener la URL de la foto del álbum.
 *
 * @author Adrián Pérez Morales
 */
public class Album implements Serializable {
    @SerializedName("cover")
    public String urlFoto;

    // GETTERS
    public String getUrlFoto() { return urlFoto; }
}