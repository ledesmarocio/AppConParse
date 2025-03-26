package com.example.appconparse.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.example.appconparse.R;
import com.example.appconparse.databinding.FragmentPerfilBinding;
import com.example.appconparse.util.ImageUtils;
import com.example.appconparse.view.MisDatosActivity;
import com.example.appconparse.view.MisPublicacionesActivity;
import com.example.appconparse.viewmodel.PostViewModel;
import com.example.appconparse.viewmodel.UserViewModel;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private UserViewModel userViewModel;
    private PostViewModel postViewModel;

    public PerfilFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        // Configura el menú y la barra de herramientas
        setupMenu();
        setupToolbar();


        setupGalleryLauncher();


        setupProfileImageClick();

        setupButtons();

        // Observa cambios en el usuario actual
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), this::updateUI);

        updatePostsCount();

        return binding.getRoot();
    }

    // Configura el menú con la opción de cerrar sesión
    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.close_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemClose) {
                    // Lógica para cerrar sesión
                    Toast.makeText(requireContext(), "Cerrar sesión", Toast.LENGTH_SHORT).show();
                    // Aquí puedes agregar la lógica para cerrar sesión si es necesario
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    // Configura la barra de herramientas
    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.getRoot().findViewById(R.id.tools_filtro));
    }

    // Configura el lanzador para seleccionar una imagen de la galería
    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) handleImageSelection(imageUri);
                    }
                }
        );
    }

    // Configura el clic en la imagen de perfil para abrir la galería
    private void setupProfileImageClick() {
        binding.circleImageView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ImageUtils.pedirPermisos(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            ImageUtils.openGallery(requireContext(), galleryLauncher);
        });
    }

    // Configura los botones para ir a las actividades correspondientes
    private void setupButtons() {
        binding.btnPosts.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MisPublicacionesActivity.class));
        });

        binding.btnDatos.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MisDatosActivity.class));
        });
    }

    // Actualiza la interfaz de usuario con la información del usuario
    private void updateUI(ParseUser currentUser) {
        if (currentUser != null) {
            binding.nameUser.setText(currentUser.getUsername());
            binding.emailUser.setText(currentUser.getEmail());
            binding.insta.setText(currentUser.getString("redsocial"));

            String fotoUrl = currentUser.getString("foto_perfil");
            if (fotoUrl != null) {
                Picasso.get().load(fotoUrl).placeholder(R.drawable.ic_person).error(R.drawable.ic_person).into(binding.circleImageView);
            } else {
                binding.circleImageView.setImageResource(R.drawable.ic_person);
            }
        } else {
            Toast.makeText(getContext(), "Usuario no logueado", Toast.LENGTH_SHORT).show();
        }
    }

    // Maneja la selección de la imagen y la sube a Parse
    private void handleImageSelection(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            binding.circleImageView.setImageBitmap(bitmap);

            // Subir la imagen seleccionada a Parse
            ImageUtils.subirImagenAParse(requireContext(), imageUri, new ImageUtils.ImageUploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        currentUser.put("foto_perfil", imageUrl);
                        currentUser.saveInBackground(e -> {
                            if (e == null) {
                                Toast.makeText(requireContext(), "Foto subida correctamente", Toast.LENGTH_SHORT).show();
                                userViewModel.updateUser(currentUser); // Actualiza el usuario en el ViewModel
                            } else {
                                Toast.makeText(requireContext(), "Error al guardar la URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(requireContext(), "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Log.e("PerfilFragment", "Error al manejar la imagen: " + e.getMessage(), e);
            Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePostsCount() {
        // Obtener el número de publicaciones del usuario desde postviemodel
        postViewModel.getUserPostsCount().observe(getViewLifecycleOwner(), count -> {

            binding.tvPostsCount.setText(String.valueOf(count));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Actualiza los datos cada vez que el fragmento vuelva al primer plano
        actualizarDatosUsuario();
    }

    private void actualizarDatosUsuario() {
        ParseUser currentUser = userViewModel.getCurrentUser().getValue();
        if (currentUser != null) {
            // Actualiza los campos del perfil con los datos del usuario
            binding.emailUser.setText(currentUser.getEmail());
            binding.insta.setText(currentUser.getString("redsocial"));
        }
    }
}
