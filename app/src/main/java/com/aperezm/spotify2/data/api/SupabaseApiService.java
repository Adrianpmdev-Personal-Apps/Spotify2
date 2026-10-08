package com.aperezm.spotify2.data.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaz para definir los endpoints de la API de Supabase relacionados con el almacenamiento.
 *
 * @author Adrián Pérez Morales
 */
public interface SupabaseApiService {
    @POST("storage/v1/object/sign/{bucket}/{nombreArchivo}")
    Call<ResponseBody> obtenerUrlFirmadaStorage(
            @Path("bucket") String bucket,
            @Path("nombreArchivo") String nombreArchivo,
            @Header("apikey") String apiKey,
            @Header("Authorization") String authorization,
            @Body RequestBody body
    );

    @PUT("storage/v1/object/{bucket}/{nombreArchivo}")
    Call<ResponseBody> subirImagenPerfil(
            @Path("bucket") String bucket,
            @Path("nombreArchivo") String nombreArchivo,
            @Header("apikey") String apiKey,
            @Header("Authorization") String authorization,
            @Body RequestBody body,
            @Query("upsert") boolean upsert
    );
}