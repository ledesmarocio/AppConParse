package com.example.appconparse.view;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconparse.R;
import com.example.appconparse.adapters.EfectoTransformer;
import com.example.appconparse.adapters.ImageSliderAdapter;
import com.example.appconparse.databinding.ActivityPostDetailBinding;
import com.example.appconparse.viewmodel.PostDetailViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;
    private PostDetailViewModel viewModel;
    private String postId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        postId=getIntent().getStringExtra("idPost");
        detailInfo();
        setupObservers();
        if (postId != null) {
            viewModel.fetchComments(postId);
        }
        binding.fabComentar.setOnClickListener(v -> showDialogComment());
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("¡COMENTARIO!");
        alert.setMessage("Ingresa tu comentario: ");

        EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Texto");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(params);
        params.setMargins(36, 0, 36, 36);


        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);
        alert.setView(container);


        alert.setPositiveButton("Ok", (dialog, which) -> {
            String value = editText.getText().toString().trim();
            if (!value.isEmpty()) {

                viewModel.saveComment(postId, value);
            } else {
                Toast.makeText(PostDetailActivity.this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", (dialog, which) -> {

            dialog.dismiss();
        });


        alert.show();
    }

    private void setupObservers() {
        viewModel.getCommentsLiveData().observe(this, comments -> {

            // updateUI(comments);
        });


        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void detailInfo() {

        binding.nameUser.setText(getIntent().getStringExtra("username"));
        binding.emailUser.setText(getIntent().getStringExtra("email"));
        binding.insta.setText(getIntent().getStringExtra("redsocial"));

        String fotoUrl = getIntent().getStringExtra("foto_perfil");
        if (fotoUrl != null) {
            Picasso.get()
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.circleImageView);
        } else {
            binding.circleImageView.setImageResource(R.drawable.ic_person);
        }

        ArrayList<String> urls = getIntent().getStringArrayListExtra("imagenes");
        String titulo = "Lugar: " + getIntent().getStringExtra("titulo");
        binding.lugar.setText(titulo);
        String categoria = "Categoria: " + getIntent().getStringExtra("categoria");
        binding.categoria.setText(categoria);
        String comentario = "descripción: " + getIntent().getStringExtra("descripcion");
        binding.description.setText(comentario);
        String duracion = "Duración: " + getIntent().getIntExtra("duracion", 0) + " día/s";
        binding.duracion.setText(duracion);
        String presupuesto = "Presupuesto: U$ " + getIntent().getDoubleExtra("presupuesto", 0.0);
        binding.presupuesto.setText(presupuesto);

        if (urls != null && !urls.isEmpty()) {
            ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(urls);
            binding.viewPager.setAdapter(imageSliderAdapter);
            binding.viewPager.setPageTransformer(new EfectoTransformer());

            // Conexión TabLayout con ViewPager2
            new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            }).attach();
        }
    }
}


