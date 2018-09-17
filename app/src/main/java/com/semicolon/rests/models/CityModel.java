package com.semicolon.rests.models;

import java.io.Serializable;

public class CityModel implements Serializable {

    private String city_id;
    private String city_title;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_title() {
        return city_title;
    }

    public void setCity_title(String city_title) {
        this.city_title = city_title;
    }
}
