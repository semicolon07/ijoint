package com.kanmanus.kmutt.sit.ijoint.models;

import lombok.Data;

/**
 * Created by Semicolon07 on 6/28/2016 AD.
 */
@Data
public class PatientProfileViewModel{
    private String pId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Gender gender;
    private String dateOfBirth;
}
