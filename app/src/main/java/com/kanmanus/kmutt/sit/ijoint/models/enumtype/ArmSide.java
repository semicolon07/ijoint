package com.kanmanus.kmutt.sit.ijoint.models.enumtype;

/**
 * Created by Semicolon07 on 7/6/2016 AD.
 */

public enum ArmSide {
    LEFT("l","Left"),
    RIGHT("r","Right");

    private final String code;
    private final String label;
    ArmSide(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public static ArmSide transform(String code){
        switch (code){
            case "l":
                return ArmSide.LEFT;
            case "r":
                return ArmSide.RIGHT;
            default:
                return ArmSide.LEFT;
        }
    }
}
