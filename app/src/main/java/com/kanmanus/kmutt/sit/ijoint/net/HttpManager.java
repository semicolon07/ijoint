package com.kanmanus.kmutt.sit.ijoint.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kanmanus.kmutt.sit.ijoint.Contextor;
import com.kanmanus.kmutt.sit.ijoint.fix.URL;
import com.kanmanus.kmutt.sit.ijoint.net.api.IjointService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Semicolon07 on 4/24/2016 AD.
 */
public class HttpManager {
    private static HttpManager instance;
    private final IjointService service;

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    private Context mContext;

    private HttpManager() {
        mContext = Contextor.getInstance().getContext();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(IjointService.class);
    }
    public IjointService getService(){
        return service;
    }
}
