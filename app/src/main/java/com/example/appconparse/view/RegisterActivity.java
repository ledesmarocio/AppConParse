package com.example.appconparse.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.appconparse.databinding.ActivityRegisterBinding;
import com.example.appconparse.model.User;
import com.example.appconparse.util.Validaciones;
import com.example.appconparse.viewmodel.RegisterViewModel;
public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.getRegisterResult().observe(this, result -> {
            if (result == null) {
                // Si el resultado es nulo, asumimos que el registro fue exitoso
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();  // Cierra la actividad de registro para que no se pueda volver atrás
            } else {
                showToast(result);
            }
        });
        manejarEventos();
    }

    private void manejarEventos() {
        // Evento volver a login
        binding.circleImageBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarRegistro();

            }

        });
    }

    private void realizarRegistro() {
        String usuario = binding.itUsuario.getText().toString().trim();
        String email = binding.itEmail.getText().toString().trim();
        String pass = binding.itPassword.getText().toString().trim();
        String pass1 = binding.itPassword1.getText().toString().trim();
        // Validaciones de entrada
        if (!Validaciones.validarTexto(usuario)) {
            showToast("Usuario incorrecto");
            return;
        }
        if (!Validaciones.validarMail(email)) {
            showToast("El correo no es válido");
            return;
        }
        String passError = Validaciones.validarPass(pass, pass1);
        if (passError != null) {
            showToast(passError);
            return;
        }

        User user = new User();
        //user.setRedSocial(redSocial);
        user.setEmail(email);
        user.setUsername(usuario);
        user.setPassword(pass);
        Log.d("RegisterActivity", "Usuario registrado: " + usuario + ", Email: " + email+" pass: "+pass);
        viewModel.register(user);
    }

    private void showToast(String message) {
        if (message != null) {
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}
