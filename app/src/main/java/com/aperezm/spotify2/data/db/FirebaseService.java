package com.aperezm.spotify2.data.db;

import android.util.Log;

import com.aperezm.spotify2.callbacks.LoginCallback;
import com.aperezm.spotify2.callbacks.ParaTiCallback;
import com.aperezm.spotify2.callbacks.PlaylistCallback;
import com.aperezm.spotify2.callbacks.UsuarioCallback;
import com.aperezm.spotify2.callbacks.UsuarioExistenteCallback;
import com.aperezm.spotify2.models.ui.ParaTi;
import com.aperezm.spotify2.models.ui.Playlist;
import com.aperezm.spotify2.models.ui.Usuario;
import com.aperezm.spotify2.utils.SpotifyUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para interactuar con Firebase Firestore.
 *
 * @author Adrián Pérez Morales
 */
public class FirebaseService {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Extrae la información de un documento "ParaTi" de Firestore y la devuelve a través de un callback.
     * El callback se utiliza para esperar a que la operación de extracción se complete.
     *
     * @param id El ID del documento "ParaTi" a extraer.
     * @param callback El callback que se llamará con el objeto "ParaTi" extraído. Si el documento no existe, se devolverá un objeto "ParaTi" vacío.
     */
    public static void extraerInfoParaTi(String id, ParaTiCallback callback) {
        db.collection("para_ti")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    ParaTi paraTi = new ParaTi();

                    if (documentSnapshot.exists()) {
                        String urlImagen = documentSnapshot.getString("url");
                        String titulo = documentSnapshot.getString("titulo");
                        String subtitulo = documentSnapshot.getString("subtitulo");

                        paraTi.setUrlImagen(urlImagen);
                        paraTi.setTitulo(titulo);
                        paraTi.setSubtitulo(subtitulo);

                        Log.d("FirebaseService", "Obtenido ParaTi con ID: " + id);
                        callback.onParaTiResultado(paraTi);
                    } else {
                        Log.d("FirebaseService", "No existe el documento ParaTi con ID: " + id);
                        callback.onParaTiResultado(paraTi);
                    }
                });
    }

    /**
     * Extrae la información de un documento "Playlist" de Firestore y la devuelve a través de un callback.
     * El callback se utiliza para esperar a que la operación de extracción se complete.
     *
     * @param id El ID del documento "Playlist" a extraer.
     * @param callback El callback que se llamará con el objeto "Playlist" extraído. Si el documento
     *                 no existe, se devolverá un objeto "Playlist" vacío.
     */
    public static void extraerInfoPlaylist(String id, PlaylistCallback callback) {
        db.collection("tus_playlists")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Playlist playlist = new Playlist();

                    if (documentSnapshot.exists()) {
                        String urlImagen = documentSnapshot.getString("url");
                        String nombre = documentSnapshot.getString("nombre");

                        playlist.setUrlImagen(urlImagen);
                        playlist.setNombre(nombre);

                        Log.d("FirebaseService", "Obtenido Playlist con ID: " + id);
                        callback.onPlaylistResultado(playlist);
                    } else {
                        Log.d("FirebaseService", "No existe el documento Playlist con ID: " + id);
                        callback.onPlaylistResultado(playlist);
                    }
                });
    }

    /**
     * Verifica si un correo electrónico ya está registrado en Firestore.
     *
     * @param email El correo electrónico a verificar.
     * @param callback El callback que se llamará con el resultado de la verificación. Se devolverá true si el
     *                 correo electrónico ya existe, false en caso contrario.
     */
    public static void usuarioExistente(String email, UsuarioExistenteCallback callback) {
        db.collection("usuarios")
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    callback.onUsuarioExistenteResultado(documentSnapshot.exists());
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al comprobar usuario existente", e);
                    callback.onUsuarioExistenteResultado(false);
                });
    }

    /**
     * Registra un nuevo usuario en Firestore creando un documento con la información proporcionada.
     *
     * @param email El correo electrónico del nuevo usuario.
     * @param nombre El nombre del nuevo usuario.
     * @param password La contraseña del nuevo usuario (será hasheada antes de almacenarla).
     * @param dia El día de nacimiento del nuevo usuario.
     * @param mes El mes de nacimiento del nuevo usuario.
     * @param anio El año de nacimiento del nuevo usuario.
     * @param genero El género del nuevo usuario.
     */
    public static void registrarUsuario(String email, String nombre, String password, String dia, String mes, String anio, String genero) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("email", email);
        usuario.put("fecha_nacimiento", SpotifyUtils.obtenerTimestampNacimiento(dia, mes, anio));
        usuario.put("genero", genero);
        usuario.put("nombre", nombre);
        usuario.put("password", SpotifyUtils.hashearPassword(password));

        db.collection("usuarios")
                .document(email)
                .set(usuario);
    }

    /**
     * Verifica si las credenciales de inicio de sesión son válidas comparando el correo electrónico y la contraseña
     * hasheada con los datos almacenados en Firestore.
     *
     * @param email El correo electrónico del usuario que intenta iniciar sesión.
     * @param password La contraseña del usuario que intenta iniciar sesión.
     * @param callback El callback que se llamará con el resultado de la verificación. Se devolverá true si las credenciales
     *                 son válidas, false en caso contrario.
     */
    public static void loginValido(String email, String password, LoginCallback callback) {
        String hashPassword = SpotifyUtils.hashearPassword(password);

        db.collection("usuarios")
                .whereEqualTo("email", email)
                .whereEqualTo("password", hashPassword)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    boolean esLoginValido = !querySnapshot.isEmpty();
                    callback.onLoginResultado(esLoginValido);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al obtener usuario", e);
                    callback.onLoginResultado(false);
                });
    }

    /**
     * Extrae la información completa de un usuario dado su correo electrónico y la devuelve a través de un callback.
     *
     * @param email El correo electrónico del usuario del cual se desea extraer la información.
     * @param callback El callback que se llamará con el objeto "Usuario" extraído. Si el documento no existe o ocurre un error, se devolverá null.
     */
    public static void extraerInfoUsuario(String email, UsuarioCallback callback) {
        db.collection("usuarios")
                .document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String password = documentSnapshot.getString("password");
                        Timestamp fechaNacimiento = documentSnapshot.getTimestamp("fecha_nacimiento");
                        String genero = documentSnapshot.getString("genero");

                        Usuario usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setNombre(nombre);
                        usuario.setPassword(password);
                        usuario.setFechaNacimiento(fechaNacimiento);
                        usuario.setGenero(genero);

                        callback.onUsuarioResultado(usuario);
                    } else {
                        Log.d("FirebaseService", "No existe el documento de usuario con email: " + email);
                        callback.onUsuarioResultado(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Error al obtener información del usuario", e);
                    callback.onUsuarioResultado(null);
                });
    }
}