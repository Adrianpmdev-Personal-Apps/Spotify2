package com.aperezm.spotify2.data.api;

import com.aperezm.spotify2.models.api.DeezerArtistaResponse;
import com.aperezm.spotify2.models.api.DeezerCancionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz para definir los endpoints de la API de Deezer.
 *
 * @author Adrián Pérez Morales
 */
public interface DeezerApiService {
    @GET("search")
    Call<DeezerCancionResponse> buscarCancion(@Query("q") String query);

    @GET("search/artist")
    Call<DeezerArtistaResponse> buscarArtista(@Query("q") String query);
}