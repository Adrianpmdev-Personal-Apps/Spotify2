package com.aperezm.spotify2.models.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Clase que representa la respuesta de la API de Deezer para una búsqueda de artistas.
 * Utilizada para mapear la respuesta de la API de Deezer y obtener una lista de artistas.
 *
 * @author Adrián Pérez Morales
 */
public class DeezerArtistaResponse {
    @SerializedName("data")
    public List<Artista> data;

    // GETTERS
    public List<Artista> getData() { return data; }
}