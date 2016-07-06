package com.kanmanus.kmutt.sit.ijoint.net.api;

import com.kanmanus.kmutt.sit.ijoint.fix.URL;
import com.kanmanus.kmutt.sit.ijoint.models.Task;
import com.kanmanus.kmutt.sit.ijoint.models.response.AllTreatmentResponse;
import com.kanmanus.kmutt.sit.ijoint.models.response.SignInResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Semicolon07 on 4/24/2016 AD.
 */
public interface IjointService {
    @FormUrlEncoded
    @POST(URL.SIGN_IN)
    Observable<SignInResponse> signIn(@Field("username") String userName, @Field("password") String password);

    @FormUrlEncoded
    @POST(URL.GET_ALL_TREATMENT)
    Observable<AllTreatmentResponse> getAllTreatment(@Field("pid") String pId);

    @FormUrlEncoded
    @POST(URL.GET_TASKS_BY_TREATMENT)
    Observable<List<Task>> getTasksByTreatment(@Field("treatment_no") String pId);

    @FormUrlEncoded
    @POST(URL.GET_TASK)
    Call<List<Task>> getTasks(@Field("pid") String pId);

    @FormUrlEncoded
    @POST(URL.UPDATE_STATUS)
    Call<Void> updateStatus(@Field("tid_list") String tIdList);

    @FormUrlEncoded
    @POST(URL.UPLOAD_RESULT_ITEM)
    Call<String> uploadResultItems(@Field("json") String json);

    @FormUrlEncoded
    @POST(URL.UPDATE_TASK)
    Call<Void> updateTask(@Field("tid_list") String tIdList,@Field("perform_datetimes") String performDateTime);
}
