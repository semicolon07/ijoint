package com.kanmanus.kmutt.sit.ijoint.models;

import com.kanmanus.kmutt.sit.ijoint.models.enumtype.Gender;

import lombok.Data;

/**
 * Created by Semicolon07 on 6/28/2016 AD.
 */
@Data
public class PatientProfileViewModel{
    private String pId;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String dateOfBirth;
    private String age;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id = "+pId);
        sb.append(", firstname = "+firstName);
        sb.append(", lastname = "+lastName);
        sb.append(", email = "+email);
        sb.append(", dateofbirth = "+dateOfBirth);
        sb.append(", gender = "+gender);
        sb.append(", age = "+age);
        return sb.toString();
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }
    public String getAgeText(){
        return age+" Years old";
    }
    public String getGenderName(){
        return gender.getLabel();
    }
}
