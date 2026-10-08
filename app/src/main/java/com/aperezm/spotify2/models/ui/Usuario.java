package com.aperezm.spotify2.models.ui;

import com.google.firebase.Timestamp;

/**
 * Clase que representa un usuario con su email, nombre, password, fecha de nacimiento y género.
 *
 * @author Adrián Pérez Morales
 */
public class Usuario {
    String email;
    String nombre;
    String password;
    Timestamp fechaNacimiento;
    String genero;

    public Usuario() {}

    public Usuario(String email, String nombre, String password, Timestamp fechaNacimiento, String genero) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
    }

    // GETTERS
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getPassword() { return password; }
    public Timestamp getFechaNacimiento() { return fechaNacimiento; }
    public String getGenero() { return genero; }

    // SETTERS
    public void setEmail(String email) { this.email = email; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPassword(String password) { this.password = password; }
    public void setFechaNacimiento(Timestamp fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setGenero(String genero) { this.genero = genero; }
}