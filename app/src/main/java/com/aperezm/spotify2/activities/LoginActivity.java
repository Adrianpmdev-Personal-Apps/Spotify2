package com.aperezm.spotify2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.aperezm.spotify2.data.db.FirebaseService;
import com.aperezm.spotify2.databinding.ActivityLoginBinding;
import com.aperezm.spotify2.data.preferences.SharedPreferencesService;

/**
 * Clase que representa la actividad de inicio de sesion de la aplicacion.
 * Permite al usuario iniciar sesion en su cuenta.
 *
 * @author Adrián Pérez Morales
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private SharedPreferencesService sharedPreferencesService;

    /**
     * Método llamado al crear la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferencesService = new SharedPreferencesService(this);

        // Recuperar email si viene del registro
        String emailRegistrado = getIntent().getStringExtra("email");

        if (emailRegistrado != null && !emailRegistrado.isEmpty()) {
            binding.loginUsername.setText(emailRegistrado);
        }

        // Configuración de botones
        configurarBotonLogin();
        configurarBotonPasarARegistro();
    }

    /**
     * Configura el botón de inicio de sesión para verificar las credenciales del usuario.
     */
    private void configurarBotonLogin() {
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.loginUsername.getText().toString().trim();
            String password = binding.loginPassword.getText().toString().trim();

            // Se comprueba que los campos no estén vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Se comprueban las credenciales del usuario
            FirebaseService.loginValido(email, password, esValido -> {
                if (esValido) {
                    sharedPreferencesService.setUsuarioLogeado(email);
                    Toast.makeText(this, "¡Inicio de sesión correcto!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, SpotifyActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Inicio de sesión incorrecto...", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     * Configura el botón para pasar a la actividad de registro.
     */
    private void configurarBotonPasarARegistro() {
        binding.loginToRegister.setOnClickListener(v -> {
            v.animate()
                    .scaleX(1.08f)
                    .scaleY(1.08f)
                    .setDuration(80)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(80)
                                .withEndAction(() -> {
                                    startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
                                    finish();
                                })
                                .start();
                    })
                    .start();
        });
    }
}