package com.kanmanus.kmutt.sit.ijoint.models.response;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by Semicolon07 on 4/24/2016 AD.
 */
@Data
public class SignInResponse {
    private boolean status;
    private String pid;

    @SerializedName("pFirstName")
    private String firstName;

    @SerializedName("pLastName")
    private String lastName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("email")
    private String email;

    @SerializedName("dob")
    private String dateOfBirth;

    @SerializedName("age")
    private String age;

}
