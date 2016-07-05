package com.kanmanus.kmutt.sit.ijoint;

import android.app.Application;

import com.kanmanus.kmutt.sit.ijoint.datamanager.SessionManager;
import com.kanmanus.kmutt.sit.ijoint.models.PatientProfileViewModel;

/**
 * Created by Semicolon07 on 4/24/2016 AD.
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private PatientProfileViewModel profileViewModel;

    public MyApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
        profileViewModel = fetchSession();
    }

    private PatientProfileViewModel fetchSession() {
        SessionManager profilePreference = new SessionManager(this);
        return profilePreference.getSessionData();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public PatientProfileViewModel getSession() {
        return profileViewModel;
    }
    public void clearSession(){
        SessionManager profilePreference = new SessionManager(this);
        profilePreference.clear();
        profileViewModel = fetchSession();
    }

    public void setSession(PatientProfileViewModel session) {
        SessionManager profilePreference = new SessionManager(this);
        profilePreference.setSessionData(session);
        profileViewModel = fetchSession();
    }
}
