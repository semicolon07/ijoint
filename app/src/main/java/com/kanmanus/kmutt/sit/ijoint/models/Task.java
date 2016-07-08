package com.kanmanus.kmutt.sit.ijoint.models;

import com.google.gson.annotations.SerializedName;

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
    public String exercise_type;
    public String score;

    @SerializedName("treatmentNo")
    public String treatmentNo;

    @SerializedName("task_type")
    public String taskType;

    public Task(){

    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("tid = "+tid);
        sb.append(", pid = "+pid);
        sb.append(", date = "+date);
        sb.append(", side = "+side);
        sb.append(", target_angle = "+target_angle);
        sb.append(", number_of_round = "+number_of_round);

        return sb.toString();
    }

    public Task(String tid, String pid, String date, String side, String target_angle, String number_of_round, String is_abf, String is_synced, String treatmentNo, String perform_datetime) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.side = side;
        this.target_angle = target_angle;
        this.number_of_round = number_of_round;
        this.is_abf = is_abf;
        this.is_synced = is_synced;
        this.treatmentNo = treatmentNo;
        this.perform_datetime = perform_datetime;
    }

    public TaskType getTaskTypeEnum(){
        return TaskType.transform(taskType);
    }


    public static final String FLEXION = "f";
    public static final String HORIZONTAL = "h";
    public static final String EXTENSION = "e";

    public static final String TASK_COMPLETE = "f";
    public static final String TASK_READY = "n";
    public static final String TASK_SYNCED = "y";

    public enum TaskType{
        Initial("1"),
        Treatment("2"),
        Manual("3"),
        Auto("4");

        private final String code;

        TaskType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static TaskType transform(String code){
            switch (code){
                case "1":
                    return TaskType.Initial;
                case "2":
                    return TaskType.Treatment;
                case "3":
                    return TaskType.Manual;
                case "4":
                    return TaskType.Auto;
            }
            return TaskType.Initial;
        }
    }
}
