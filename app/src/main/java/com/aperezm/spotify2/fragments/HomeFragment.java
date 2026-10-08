package com.aperezm.spotify2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aperezm.spotify2.activities.SpotifyActivity;
import com.aperezm.spotify2.adapters.ArtistaAdapter;
import com.aperezm.spotify2.adapters.ParaTiAdapter;
import com.aperezm.spotify2.adapters.PlaylistAdapter;
import com.aperezm.spotify2.data.api.DeezerApiClient;
import com.aperezm.spotify2.data.db.FirebaseService;
import com.aperezm.spotify2.models.api.Artista;
import com.aperezm.spotify2.models.ui.ParaTi;
import com.aperezm.spotify2.models.ui.Playlist;
import com.aperezm.spotify2.utils.SpotifyUtils;
import com.aperezm.spotify2.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que representa la pantalla de inicio.
 *
 * @author Adrián Pérez Morales
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private final List<Playlist> playlists = new ArrayList<>();
    private final List<ParaTi> paraTiLista = new ArrayList<>();
    private final List<Artista> artistas = new ArrayList<>();

    /**
     * Infla el layout del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Configura la vista una vez creada.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SpotifyActivity) getActivity()).setToolbarTitle(SpotifyUtils.getMomentoDelDia());
        cargarListaPlaylists();
        cargarListaParaTi();
        cargarListaArtistas();
    }

    /**
     * Carga la lista de playlists en el RecyclerView.
     * Extrae la información de Firebase para cada playlist y la agrega a la lista.
     */
    private void cargarListaPlaylists() {
        buscarPlaylistFirebase("1");
        buscarPlaylistFirebase("2");
        buscarPlaylistFirebase("3");
        buscarPlaylistFirebase("4");
        buscarPlaylistFirebase("5");
        buscarPlaylistFirebase("6");

        binding.rvPlaylists.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(playlists);
        binding.rvPlaylists.setAdapter(playlistAdapter);
    }

    /**
     * Carga la lista de recomendaciones "Para Ti" en el RecyclerView.
     * Extrae la información de Firebase para cada recomendación y la agrega a la lista.
     */
    private void cargarListaParaTi() {
        buscarParaTiFirebase("1");
        buscarParaTiFirebase("2");
        buscarParaTiFirebase("3");

        binding.rvParaTi.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        ParaTiAdapter paraTiAdapter = new ParaTiAdapter(paraTiLista);
        binding.rvParaTi.setAdapter(paraTiAdapter);
    }

    /**
     * Carga la lista de artistas populares en el RecyclerView.
     * Busca artistas específicos en la API de Deezer y los agrega a la lista.
     */
    private void cargarListaArtistas() {
        buscarArtistaEnApiDeezer("Martin Garrix");
        buscarArtistaEnApiDeezer("Charlotte de Witte");
        buscarArtistaEnApiDeezer("Dimitri Vegas & Like Mike");

        binding.rvArtistasPopulares.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        ArtistaAdapter artistaAdapter = new ArtistaAdapter(artistas);
        binding.rvArtistasPopulares.setAdapter(artistaAdapter);
    }

    /**+
     * Busca una recomendación "Para Ti" en Firebase por su ID y la agrega a la lista de recomendaciones.
     *
     * @param id El ID de la recomendación "Para Ti" a buscar en Firebase.
     */
    private void buscarParaTiFirebase(String id) {
        FirebaseService.extraerInfoParaTi(id, paraTi -> {
            paraTiLista.add(paraTi);
            binding.rvParaTi.getAdapter().notifyDataSetChanged();
        });
    }

    /**
     * Busca una playlist en Firebase por su ID y la agrega a la lista de playlists.
     *
     * @param id El ID de la playlist a buscar en Firebase.
     */
    private void buscarPlaylistFirebase(String id) {
        FirebaseService.extraerInfoPlaylist(id, playlist -> {
            playlists.add(playlist);
            binding.rvPlaylists.getAdapter().notifyDataSetChanged();
        });
    }

    /**
     * Busca un artista en la API de Deezer y lo agrega a la lista de artistas populares.
     *
     * @param nombreArtista El nombre del artista a buscar.
     */
    private void buscarArtistaEnApiDeezer(String nombreArtista) {
        DeezerApiClient.buscarArtista(nombreArtista, artista -> {
            if (artista != null) {
                artistas.add(artista);
                binding.rvArtistasPopulares.getAdapter().notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "No se pudo cargar el artista: " + nombreArtista, Toast.LENGTH_SHORT).show();
            }
        });
    }
}