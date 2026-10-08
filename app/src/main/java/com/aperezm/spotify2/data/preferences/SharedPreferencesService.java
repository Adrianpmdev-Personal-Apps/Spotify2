package com.aperezm.spotify2.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase para gestionar las preferencias compartidas de la aplicación,
 * específicamente el estado de inicio de sesión del usuario.
 *
 * @author Adrián Pérez Morales
 */
public class SharedPreferencesService {
    private static final String PREFS_NAME = "prefs";
    private static final String KEY_USUARIO_LOGEADO = "usuarioLogeado";
    private final SharedPreferences prefs;

    /**
     * Constructor que inicializa las preferencias compartidas.
     *
     * @param context Contexto de la aplicación.
     */
    public SharedPreferencesService(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Obtiene el nombre del usuario actualmente logeado.
     *
     * @return Nombre del usuario logeado, o una cadena vacía si no hay ninguno.
     */
    public String getUsuarioLogeado() {
        return prefs.getString(KEY_USUARIO_LOGEADO, "");
    }

    /**
     * Establece el nombre del usuario actualmente logeado.
     *
     * @param usuario Nombre del usuario a establecer como logeado.
     */
    public void setUsuarioLogeado(String usuario) {
        prefs.edit().putString(KEY_USUARIO_LOGEADO, usuario).apply();
    }

    /**
     * Cierra la sesión del usuario actual, eliminando su información de las preferencias.
     */
    public void cerrarSesion() {
        prefs.edit().remove(KEY_USUARIO_LOGEADO).apply();
    }

    /**
     * Verifica si hay un usuario actualmente logeado.
     *
     * @return true si hay un usuario logeado, false en caso contrario.
     */
    public boolean hayUsuarioLogeado() {
        return !getUsuarioLogeado().isEmpty();
    }
}