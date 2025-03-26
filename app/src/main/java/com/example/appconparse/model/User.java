package com.example.appconparse.model;
import android.util.Log;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")

public class User extends ParseObject {


    public User() {
    }


    public String getRedSocial() {
        return getString("redsocial");
    }

    public void setRedSocial(String redSocial) {
        if (redSocial != null) {
            this.put("redsocial", redSocial);
        } else {
            Log.e("User", "El valor de redsocial es nulo. No se guardará en Parse.");
        }
    }

    public String getFotoperfil() {
        return getString("foto_perfil");
    }

    public void setFotoperfil(String fotoperfil) {
        if (fotoperfil != null) {
            put("foto_perfil", fotoperfil);
        }
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        if (email != null) {
            put("email", email);
        } else {
            Log.w("User", "El correo electrónico es nulo");
        }
    }

    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public String getId(){
        return getObjectId();
    }

}
