package com.semicolon.rests.models;

import java.io.Serializable;

public class BanksModel implements Serializable {

    private String account_id;
    private String account_name;
    private String account_IBAN;
    private String account_bank_name;
    private String account_number;


    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_IBAN() {
        return account_IBAN;
    }

    public void setAccount_IBAN(String account_IBAN) {
        this.account_IBAN = account_IBAN;
    }

    public String getAccount_bank_name() {
        return account_bank_name;
    }

    public void setAccount_bank_name(String account_bank_name) {
        this.account_bank_name = account_bank_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }
}
