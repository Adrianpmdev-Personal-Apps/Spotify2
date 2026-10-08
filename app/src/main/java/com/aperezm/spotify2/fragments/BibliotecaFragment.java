package com.aperezm.spotify2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aperezm.spotify2.activities.SpotifyActivity;
import com.aperezm.spotify2.adapters.CancionBibliotecaAdapter;
import com.aperezm.spotify2.data.api.DeezerApiClient;
import com.aperezm.spotify2.databinding.FragmentBibliotecaBinding;
import com.aperezm.spotify2.models.api.Cancion;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que representa la pantalla que muestra las canciones disponibles.
 *
 * @author Adrián Pérez Morales
 */
public class BibliotecaFragment extends Fragment {
    private FragmentBibliotecaBinding binding;
    private final List<Cancion> canciones = new ArrayList<>();

    /**
     * Infla el layout del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBibliotecaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Configura la vista una vez creada.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SpotifyActivity) getActivity()).setToolbarTitle("Biblioteca");
        cargarListaCanciones();
    }

    /**
     * Carga la lista de canciones en el RecyclerView.
     * Busca varias canciones populares en la API de Deezer y las agrega a la lista de canciones.
     */
    private void cargarListaCanciones() {
        buscarCancionEnApiDeezer("Proxy");
        buscarCancionEnApiDeezer("The Techno Code");
        buscarCancionEnApiDeezer("Tremor (Sensation 2014 Anthem)");
        buscarCancionEnApiDeezer("Hot Together");
        buscarCancionEnApiDeezer("New Rules");
        buscarCancionEnApiDeezer("Mask Off");
        buscarCancionEnApiDeezer("Eminence Front");
        buscarCancionEnApiDeezer("Heavy Is the Crown");
        buscarCancionEnApiDeezer("Stayin' Alive");
        buscarCancionEnApiDeezer("Ezio's Family");

        binding.recyclerViewCanciones.setLayoutManager(new LinearLayoutManager(requireContext()));
        CancionBibliotecaAdapter cancionBibliotecaAdapter = new CancionBibliotecaAdapter(canciones);
        binding.recyclerViewCanciones.setAdapter(cancionBibliotecaAdapter);
    }

    /**
     * Busca una canción por su título en la API de Deezer y la agrega a la lista de canciones si se encuentra.
     *
     * @param tituloCancion El título de la canción a buscar en la API de Deezer.
     */
    private void buscarCancionEnApiDeezer(String tituloCancion) {
        DeezerApiClient.buscarCancion(tituloCancion, cancion -> {
            if (cancion != null) {
                canciones.add(cancion);
                binding.recyclerViewCanciones.getAdapter().notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "No se encontró la canción: " + tituloCancion, Toast.LENGTH_SHORT).show();
            }
        });
    }
}