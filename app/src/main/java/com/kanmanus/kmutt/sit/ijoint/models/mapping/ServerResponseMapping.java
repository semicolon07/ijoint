package com.kanmanus.kmutt.sit.ijoint.models.mapping;

import com.kanmanus.kmutt.sit.ijoint.models.PatientProfileViewModel;
import com.kanmanus.kmutt.sit.ijoint.models.TreatmentModel;
import com.kanmanus.kmutt.sit.ijoint.models.enumtype.ArmSide;
import com.kanmanus.kmutt.sit.ijoint.models.enumtype.Gender;
import com.kanmanus.kmutt.sit.ijoint.models.enumtype.ServerDbStatus;
import com.kanmanus.kmutt.sit.ijoint.models.response.SignInResponse;
import com.kanmanus.kmutt.sit.ijoint.models.response.TreatmentResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class ServerResponseMapping {
    public PatientProfileViewModel transformSignIn(SignInResponse signInResponse){
        PatientProfileViewModel viewModel = new PatientProfileViewModel();
        viewModel.setPId(signInResponse.getPid());
        viewModel.setFirstName(signInResponse.getFirstName());
        viewModel.setLastName(signInResponse.getLastName());
        viewModel.setGender(Gender.transform(signInResponse.getGender()));
        viewModel.setEmail(signInResponse.getEmail());
        viewModel.setDateOfBirth(signInResponse.getDateOfBirth());
        viewModel.setAge(signInResponse.getAge());
        return viewModel;
    }

    public TreatmentModel transformTreatment(TreatmentResponse response){
        TreatmentModel model = new TreatmentModel();
        model.setTreatmentNo(response.getTreatmentNo());
        model.setSubject(response.getSubject());
        model.setDetail(response.getDetail());
        model.setCreateDate(response.getCreateDate());
        model.setStatus(ServerDbStatus.transform(response.getStatus()));
        model.setArmSide(ArmSide.transform(response.getArmSide()));
        return model;
    }

    public List<TreatmentModel> transformTreatment(List<TreatmentResponse> list){
        List<TreatmentModel> treatmentModels = new ArrayList<>();
        for (TreatmentResponse response : list){
            treatmentModels.add(transformTreatment(response));
        }
        return treatmentModels;
    }
}
