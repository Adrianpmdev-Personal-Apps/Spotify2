package com.aperezm.spotify2.activities;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aperezm.spotify2.data.api.SupabaseApiClient;
import com.aperezm.spotify2.databinding.ActivityBienvenidaBinding;

/**
 * Actividad de bienvenida que muestra un video de fondo y botones para iniciar sesión o registrarse.
 * Esta actividad se ejecuta la primera vez que el usuario abre la aplicación o al cerrar sesión.
 *
 * @author Adrián Pérez Morales
 */
public class BienvenidaActivity extends AppCompatActivity implements SurfaceTextureListener {
    private ActivityBienvenidaBinding binding;
    private MediaPlayer mediaPlayer;

    /**
     * Metodo llamado al crear la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityBienvenidaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backgroundVideo.setSurfaceTextureListener(this);
        configuracionBotones();
    }

    /**
     * Metodo llamado cuando la textura de la superficie está disponible.
     * Aquí se inicializa y configura el MediaPlayer para reproducir el video de fondo.
     */
    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        SupabaseApiClient.obtenerUrlSupabaseStorage("Videos", "video_bienvenida.mp4", urlVideo -> {
            if (!urlVideo.isEmpty()) {
                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(urlVideo);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setSurface(new Surface(surface));
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setOnPreparedListener(mp -> {
                        configurarEscalaVideo();
                        mediaPlayer.start();

                        // Fade-in simultáneo para todos los elementos
                        int fadeDuration = 900;
                        binding.backgroundVideo.animate().alpha(1f).setDuration(fadeDuration).start();
                        binding.logo.animate().alpha(1f).setDuration(fadeDuration).start();
                        binding.welcomeText.animate().alpha(1f).setDuration(fadeDuration).start();
                        binding.loginButton.animate().alpha(1f).setDuration(fadeDuration).start();
                        binding.registerButton.animate().alpha(1f).setDuration(fadeDuration).start();
                    });
                } catch (Exception e) {
                    Toast.makeText(BienvenidaActivity.this, "No se pudo cargar el video de fondo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(BienvenidaActivity.this, "No se pudo cargar el video de fondo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo llamado cuando la textura de la superficie se destruye.
     * Aquí se libera el MediaPlayer para liberar recursos.
     */
    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        return true;
    }

    /**
     * Metodo llamado cuando el tamaño de la textura de la superficie cambia.
     */
    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {}

    /**
     * Metodo llamado cuando la textura de la superficie se actualiza.
     */
    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {}

    /**
     * Configura el escalado del video para que cubra toda la pantalla sin distorsión.
     */
    private void configurarEscalaVideo() {
        mediaPlayer.setOnVideoSizeChangedListener((mp, videoWidth, videoHeight) -> {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            float scale = Math.max(
                    (float) screenWidth / videoWidth,
                    (float) screenHeight / videoHeight
            );

            binding.backgroundVideo.setScaleX(scale * videoWidth / screenWidth);
            binding.backgroundVideo.setScaleY(scale * videoHeight / screenHeight);
        });
    }

    /**
     * Configura los botones de inicio de sesión y registro para navegar a las actividades correspondientes.
     */
    private void configuracionBotones() {
        binding.loginButton.setOnClickListener(v -> {
            startActivity(new Intent(BienvenidaActivity.this, LoginActivity.class));
            finish();
        });

        binding.registerButton.setOnClickListener(v -> {
            startActivity(new Intent(BienvenidaActivity.this, RegistroActivity.class));
            finish();
        });
    }
}