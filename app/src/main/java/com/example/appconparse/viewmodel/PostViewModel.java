package com.example.appconparse.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import com.example.appconparse.model.Post;
import com.example.appconparse.providers.PostProvider;
import com.parse.ParseObject;

import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<String> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;
    private final MutableLiveData<List<ParseObject>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

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

}


