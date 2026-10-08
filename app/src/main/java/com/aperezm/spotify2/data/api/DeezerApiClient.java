package com.aperezm.spotify2.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aperezm.spotify2.callbacks.ArtistaCallback;
import com.aperezm.spotify2.callbacks.CancionCallback;
import com.aperezm.spotify2.models.api.Artista;
import com.aperezm.spotify2.models.api.Cancion;
import com.aperezm.spotify2.models.api.DeezerArtistaResponse;
import com.aperezm.spotify2.models.api.DeezerCancionResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para configurar Retrofit y crear una instancia del servicio de la API de Deezer.
 *
 * @author Adrián Pérez Morales
 */
public class DeezerApiClient {
    private static final String BASE_URL = "https://api.deezer.com/";

    /**
     * Crea y devuelve una instancia del servicio de la API de Deezer utilizando Retrofit.
     *
     * @return Una instancia de DeezerApiService para realizar llamadas a la API de Deezer.
     */
    public static DeezerApiService getService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DeezerApiService.class);
    }

    /**
     * Busca una canción por su título utilizando la API de Deezer y devuelve el resultado a través de un callback.
     *
     * @param tituloCancion El título de la canción a buscar.
     * @param callback Un callback que se llamará con la canción obtenida o null si no se encuentra o ocurre un error.
     */
    public static void buscarCancion(String tituloCancion, CancionCallback callback) {
        DeezerApiService api = DeezerApiClient.getService();

        api.buscarCancion(tituloCancion).enqueue(new Callback<DeezerCancionResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeezerCancionResponse> call, @NonNull Response<DeezerCancionResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    Cancion cancion = response.body().getData().get(0);
                    callback.onCancionObtenida(cancion);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "sin cuerpo";
                        Log.e("Deezer", "Código: " + response.code() + ", error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    callback.onCancionObtenida(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeezerCancionResponse> call, @NonNull Throwable t) {
                Log.e("Deezer", "Fallo al obtener cancion: " + t.getMessage(), t);
                callback.onCancionObtenida(null);
            }
        });
    }

    /**
     * Busca un artista por su nombre utilizando la API de Deezer y devuelve el resultado a través de un callback.
     *
     * @param nombreArtista El nombre del artista a buscar.
     * @param callback Un callback que se llamará con el artista obtenido o null si no se encuentra o ocurre un error.
     */
    public static void buscarArtista(String nombreArtista, ArtistaCallback callback) {
        DeezerApiService api = DeezerApiClient.getService();

        api.buscarArtista(nombreArtista).enqueue(new Callback<DeezerArtistaResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeezerArtistaResponse> call, @NonNull Response<DeezerArtistaResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    Artista artista = response.body().getData().get(0);
                    callback.onArtistaObtenido(artista);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "sin cuerpo";
                        Log.e("Deezer", "Código: " + response.code() + ", error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    callback.onArtistaObtenido(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeezerArtistaResponse> call, @NonNull Throwable t) {
                Log.e("Deezer", "Fallo al obtener artista: " + t.getMessage(), t);
                callback.onArtistaObtenido(null);
            }
        });
    }
}