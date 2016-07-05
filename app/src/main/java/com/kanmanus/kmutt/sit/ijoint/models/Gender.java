package com.kanmanus.kmutt.sit.ijoint.models;

/**
 * Created by Semicolon07 on 6/28/2016 AD.
 */

public enum Gender {
    FEMALE("f","Female"),
    MAILE("m","Male");

    private final String code;
    private final String label;
    Gender(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
