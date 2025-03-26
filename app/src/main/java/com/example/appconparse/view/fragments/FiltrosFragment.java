package com.example.appconparse.view.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.appconparse.R;
import com.example.appconparse.adapters.PostAdapter;
import com.example.appconparse.databinding.FragmentFiltrosBinding;
import com.example.appconparse.model.Post;
import com.example.appconparse.providers.PostProvider;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class FiltrosFragment extends Fragment {

    private FragmentFiltrosBinding binding;
    private String categoria;
    private PostProvider postProvider;
    private PostAdapter adapter;
    public FiltrosFragment() {
        postProvider= new PostProvider();
    }

    public static FiltrosFragment newInstance(String param1, String param2) {
        return new FiltrosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentFiltrosBinding.inflate(inflater,container,false);
        setupCategorySpinner();
        binding.recyclerViewFiltros.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(new ArrayList<>());
        binding.recyclerViewFiltros.setAdapter(adapter);
        return binding.getRoot();
    }


    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.categorias_array) );// crea y configura el spinner con las categorias

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // define layout de los items en el desplegable
        binding.spinnerFiltro.setAdapter(adapter); //asigna el spinner junto con las categorias
        binding.spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (String) parent.getItemAtPosition(position);
                Log.d("FiltrosFragment", "Categoría seleccionada: " + categoria);
                cargarPostsPorCategoria(categoria);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoria = null;
            }
        });
    }
    private void cargarPostsPorCategoria(String categoria) {
        LiveData<List<Post>> postListLiveData = postProvider.getPostsByCategory(categoria);
        postListLiveData.observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                adapter.setPosts(posts);  // Pasa los datos de la lista al adaptador
            } else {
                Log.d("FiltrosFragment", "No hay posts para la categoría: " + categoria);
                adapter.setPosts(new ArrayList<>());  // Limpia el adaptador si no hay posts
            }
        });
    }

}
