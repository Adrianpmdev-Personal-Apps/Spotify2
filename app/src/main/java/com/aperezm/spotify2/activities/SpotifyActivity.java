package com.aperezm.spotify2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.aperezm.spotify2.data.api.SupabaseApiClient;
import com.aperezm.spotify2.fragments.HomeFragment;
import com.aperezm.spotify2.fragments.BibliotecaFragment;
import com.aperezm.spotify2.fragments.PerfilFragment;
import com.aperezm.spotify2.R;
import com.aperezm.spotify2.databinding.ActivitySpotifyBinding;
import com.aperezm.spotify2.data.preferences.SharedPreferencesService;

import java.io.ByteArrayOutputStream;

/**
 * Actividad de la aplicación Spotify que gestiona la navegación entre fragmentos
 * y la funcionalidad de cámara para foto de perfil.
 *
 * @author Adrián Pérez Morales
 */
public class SpotifyActivity extends AppCompatActivity {
    private ActivitySpotifyBinding binding;
    private SharedPreferencesService sharedPreferencesService;
    private ActivityResultLauncher<Intent> tomarFotoLauncher;
    private ActivityResultLauncher<String> solicitarPermisoLauncher;
    private int ultimoItemSeleccionado = R.id.nav_home;

    // --- Métodos de ciclo de vida ---

    /**
     * Metodo llamado al crear la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySpotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferencesService = new SharedPreferencesService(this);

        configurarWindowInsets();
        configurarMenus();
        inicializarLanzadoresCamara();
        cargarFragmentoInicial();
    }

    /**
     * Infla el menú superior (toolbar) con opciones.
     *
     * @param menu El menú a inflar.
     * @return true si el menú se infla correctamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar_menu, menu);
        return true;
    }

    /**
     * Maneja la selección de elementos del menú superior (toolbar).
     *
     * @param item El elemento del menú seleccionado.
     * @return true si el elemento se maneja correctamente.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.web) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://spotify.com"));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --- Métodos públicos ---

    /**
     * Establece el título del toolbar superior.
     *
     * @param titulo El título a establecer.
     */
    public void setToolbarTitle(String titulo) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titulo);
        }
    }

    // --- Configuración de UI y menús ---

    /**
     * Esto permite que la aplicación utilice todo el espacio de la pantalla, sin
     * que se solape con la barra de estado superior del teléfono.
     */
    private void configurarWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.home, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Configura los menús superior (toolbar) e inferior (bottom navigation).
     */
    private void configurarMenus() {
        setSupportActionBar(binding.topToolbar);
        configurarMenuInferior();
    }

    /**
     * Configura el menú de navegación inferior y sus listeners.
     */
    private void configurarMenuInferior() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId != R.id.nav_camera) {
                ultimoItemSeleccionado = itemId;
            }

            if (itemId == R.id.nav_home) {
                reemplazarFragmento("home");
            } else if (itemId == R.id.nav_library) {
                reemplazarFragmento("musica");
            } else if (itemId == R.id.nav_camera) {
                manejarAccionCamara();
                return true;
            } else if (itemId == R.id.nav_user) {
                reemplazarFragmento("perfil");
            } else {
                return false;
            }

            return true;
        });
    }

    /**
     * Carga el fragmento inicial (HomeFragment) al iniciar la actividad.
     */
    private void cargarFragmentoInicial() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragmentContainerView, new HomeFragment())
                .commit();
    }

    /**
     * Reemplaza el fragmento actual con el fragmento especificado.
     *
     * @param nombreFragmento El nombre del fragmento a cargar ("home", "musica", "perfil").
     */
    private void reemplazarFragmento(String nombreFragmento) {
        Fragment nuevoFragmento;

        switch (nombreFragmento) {
            case "musica":
                nuevoFragmento = new BibliotecaFragment();
                break;
            case "perfil":
                nuevoFragmento = new PerfilFragment();
                break;
            default:
                nuevoFragmento = new HomeFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContainerView, nuevoFragmento)
                .commit();
    }

    // --- Cámara y permisos ---

    /**
     * Inicializa los lanzadores para tomar fotos y solicitar permisos de cámara.
     */
    private void inicializarLanzadoresCamara() {
        inicializarTomarFotoLauncher();
        inicializarSolicitarPermisoLauncher();
    }

    /**
     * Inicializa el lanzador para tomar fotos y procesar el resultado.
     */
    private void inicializarTomarFotoLauncher() {
        tomarFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    restaurarSeleccionBottomNav();
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        procesarResultadoFoto(result.getData());
                    }
                }
        );
    }

    /**
     * Inicializa el lanzador para solicitar permisos de cámara.
     */
    private void inicializarSolicitarPermisoLauncher() {
        solicitarPermisoLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        abrirCamara();
                    } else {
                        mostrarMensajePermisoDenegado();
                    }
                }
        );
    }

    /**
     * Maneja la acción de abrir la cámara verificando permisos.
     */
    private void manejarAccionCamara() {
        if (tienePermisoCamara()) {
            abrirCamara();
        } else {
            solicitarPermisoCamara();
        }
    }

    /**
     * Verifica si la aplicación tiene permiso para usar la cámara.
     *
     * @return true si tiene permiso, false en caso contrario.
     */
    private boolean tienePermisoCamara() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Abre la aplicación de cámara para tomar una foto.
     */
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tomarFotoLauncher.launch(intent);
    }

    /**
     * Solicita el permiso de cámara al usuario.
     */
    private void solicitarPermisoCamara() {
        solicitarPermisoLauncher.launch(Manifest.permission.CAMERA);
    }

    /**
     * Procesa el resultado de la foto tomada.
     *
     * @param data Los datos de la foto capturada.
     */
    private void procesarResultadoFoto(Intent data) {
        Bundle extras = data.getExtras();

        if (extras != null) {
            Bitmap foto = (Bitmap) extras.get("data");

            if (foto != null) {
                guardarFotoPerfil(foto);
            }
        }
    }

    /**
     * Restaura la selección del elemento previamente seleccionado en el menú inferior.
     * Esto se hace después de tomar una foto para evitar que el ícono de la cámara permanezca seleccionado.
     */
    private void restaurarSeleccionBottomNav() {
        binding.bottomNavigation.getMenu().findItem(R.id.nav_camera).setChecked(false);
        binding.bottomNavigation.getMenu().findItem(ultimoItemSeleccionado).setChecked(true);
    }

    /**
     * Muestra un mensaje cuando el permiso de cámara es denegado.
     */
    private void mostrarMensajePermisoDenegado() {
        Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
    }

    // --- Guardado y notificación de foto de perfil ---

    /**
     * Guarda la foto tomada usando Supabase Storage.
     *
     * @param foto La foto a guardar.
     */
    private void guardarFotoPerfil(Bitmap foto) {
        String emailUsuario = sharedPreferencesService.getUsuarioLogeado();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imagenBytes = stream.toByteArray();

        SupabaseApiClient.subirImagenPerfil(emailUsuario + "_perfil.png", imagenBytes, url -> {
            if (!url.isEmpty()) {
                Toast.makeText(this, "Foto guardada correctamente", Toast.LENGTH_SHORT).show();
                notificarActualizacionFotoPerfil();
            } else {
                Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Notifica al fragmento de perfil que se ha actualizado la foto de perfil.
     * Esto se hace para que el fragmento pueda actualizar su vista con la nueva foto,
     * ya que si se accede a la cámara desde el perfil y se hace la foto, se actualice automáticamente.
     */
    private void notificarActualizacionFotoPerfil() {
        if (ultimoItemSeleccionado == R.id.nav_user) {
            Fragment fragmentoActual = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

            if (fragmentoActual instanceof PerfilFragment) {
                ((PerfilFragment) fragmentoActual).cargarImagenPerfil();
            }
        }
    }
}