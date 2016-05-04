package com.kanmanus.kmutt.sit.ijoint.models;

/**
 * Created by kanmanus on 1/14/15 AD.
 */
public class Task {
    public String tid;
    public String pid;
    public String date;
    public String side;
    public String target_angle;
    public String number_of_round;
    public String is_abf;
    public String is_synced;
    public String perform_datetime;
    public String status;
    public String exercise_type;

    public Task(String tid, String pid, String date, String side, String target_angle, String number_of_round, String is_abf, String is_synced) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.side = side;
        this.target_angle = target_angle;
        this.number_of_round = number_of_round;
        this.is_abf = is_abf;
        this.is_synced = is_synced;
    }

    public static final String FLEXION = "f";
    public static final String HORIZONTAL = "h";
    public static final String EXTENSION = "e";

}
