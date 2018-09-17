package com.semicolon.rests.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.semicolon.rests.models.UserModel;
import com.semicolon.rests.tags.Tags;

public class Preferences {
    private static Preferences instance=null;

    private Preferences() {
    }
    public static synchronized Preferences getInstance()
    {
        if (instance==null)
        {
            instance = new Preferences();
        }
        return instance;
    }

    public void CreateUpdateUserData(Context context,UserModel userModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_gson = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",user_gson);
        editor.apply();
        CreateUpdateSession(context, Tags.session_login);
    }

    public UserModel getUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);

        String user_gson = preferences.getString("user_data","");
        Gson gson = new Gson();
        UserModel userModel = gson.fromJson(user_gson,UserModel.class);
        return userModel;
    }

    public void CreateUpdateSession(Context context,String session)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("session_data",session);
        editor.apply();
    }

    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        String session = preferences.getString("session_data","");
        return session;
    }
}
