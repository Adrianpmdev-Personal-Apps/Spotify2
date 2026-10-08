package com.aperezm.spotify2.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aperezm.spotify2.BuildConfig;
import com.aperezm.spotify2.callbacks.UrlCallback;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para configurar Retrofit y crear una instancia del servicio de la API de Supabase.
 *
 * @author Adrián Pérez Morales
 */
public class SupabaseApiClient {
    private static final String BASE_URL = BuildConfig.SUPABASE_URL + "/";

    /**
     * Crea y devuelve una instancia del servicio de la API de Supabase utilizando Retrofit.
     *
     * @return Una instancia de SupabaseApiService para realizar llamadas a la API de Supabase.
     */
    public static SupabaseApiService getService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SupabaseApiService.class);
    }

    /**
     * Obtiene la URL firmada de un archivo almacenado en Supabase Storage para su reproducción o descarga.
     *
     * @param nombreArchivo Nombre del archivo de la canción en Supabase Storage.
     * @param callback Callback para recibir la URL obtenida o manejar el error en caso de fallo.
     */
    public static void obtenerUrlSupabaseStorage(String nombreBucket, String nombreArchivo, UrlCallback callback) {
        SupabaseApiService api = SupabaseApiClient.getService();
        RequestBody body = RequestBody.create("{\"expiresIn\":600}", MediaType.parse("application/json"));

        api.obtenerUrlFirmadaStorage(nombreBucket, nombreArchivo, BuildConfig.SUPABASE_API_KEY, "Bearer " + BuildConfig.SUPABASE_API_KEY, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        String urlFirmada = BuildConfig.SUPABASE_URL + "/storage/v1" + json.getString("signedURL");
                        callback.onUrlObtenida(urlFirmada);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onUrlObtenida("");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "sin cuerpo";
                        Log.e("Supabase", "Código: " + response.code() + ", error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    callback.onUrlObtenida("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<okhttp3.ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                callback.onUrlObtenida("");
            }
        });
    }

    /**
     * Sube una imagen de perfil a Supabase Storage.
     *
     * @param nombreArchivo Nombre del archivo de la imagen a subir en Supabase Storage.
     * @param imagenBytes Bytes de la imagen a subir.
     * @param callback Callback para recibir la URL obtenida o manejar el error en caso de fallo. En este caso, se devuelve "OK" si la subida fue exitosa, o una cadena vacía si hubo un error.
     */
    public static void subirImagenPerfil(String nombreArchivo, byte[] imagenBytes, UrlCallback callback) {
        SupabaseApiService api = SupabaseApiClient.getService();
        RequestBody body = RequestBody.create(imagenBytes, MediaType.parse("image/png"));

        api.subirImagenPerfil("ImagenesPerfil", nombreArchivo, BuildConfig.SUPABASE_API_KEY, "Bearer " + BuildConfig.SUPABASE_API_KEY, body, true).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onUrlObtenida("OK");
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "sin cuerpo";
                        Log.e("Supabase", "Código: " + response.code() + ", error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    callback.onUrlObtenida("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("Supabase", "Fallo al subir imagen: " + t.getMessage(), t);
                callback.onUrlObtenida("");
            }
        });
    }
}