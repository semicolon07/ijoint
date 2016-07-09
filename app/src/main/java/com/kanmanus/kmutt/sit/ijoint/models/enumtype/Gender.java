package com.kanmanus.kmutt.sit.ijoint.models.enumtype;

/**
 * Created by Semicolon07 on 6/28/2016 AD.
 */

public enum Gender {
    FEMALE("f","Female"),
    MALE("m","Male");

    private final String code;
    private final String label;
    Gender(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public static Gender transform(String code){
        switch (code){
            case "m":
                return Gender.MALE;
            case "f":
                return Gender.FEMALE;
            default:
                return Gender.FEMALE;
        }
    }
}
