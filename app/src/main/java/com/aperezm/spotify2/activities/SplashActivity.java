package com.aperezm.spotify2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.aperezm.spotify2.data.preferences.SharedPreferencesService;

/**
 * SplashScreen de la app que muestra el splash nativo (fondo negro + logo)
 * durante 2 segundos y redirige según login.
 *
 * @author Adrián Pérez Morales
 */
public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY_MS = 2000;

    private SharedPreferencesService sharedPreferencesService;
    private boolean splashFinished = false;

    /**
     * Método llamado al crear la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> !splashFinished);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        sharedPreferencesService = new SharedPreferencesService(this);

        // Espera 2 segundos, luego redirige
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            splashFinished = true;
            Intent intent;

            if (sharedPreferencesService.hayUsuarioLogeado()) {
                intent = new Intent(this, SpotifyActivity.class);
            } else {
                intent = new Intent(this, BienvenidaActivity.class);
            }

            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);
    }
}