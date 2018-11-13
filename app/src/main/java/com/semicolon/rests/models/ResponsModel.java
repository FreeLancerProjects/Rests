package com.semicolon.rests.models;

import java.io.Serializable;

public class ResponsModel implements Serializable {
    private int success_contact;
    private int success_logout;
    private int success_location;
    private int success_token_id;
    private int success_reservation;
    private int success_update_reservation;
    private int success_delete_reservation;
    private int success_read;

    public int getSuccess_logout() {
        return success_logout;
    }

    public int getSuccess_contact() {
        return success_contact;
    }

    public int getSuccess_location() {
        return success_location;
    }

    public int getSuccess_token_id() {
        return success_token_id;
    }

    public int getSuccess_reservation() {
        return success_reservation;
    }

    public int getSuccess_update_reservation() {
        return success_update_reservation;
    }

    public int getSuccess_delete_reservation() {
        return success_delete_reservation;
    }

    public int getSuccess_read() {
        return success_read;
    }
}
