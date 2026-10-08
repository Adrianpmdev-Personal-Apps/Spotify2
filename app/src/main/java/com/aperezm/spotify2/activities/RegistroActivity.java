package com.aperezm.spotify2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.aperezm.spotify2.data.db.FirebaseService;
import com.aperezm.spotify2.databinding.ActivityRegistroBinding;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.util.Calendar;

/**
 * Clase que representa la actividad de registro de la aplicacion.
 * Permite al usuario registrarse creando una nueva cuenta.
 *
 * @author Adrián Pérez Morales
 */
public class RegistroActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;

    /**
     * Metodo llamado al crear la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarBotonRegistro();
        configurarBotonPasarALogin();
    }

    /**
     * Configura el boton de registro.
     * Valida los campos del formulario y registra un nuevo usuario si todo es correcto.
     */
    private void configurarBotonRegistro() {
        binding.registerButton.setOnClickListener(v -> {
            String email = binding.registerEmail.getText().toString().trim();
            String nombre = binding.registerName.getText().toString().trim();
            String password = binding.registerPassword.getText().toString().trim();
            String passwordRepetir = binding.registerRepeatPassword.getText().toString().trim();
            String dia = binding.registerDay.getText().toString().trim();
            String mes = binding.registerMonth.getText().toString().trim();
            String anio = binding.registerYear.getText().toString().trim();
            int idGenero = binding.registerGenderGroup.getCheckedRadioButtonId();
            String genero = idGenero == -1 ? "" : ((RadioButton) findViewById(idGenero)).getText().toString();

            if (!esFormularioValido(email, nombre, password, passwordRepetir, dia, mes, anio, genero)) {
                return;
            }

            FirebaseService.usuarioExistente(email, existe -> {
                if (existe) {
                    Toast.makeText(this, "Este correo electrónico ya está en uso.", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseService.registrarUsuario(email, nombre, password, dia, mes, anio, genero);
                    Toast.makeText(this, "Registro completado exitosamente", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            });
        });
    }

    /**
     * Configura el boton para pasar a la actividad de login.
     * Redirige al usuario a la pantalla de inicio de sesion.
     */
    private void configurarBotonPasarALogin() {
        binding.registerToLogin.setOnClickListener(v -> {
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
                                    startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .start();
                    })
                    .start();
        });
    }

    /**
     * Valida todos los campos del formulario.
     */
    private boolean esFormularioValido(String email, String nombre, String password, String passwordRepetir, String dia, String mes, String anio, String genero) {
        if (camposVacios(email, nombre, password, passwordRepetir, dia, mes, anio, genero)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!emailValido(email)) {
            binding.registerEmail.setError("El correo electrónico ingresado no es válido. Verifica el formato: usuario@dominio.com");
            return false;
        }

        if (!passwordValida(password)) {
            binding.registerPassword.setError("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial");
            return false;
        }

        if (!password.equals(passwordRepetir)) {
            binding.registerRepeatPassword.setError("Las contraseñas deben coincidir");
            return false;
        }

        if (!esFechaValida(dia, mes, anio)) {
            Toast.makeText(this, "Fecha inválida. Usa DD/MM/AAAA entre 1950 y el año actual", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Verifica si alguno de los campos del formulario está vacío.
     */
    private boolean camposVacios(String email, String nombre, String password, String passwordRepetir, String dia, String mes, String anio, String genero) {
        return email.isEmpty() || nombre.isEmpty() || password.isEmpty() || passwordRepetir.isEmpty()
                || dia.isEmpty() || mes.isEmpty() || anio.isEmpty() || genero.isEmpty();

    }

    /**
     * Verifica si la contraseña cumple con los requisitos de formato.
     */
    private boolean passwordValida(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(pattern);
    }

    /**
     * Verifica si el correo electrónico tiene un formato válido.
     */
    private boolean emailValido(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Verifica si la fecha tiene un formato válido y es una fecha real.
     */
    private boolean esFechaValida(String dia, String mes, String anio) {
        try {
            int anioInt = Integer.parseInt(anio);
            int anioActual = Calendar.getInstance().get(Calendar.YEAR);

            if (anioInt < 1950 || anioInt > anioActual) {
                return false;
            }

            SpotifyUtils.obtenerTimestampNacimiento(dia, mes, anio);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}