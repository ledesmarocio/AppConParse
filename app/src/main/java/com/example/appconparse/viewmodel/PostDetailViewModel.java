package com.example.appconparse.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appconparse.providers.PostProvider;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class PostDetailViewModel extends ViewModel {

    private final MutableLiveData<List<ParseObject>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final PostProvider postProvider;

    public PostDetailViewModel() {
        postProvider = new PostProvider();
    }

    public LiveData<List<ParseObject>> getCommentsLiveData() {
        return commentsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchComments(String postId) {
        postProvider.fetchComments(postId, new PostProvider.CommentsCallback() {
            @Override
            public void onSuccess(List<ParseObject> comments) {
                commentsLiveData.postValue(comments);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e.getMessage());
            }
        });
    }

    public void saveComment(String postId, String commentText) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        postProvider.saveComment(postId, commentText, currentUser, e -> {
            if (e == null) {
                fetchComments(postId);
            } else {
                errorLiveData.postValue(e.getMessage());
            }
        });
    }
}


