package com.kanmanus.kmutt.sit.ijoint.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by Semicolon07 on 7/6/2016 AD.
 */
@Data
public class AllTreatmentResponse {
    @SerializedName("new")
    private List<TreatmentResponse> newTreatmentResponses;

    @SerializedName("during")
    private List<TreatmentResponse> duringTreatmentResponses;
}
