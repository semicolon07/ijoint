package com.kanmanus.kmutt.sit.ijoint.models.mapping;

import com.kanmanus.kmutt.sit.ijoint.models.PatientProfileViewModel;
import com.kanmanus.kmutt.sit.ijoint.models.SignInResponse;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class SignInResponseMapping {
    public PatientProfileViewModel transform(SignInResponse signInResponse){
        PatientProfileViewModel viewModel = new PatientProfileViewModel();
        viewModel.setPId(signInResponse.getPid());
        viewModel.setFirstName(signInResponse.getFirstName());
        viewModel.setLastName(signInResponse.getLastName());
        return viewModel;
    }
}
