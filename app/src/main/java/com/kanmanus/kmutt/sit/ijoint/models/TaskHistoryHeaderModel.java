package com.kanmanus.kmutt.sit.ijoint.models;

import com.google.gson.annotations.SerializedName;
import com.kanmanus.kmutt.sit.ijoint.utils.DateTimeUtils;

import java.util.Date;

import lombok.Data;

/**
 * Created by Semicolon07 on 7/8/2016 AD.
 */
@Data
public class TaskHistoryHeaderModel {
    @SerializedName("date")
    private Date date;
    @SerializedName("count_done")
    private String amountTask;

    public String getDateLabel(){
        return DateTimeUtils.toFormat(date,"dd/MM/yyyy");
    }
}
