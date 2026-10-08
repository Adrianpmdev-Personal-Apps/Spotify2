package com.aperezm.spotify2.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.firebase.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase de utilidades para la aplicación Spotify.
 *
 * @author Adrián Pérez Morales
 */
public class SpotifyUtils {

    /**
     * Carga una imagen desde una URL en un ImageView usando Glide.
     *
     * @param context Contexto de la aplicación
     * @param url URL de la imagen
     * @param imageView ImageView donde cargar la imagen
     * @param idPlaceholder ID del recurso de imagen a mostrar mientras se carga la imagen real
     */
    public static void cargarImagenGlide(Context context, String url, ImageView imageView, int idPlaceholder) {
        Glide.with(context)
                .load(url)
                .placeholder(idPlaceholder)
                .into(imageView);
    }

    /**
     * Obtiene el saludo apropiado según la hora actual del día.
     *
     * @return Saludo basado en la hora: "Buenos días", "Buenas tardes" o "Buenas noches"
     */
    public static String getMomentoDelDia() {
        final int HORA_INICIO_MANANA = 6;
        final int HORA_INICIO_TARDE = 14;
        final int HORA_INICIO_NOCHE = 20;

        int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hora >= HORA_INICIO_MANANA && hora < HORA_INICIO_TARDE) {
            return "Buenos días";
        } else if (hora >= HORA_INICIO_TARDE && hora < HORA_INICIO_NOCHE) {
            return "Buenas tardes";
        } else {
            return "Buenas noches";
        }
    }

    /**
     * Hashea una contraseña utilizando el algoritmo SHA-256.
     *
     * @param password La contraseña a hashear
     * @return El hash de la contraseña en formato hexadecimal
     */
    public static String hashearPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convierte una fecha de nacimiento dada por día, mes y año en un objeto Timestamp.
     * Además, valida que la fecha sea real.
     *
     * @param dia El día de nacimiento
     * @param mes El mes de nacimiento
     * @param anio El año de nacimiento
     * @return Un objeto Timestamp representando la fecha de nacimiento
     */
    public static Timestamp obtenerTimestampNacimiento(String dia, String mes, String anio) {
        int diaInt = Integer.parseInt(dia);
        int mesInt = Integer.parseInt(mes);
        int anioInt = Integer.parseInt(anio);

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(anioInt, mesInt - 1, diaInt, 0, 0, 0);
        Date date = calendar.getTime();

        return new Timestamp(date);
    }
}