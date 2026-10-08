package com.aperezm.spotify2.activities;

import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Looper;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.adapters.CancionReproductorAdapter;
import com.aperezm.spotify2.data.api.SupabaseApiClient;
import com.aperezm.spotify2.databinding.ActivityReproductorBinding;
import com.aperezm.spotify2.models.api.Cancion;

import java.util.List;
import java.util.Locale;

/**
 * Actividad que sirve para mostrar el reproductor de música.
 * Ahora recibe la lista completa y el índice de la canción a reproducir vía Intent.
 * Usa ViewPager2 para swipear entre canciones.
 *
 * @author Adrián Pérez Morales
 */
public class ReproductorActivity extends AppCompatActivity {
    private ActivityReproductorBinding binding;
    private MediaPlayer cancion; // MediaPlayer actual

    // Handler y Runnable necesarios para crear el bucle que actualiza la barra de tiempo
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateSeekBar;

    // Lista de canciones y posición actual
    private List<Cancion> listaCanciones;
    private int cancionActualIndex;

    /**
     * Método que se llama al crear la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityReproductorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Extrae lista y la posición seleccionada
        listaCanciones = (List<Cancion>) getIntent().getSerializableExtra("listaCanciones");
        cancionActualIndex = getIntent().getIntExtra("posicionCancion", 0);

        if (listaCanciones == null || listaCanciones.isEmpty()) {
            Toast.makeText(this, "No hay canciones en lista", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configura ViewPager2 y controles
        configurarViewPager();
        configurarControles();

        // Carga la canción inicial
        cargarCancion(cancionActualIndex);
    }

    /**
     * Configura el ViewPager2 para permitir swipe entre canciones y escucha cambios de página.
     */
    private void configurarViewPager() {
        CancionReproductorAdapter adapter = new CancionReproductorAdapter(listaCanciones);
        binding.viewPagerCanciones.setAdapter(adapter);
        binding.viewPagerCanciones.setCurrentItem(cancionActualIndex, true);

        // Callback: al hacer swipe, cambia la canción y actualiza el reproductor
        binding.viewPagerCanciones.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                cancionActualIndex = position;
                releaseMediaPlayer();
                cargarCancion(position);
            }
        });
    }

    /**
     * Configura los controles del reproductor.
     */
    private void configurarControles() {
        configurarSeekBarListener();
        configurarBotonPlay();
        configurarBotonAtras();
    }

    /**
     * Carga la canción seleccionada en el MediaPlayer y actualiza la UI.
     *
     * @param index Índice de la canción en la lista.
     */
    private void cargarCancion(int index) {
        Cancion cancionObj = listaCanciones.get(index);
        String nombreArchivo = cancionObj.getNombre() + ".mp3";

        SupabaseApiClient.obtenerUrlSupabaseStorage("Canciones", nombreArchivo, urlCancion -> {
            if (!urlCancion.isEmpty()) {
                cancion = new MediaPlayer();

                try {
                    cancion.setDataSource(urlCancion);
                    cancion.prepareAsync();
                    cancion.setOnPreparedListener(mp -> {
                        colocarTiempoTotal(cancion);
                        binding.playerSeekbar.setMax(cancion.getDuration());
                        reproducirCancion();
                    });
                } catch (Exception e) {
                    Toast.makeText(ReproductorActivity.this, "No se pudo cargar la canción", Toast.LENGTH_SHORT).show();
                }

                // Listener para cuando termina la canción
                cancion.setOnCompletionListener(mp -> {
                    binding.playerPlayPause.setImageResource(R.drawable.ic_play);
                    binding.playerSeekbar.setProgress(0);
                    binding.playerCurrentTime.setText(R.string.inicio_cancion);
                    handler.removeCallbacks(updateSeekBar);
                });
            } else {
                Toast.makeText(ReproductorActivity.this, "No se pudo cargar la canción", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * Coloca el tiempo total de la canción en el TextView correspondiente.
     *
     * @param cancion MediaPlayer con la canción cargada.
     */
    private void colocarTiempoTotal(MediaPlayer cancion) {
        int duracionMs = cancion.getDuration();
        binding.playerTotalTime.setText(formatearTiempo(duracionMs));
    }

    /**
     * Formatea el tiempo en milisegundos a formato mm:ss.
     *
     * @param milisegundos Tiempo en milisegundos.
     * @return String con el formato mm:ss.
     */
    private String formatearTiempo(int milisegundos) {
        int minutos = (milisegundos / 1000) / 60;
        int segundos = (milisegundos / 1000) % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutos, segundos);
    }

    /**
     * Configura el botón de play/pause para reproducir o pausar la canción.
     */
    private void configurarBotonPlay() {
        binding.playerPlayPause.setOnClickListener(v -> {
            // Animación de escalado al pulsar el botón
            v.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                    ).start();

            // Lógica de reproducción/pausa
            if (cancion != null) {
                if (cancion.isPlaying()) {
                    pausarCancion();
                } else {
                    reproducirCancion();
                }
            }
        });
    }

    /**
     * Inicia la reproducción de la canción y comienza el bucle de actualización de la barra de progreso.
     */
    private void reproducirCancion() {
        if (cancion != null) {
            cancion.start();
            binding.playerPlayPause.setImageResource(R.drawable.ic_pause);

            updateSeekBar = new Runnable() {
                @Override
                public void run() {
                    if (cancion != null && cancion.isPlaying()) {
                        int currentPosition = cancion.getCurrentPosition();
                        binding.playerSeekbar.setProgress(currentPosition);

                        // Se actualiza el tiempo actual mostrado
                        binding.playerCurrentTime.setText(formatearTiempo(currentPosition));

                        // Se llama a sí mismo cada 100ms para mantener la barra fluida
                        handler.postDelayed(this, 100);
                    }
                }
            };

            // Se inicia el bucle
            handler.post(updateSeekBar);
        }
    }

    /**
     * Pausa la reproducción de la canción y detiene la actualización de la barra de progreso.
     */
    private void pausarCancion() {
        if (cancion != null && cancion.isPlaying()) {
            cancion.pause();
            binding.playerPlayPause.setImageResource(R.drawable.ic_play);
            handler.removeCallbacks(updateSeekBar);
        }
    }

    /**
     * Configura el listener de la SeekBar para permitir al usuario saltar el tiempo de la canción.
     */
    private void configurarSeekBarListener() {
        binding.playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Se llama cuando el usuario arrastra la barra.
             * Actualiza el tiempo mostrado pero no salta en la canción hasta que suelta el dedo.
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    binding.playerCurrentTime.setText(formatearTiempo(progress));
                }
            }

            /**
             * Se llama cuando el usuario comienza a arrastrar la barra.
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            /**
             * Se llama cuando el usuario suelta la barra.
             * Salta en la canción al punto seleccionado.
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (cancion != null) {
                    cancion.seekTo(seekBar.getProgress());

                    if (cancion.isPlaying()) {
                        handler.post(updateSeekBar);
                    }
                }
            }
        });
    }

    /**
     * Configura el botón de retroceso para finalizar la actividad.
     */
    private void configurarBotonAtras() {
        binding.playerBack.setOnClickListener(v -> {
            // Animación de escalado al pulsar el botón
            v.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                    ).start();
            finish();
        });
    }

    /**
     * Libera los recursos del MediaPlayer para evitar fugas de memoria.
     */
    private void releaseMediaPlayer() {
        if (cancion != null) {
            cancion.release();
            cancion = null;
            handler.removeCallbacks(updateSeekBar);
        }
    }

    /**
     * Se llama al destruir la actividad para liberar recursos del MediaPlayer.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
}