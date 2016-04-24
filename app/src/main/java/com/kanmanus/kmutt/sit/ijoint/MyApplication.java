package com.kanmanus.kmutt.sit.ijoint;

import android.app.Application;

/**
 * Created by Semicolon07 on 4/24/2016 AD.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }
}
