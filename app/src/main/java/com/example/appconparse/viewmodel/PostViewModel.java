package com.example.appconparse.viewmodel;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import com.example.appconparse.model.Post;
import com.example.appconparse.providers.PostProvider;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<String> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;
    private final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();


    public PostViewModel() {
        posts = new MutableLiveData<>();
        postProvider = new PostProvider();
    }

    public LiveData<String> getPostSuccess() {
        return postSuccess;
    }

    public void publicar(Post post) {
        postProvider.addPost(post)
                .observeForever(result -> {
                    postSuccess.setValue(result);
                });
    }

    public LiveData<List<Post>> getPosts() {
        posts = postProvider.getAllPosts();
        return posts;
    }



    public LiveData<List<Post>> getPostsByCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            postsLiveData.setValue(new ArrayList<>());
            return postsLiveData;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", currentUser);
        query.include("user");

        query.findInBackground((posts, e) -> {
            if (e == null) {
                postsLiveData.setValue(posts);
            } else {
                postsLiveData.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar los posts: ", e);
            }
        });
        return postsLiveData;
    }




    public LiveData<Integer> getUserPostsCount() {
        MutableLiveData<Integer> postCount = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Si no hay un usuario actual (usuario no está logueado)
        if (currentUser == null) {
            postCount.setValue(0);  // Si no hay usuario, entonces no hay publicaciones
            return postCount;
        }

        // Si el usuario está logueado, consulta las publicaciones
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user", currentUser);

        // Realizamos la consulta asíncrona
        query.countInBackground((count, e) -> {
            if (e == null) {
                // Si la consulta es exitosa, devuelve el número de publicaciones
                postCount.setValue(count);
            } else {
                // Si hay un error en la consulta, configura el contador a 0
                postCount.setValue(0);
                Log.e("ParseError", "Error al contar las publicaciones: ", e);
            }
        });
        return postCount;
    }


}


