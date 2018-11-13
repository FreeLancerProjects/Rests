package com.semicolon.rests.models;

import java.io.Serializable;

public class Notification_Model implements Serializable {
    private String id_place;
    private String place_name;
    private String place_main_photo;
    private String city_title;
    private String approved;


    public String getId_place() {
        return id_place;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_main_photo() {
        return place_main_photo;
    }

    public String getCity_title() {
        return city_title;
    }

    public String getApproved() {
        return approved;
    }
}
