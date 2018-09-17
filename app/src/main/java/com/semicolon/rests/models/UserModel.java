package com.semicolon.rests.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String user_name;
    private String user_pass;
    private String user_type;
    private String user_full_name;
    private String user_phone;
    private String user_email;
    private String user_photo;
    private String user_token_id;
    private String user_google_lat;
    private String user_google_long;
    private String user_city;
    private String user_neighborhood;
    private String city_title;
    private int success_signup;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public String getUser_city() {
        return user_city;
    }

    public String getUser_neighborhood() {
        return user_neighborhood;
    }

    public String getCity_title() {
        return city_title;
    }

    public int getSuccess_signup() {
        return success_signup;
    }

}
