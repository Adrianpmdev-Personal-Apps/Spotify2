package com.aperezm.spotify2.models.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Clase que representa una canción de Deezer.
 * Utilizada para mapear la respuesta de la API de Deezer y obtener el nombre de la canción, el artista y el álbum.
 *
 * @author Adrián Pérez Morales
 */
public class Cancion implements Serializable {
    @SerializedName("title")
    public String nombre;

    @SerializedName("artist")
    public Artista artista;

    @SerializedName("album")
    public Album album;

    // GETTERS
    public String getNombre() { return nombre; }
    public Artista getArtista() { return artista; }
    public Album getAlbum() { return album; }
}