package com.aperezm.spotify2.models.ui;

/**
 * Clase que representa una playlist con su nombre y su url de imagen.
 *
 * @author Adrián Pérez Morales
 */
public class Playlist {
    private String nombre;
    private String urlImagen;

    public Playlist() {}

    public Playlist(String nombre, String urlImagen) {
        this.nombre = nombre;
        this.urlImagen = urlImagen;
    }

    // GETTERS
    public String getNombre() { return nombre; }
    public String getUrlImagen() { return urlImagen; }

    // SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}