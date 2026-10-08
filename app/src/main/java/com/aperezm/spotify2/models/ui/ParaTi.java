package com.aperezm.spotify2.models.ui;

/**
 * Clase que representa un elemento de la sección "Para Ti" con su URL de imagen, título y subtítulo.
 *
 * @author Adrián Pérez Morales
 */
public class ParaTi {
    private String urlImagen;
    private String titulo;
    private String subtitulo;

    public ParaTi() {}

    public ParaTi(String urlImagen, String titulo, String subtitulo) {
        this.urlImagen = urlImagen;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    // GETTERS
    public String getUrlImagen() { return urlImagen; }
    public String getTitulo() { return titulo; }
    public String getSubtitulo() { return subtitulo; }

    // SETTERS
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }
}