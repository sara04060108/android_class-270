package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by user on 2016/7/19.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("tCpi53em9xNwNTCSOzXkGNqlhCfpa8fPqdYYPbvX")
                .server("https://parseapi.back4app.com/")
                .clientKey("sp3Lt8xh55WGfIAWr8Gdl3gIsAWvo3BJVH6Fgo3K")
                .build());
    }
}
