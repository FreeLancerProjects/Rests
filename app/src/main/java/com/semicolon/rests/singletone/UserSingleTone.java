package com.semicolon.rests.singletone;

import com.semicolon.rests.models.UserModel;

public class UserSingleTone {
    private static UserSingleTone instance=null;
    private UserModel userModel=null;
    private UserSingleTone() {
    }

    public static synchronized UserSingleTone getInstance()
    {
        if (instance==null)
        {
            instance = new UserSingleTone();
        }
        return  instance;
    }

    public void setUserModel(UserModel userModel)
    {
        this.userModel = userModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void clearUserData()
    {
        this.userModel=null;
    }
}
