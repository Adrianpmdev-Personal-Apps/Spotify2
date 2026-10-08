package com.aperezm.spotify2.models.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase que representa un artista de Deezer.
 * Utilizada para mapear la respuesta de la API de Deezer y obtener el nombre y la URL de la foto del artista.
 *
 * @author Adrián Pérez Morales
 */
public class Artista implements Serializable {
    @SerializedName("name")
    public String nombre;

    @SerializedName("picture")
    public String urlFoto;

    // GETTERS
    public String getNombre() { return nombre; }
    public String getUrlFoto() { return urlFoto; }
}