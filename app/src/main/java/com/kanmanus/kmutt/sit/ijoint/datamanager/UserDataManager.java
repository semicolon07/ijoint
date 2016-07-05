package com.kanmanus.kmutt.sit.ijoint.datamanager;

import com.kanmanus.kmutt.sit.ijoint.models.SignInResponse;
import com.kanmanus.kmutt.sit.ijoint.net.DefaultSubscriber;
import com.kanmanus.kmutt.sit.ijoint.net.HttpManager;

import rx.Observable;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class UserDataManager extends BaseDataManager{

    public void login(String userName, String password, DefaultSubscriber subscriber){
        Observable<SignInResponse> call = HttpManager.getInstance().getService().signIn(userName,password);
        executeObservable(call,subscriber);
    }


}
