package com.kanmanus.kmutt.sit.ijoint.models.enumtype;

/**
 * Created by Semicolon07 on 7/6/2016 AD.
 */

public enum ServerDbStatus {
    NONE("1","None"),
    PROCESSING("2","Processing"),
    COMPLETE("3","Complete"),
    DELETE("4","Delete"),
    CREATED("c","Created"),
    DONE("d","Done"),
    EDITED("e","Edited"),
    SYNCED("s","Synced"),
    DELETED("x","Deleted");

    private final String code;
    private final String label;
    ServerDbStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public static ServerDbStatus transform(String code){
        switch (code){
            case "1":
                return ServerDbStatus.NONE;
            case "2":
                return ServerDbStatus.PROCESSING;
            case "3":
                return ServerDbStatus.COMPLETE;
            case "4":
                return ServerDbStatus.DELETE;
            case "c":
                return ServerDbStatus.CREATED;
            case "d":
                return ServerDbStatus.DONE;
            case "e":
                return ServerDbStatus.EDITED;
            case "s":
                return ServerDbStatus.SYNCED;
            case "x":
                return ServerDbStatus.DELETED;
            default:
                return ServerDbStatus.NONE;
        }
    }
}
