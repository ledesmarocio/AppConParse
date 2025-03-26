package com.example.appconparse.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.appconparse.databinding.ActivityMainBinding;
import com.example.appconparse.providers.AuthProvider;
import com.example.appconparse.util.Validaciones;
import com.example.appconparse.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private AuthProvider authProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        authProvider=new AuthProvider();
        manejarEventos();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void manejarEventos() {
        binding.tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.itUsuario.getText().toString().trim();
                String pass = binding.itPassword.getText().toString().trim();

                if (!Validaciones.validarMail(email)) {
                    showToast("Email incorrecto");
                    return;
                }
                if (!Validaciones.controlarPassword(pass)) {
                    showToast("Password incorrecto");
                    return;
                }

                viewModel.login(email, pass).observe(MainActivity.this, user_id -> {
                    if (user_id!=null) {

                        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                        //   intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    } else {
                        showToast("Login fallido");
                    }
                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        limpiarCampos();
    }
    private void limpiarCampos() {
        binding.itUsuario.setText("");
        binding.itPassword.setText("");

    }


}