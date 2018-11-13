package com.semicolon.rests.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlacesModel implements Serializable{
    private String id_place;
    private String place_name;
    private String place_main_photo;
    private String place_category;
    private String place_cost;
    private String place_size;
    private String place_details;
    private String place_address;
    private String place_google_lat;
    private String place_google_long;
    private String place_city;
    private String place_phone;
    private String place_mobile;
    private String place_email;
    private String place_evaluation_count;
    private String place_clients_count;
    private String city_title;
    private String place_id_fk;
    private String approved;
    private String id_reservation;
    private String reservation_date;
    private String reservation_cost;
    private String transformated;
    private String can_edit;
    private String can_cancel;
    private String can_transformat;

    @SerializedName("gallary")
    private List<Gallery> galleryList;

    public String getId_place() {
        return id_place;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_main_photo() {
        return place_main_photo;
    }

    public String getPlace_category() {
        return place_category;
    }

    public String getPlace_cost() {
        return place_cost;
    }

    public String getPlace_size() {
        return place_size;
    }

    public String getPlace_details() {
        return place_details;
    }

    public String getPlace_address() {
        return place_address;
    }

    public String getPlace_google_lat() {
        return place_google_lat;
    }

    public String getPlace_google_long() {
        return place_google_long;
    }

    public String getPlace_city() {
        return place_city;
    }

    public String getPlace_phone() {
        return place_phone;
    }

    public String getPlace_mobile() {
        return place_mobile;
    }

    public String getPlace_email() {
        return place_email;
    }

    public String getPlace_evaluation_count() {
        return place_evaluation_count;
    }

    public String getPlace_clients_count() {
        return place_clients_count;
    }

    public String getCity_title() {
        return city_title;
    }

    public String getPlace_id_fk() {
        return place_id_fk;
    }

    public String getApproved() {
        return approved;
    }

    public String getReservation_date() {
        return reservation_date;
    }

    public String getReservation_cost() {
        return reservation_cost;
    }

    public String getTransformated() {
        return transformated;
    }

    public String getCan_edit() {
        return can_edit;
    }

    public String getCan_cancel() {
        return can_cancel;
    }

    public String getCan_transformat() {
        return can_transformat;
    }

    public String getId_reservation() {
        return id_reservation;
    }

    public List<Gallery> getGalleryList() {
        return galleryList;
    }

    public class Gallery implements Serializable
    {
        private String id_photo;
        private String photo_name;

        public Gallery(String id_photo, String photo_name) {
            this.id_photo = id_photo;
            this.photo_name = photo_name;
        }

        public String getId_photo() {
            return id_photo;
        }

        public String getPhoto_name() {
            return photo_name;
        }
    }
}
