package com.kanmanus.kmutt.sit.ijoint.datamanager;

import com.kanmanus.kmutt.sit.ijoint.models.response.AllTreatmentResponse;
import com.kanmanus.kmutt.sit.ijoint.net.DefaultSubscriber;
import com.kanmanus.kmutt.sit.ijoint.net.HttpManager;

import rx.Observable;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class TreatmentDataManager extends BaseDataManager{
    public void getAllTreatment(String patientId,DefaultSubscriber subscriber){
        Observable<AllTreatmentResponse> call = HttpManager.getInstance().getService().getAllTreatment(patientId);
        executeObservable(call,subscriber);
    }
}
