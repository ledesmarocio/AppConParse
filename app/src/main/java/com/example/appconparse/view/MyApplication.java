package com.example.appconparse.view;

import com.example.appconparse.R;
import android.app.Application;

import com.example.appconparse.model.Post;
import com.example.appconparse.model.User;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;


public class MyApplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        ParseACL defaultACL=new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);

    }
}
