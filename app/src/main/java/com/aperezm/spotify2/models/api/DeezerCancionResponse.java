package com.aperezm.spotify2.models.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Clase que representa la respuesta de la API de Deezer para una búsqueda de canciones.
 * Utilizada para mapear la respuesta de la API de Deezer y obtener una lista de canciones.
 *
 * @author Adrián Pérez Morales
 */
public class DeezerCancionResponse {
    @SerializedName("data")
    public List<Cancion> data;

    // GETTERS
    public List<Cancion> getData() { return data; }
}