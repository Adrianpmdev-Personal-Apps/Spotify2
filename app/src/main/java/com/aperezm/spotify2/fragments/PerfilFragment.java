package com.aperezm.spotify2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aperezm.spotify2.R;
import com.aperezm.spotify2.activities.BienvenidaActivity;
import com.aperezm.spotify2.activities.SpotifyActivity;
import com.aperezm.spotify2.data.api.SupabaseApiClient;
import com.aperezm.spotify2.data.db.FirebaseService;
import com.aperezm.spotify2.databinding.FragmentPerfilBinding;
import com.aperezm.spotify2.data.preferences.SharedPreferencesService;
import com.aperezm.spotify2.utils.SpotifyUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Fragmento que representa la pantalla de perfil.
 *
 * @author Adrián Pérez Morales
 */
public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private SharedPreferencesService sharedPreferencesService;

    /**
     * Infla el layout del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        sharedPreferencesService = new SharedPreferencesService(requireContext());
        return binding.getRoot();
    }

    /**
     * Configura la vista una vez creada.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SpotifyActivity) requireActivity()).setToolbarTitle("Perfil");
        mostrarInformacionUsuario();
        configurarBotonLogout();
    }

    /**
     * Carga la imagen de perfil del usuario desde Supabase Storage y la muestra en el ImageView correspondiente.
     */
    public void cargarImagenPerfil() {
        String usuarioLogeado = sharedPreferencesService.getUsuarioLogeado();
        String nombreFoto = usuarioLogeado + "_perfil.png";

        SupabaseApiClient.obtenerUrlSupabaseStorage("ImagenesPerfil", nombreFoto, url -> {
            if (url != null) {
                SpotifyUtils.cargarImagenGlide(requireContext(), url, binding.profileImage, R.drawable.ic_user);
            } else {
                binding.profileImage.setImageResource(R.drawable.ic_user);
            }
        });
    }

    /**
     * Muestra la información del usuario logeado en los TextView correspondientes.
     */
    private void mostrarInformacionUsuario() {
        String usuarioLogeado = sharedPreferencesService.getUsuarioLogeado();

        FirebaseService.extraerInfoUsuario(usuarioLogeado, usuario -> {
            if (usuario != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fecha = sdf.format(usuario.getFechaNacimiento().toDate());

                binding.tvEmail.setText(usuario.getEmail());
                binding.tvName.setText(usuario.getNombre());
                binding.tvBirth.setText(fecha);
                binding.tvGender.setText(usuario.getGenero());
                cargarImagenPerfil();
            }
        });
    }

    /**
     * Configura el botón de logout para cerrar sesión y volver a la actividad de bienvenida.
     */
    private void configurarBotonLogout() {
        binding.btnLogout.setOnClickListener(v -> {
            sharedPreferencesService.cerrarSesion();
            startActivity(new Intent(requireActivity(), BienvenidaActivity.class));
            requireActivity().finish();
            Toast.makeText(requireContext(), "Has cerrado sesión...", Toast.LENGTH_SHORT).show();
        });
    }
}