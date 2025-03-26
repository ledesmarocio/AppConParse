package com.example.appconparse.model;
import android.os.Bundle;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")

public class Post extends ParseObject {
    public String getId(){
        return getObjectId();
    }

    public String getTitulo(){
        return getString("titulo");
    }

    public void setTitulo(String titulo) {
        if (titulo != null) {
            put("titulo", titulo);
        } else {

            put("titulo", "Sin titulo");
        }
    }

    public String getDescripcion(){
        return getString("descripcion");
    }
    public void setDescripcion(String descripcion){
        if (descripcion != null) {
            put("descripcion", descripcion);
        } else {

            put("descripcion", "Sin descripcion");
        }
    }

    public int getDuracion(){
        return getInt("duracion");
    }
    public void setDuracion(int duracion){
        if (duracion!=0) {
            put("duracion", duracion);
        } else {

            put("duracion", 0);
        }
    }

    public String getCategoria(){
        return getString("categoria");
    }
    public void setCategoria(String categoria){
        if (categoria != null) {
            put("categoria", categoria);
        } else {
            put("categoria", "Sin categoria");
        }
    }
    public double getPresupuesto(){
        return getDouble("presupuesto");
    }
    public void setPresupuesto(Double presupuesto){
        if (presupuesto != null) {
            put("presupuesto", presupuesto);
        } else {

            put("presupuesto", "0.0");
        }
    }

    public List<String> getImagenes(){
        return  getList("imagenes");
    }

    public void setImagenes(List<String>imagenes){
        put("imagenes", imagenes);
    }

    public User getUser(){
        return (User)getParseObject("user");
    }
    public void setUser(User user){
        put("user", user);
    }
    // Método para exportar los datos del post como un Bundle
    public Bundle toBundle(){
        Bundle bundle= new Bundle();
        bundle.putString("titulo", getTitulo());
        bundle.putString("descripcion", getDescripcion());
        bundle.putString("categoria", getCategoria());
        bundle.putInt("duracion", getDuracion());
        bundle.putDouble("presupuesto", getPresupuesto());

        //Datos del Usuario

        User user=getUser();

        if(user!=null){
            bundle.putString("username", user.getUsername());
            bundle.putString("email", user.getEmail());
            bundle.putString("fotoperfil", user.getString("foto_perfil"));
        }

        //Lista de imágenes
        bundle.putStringArrayList("imagenes", new ArrayList<>(getImagenes()));
        return bundle;
    }

}
