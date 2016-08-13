package com.kanmanus.kmutt.sit.ijoint.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;

/**
 * Created by Semicolon07 on 7/6/2016 AD.
 */
@Data
public class TreatmentResponse {
    @SerializedName("createDate")
    private Date createDate;

    @SerializedName("subject")
    private String subject;

    @SerializedName("detail")
    private String detail;

    @SerializedName("status")
    private String status;

    @SerializedName("arm_side")
    private String armSide;

    @SerializedName("treatmentNo")
    private int treatmentNo;


}
