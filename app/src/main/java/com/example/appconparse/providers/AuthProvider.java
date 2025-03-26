package com.example.appconparse.providers;

import com.example.appconparse.model.User;
import com.parse.ParseUser;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AuthProvider {

    public AuthProvider() {
    }

    // Inicio de sesión
    public LiveData<String> signIn(String email, String password) {
        MutableLiveData<String> authResult = new MutableLiveData<>();

        // Cerrar sesión si ya hay una sesión activa
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut(); // Cerrar sesión activa antes de iniciar sesión nuevamente
            Log.d("AuthProvider", "Sesión cerrada antes de iniciar sesión.");
        }

        // Intentar iniciar sesión
        ParseUser.logInInBackground(email, password, (user, e) -> {
            if (e == null) {
                // Login exitoso
                authResult.setValue(user.getObjectId());
                Log.d("AuthProvider", "Usuario autenticado exitosamente: " + user.getObjectId());
            } else {
                // Error en el login
                Log.e("AuthProvider", "Error en inicio de sesión: ", e);
                authResult.setValue(null);
            }
        });
        return authResult;
    }

    // Registro de usuario
    public LiveData<String> signUp(User user) {
        MutableLiveData<String> authResult = new MutableLiveData<>();

        // Verificar que los datos no sean nulos
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            Log.e("AuthProvider", "Uno o más valores son nulos: " +
                    "Username=" + user.getUsername() + ", " +
                    "Password=" + user.getPassword() + ", " +
                    "Email=" + user.getEmail());
            authResult.setValue(null);
            return authResult;
        }

        // Cerrar sesión si ya hay una sesión activa antes de registrar un nuevo usuario
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut(); // Cerrar sesión activa antes de registrar un nuevo usuario
            Log.d("AuthProvider", "Sesión cerrada antes del registro.");
        }

        // Crear un nuevo ParseUser para el registro
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user.getUsername() != null ? user.getUsername() : "defaultUsername");
        parseUser.setPassword(user.getPassword() != null ? user.getPassword() : "defaultPassword");
        parseUser.setEmail(user.getEmail() != null ? user.getEmail() : "default@example.com");

        // Intentar registrar al nuevo usuario
        parseUser.signUpInBackground(e -> {
            if (e == null) {
                // Registro exitoso
                authResult.setValue(parseUser.getObjectId());
                Log.d("AuthProvider", "Usuario registrado exitosamente: " + parseUser.getObjectId());
            } else {
                // Error en el registro
                Log.e("AuthProvider", "Error en registro: ", e);
                authResult.setValue(null);
            }
        });
        return authResult;
    }

    // Cerrar sesión
    public LiveData<Boolean> logout() {
        MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
        ParseUser.logOutInBackground(e -> {
            if (e == null) {
                logoutResult.setValue(true);
                Log.d("AuthProvider", "Caché eliminada y usuario desconectado.");
            } else {
                logoutResult.setValue(false);
                Log.e("AuthProvider", "Error al desconectar al usuario: ", e);
            }
        });
        return logoutResult;
    }
}
