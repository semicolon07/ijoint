package com.kanmanus.kmutt.sit.ijoint.models;

import com.kanmanus.kmutt.sit.ijoint.models.enumtype.ArmSide;
import com.kanmanus.kmutt.sit.ijoint.models.enumtype.ServerDbStatus;

import java.util.Date;

import lombok.Data;

/**
 * Created by Semicolon07 on 7/6/2016 AD.
 */
@Data
public class TreatmentModel {
    private Date createDate;

    private String subject;

    private String detail;

    private ServerDbStatus status;

    private ArmSide armSide;

    private int treatmentNo;
}
