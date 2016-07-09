package com.kanmanus.kmutt.sit.ijoint.models;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import lombok.Data;

/**
 * Created by niceinkeaw on 4/12/2558.
 */
@Data
public class TabModel {
    private String title;
    private int iconResId;
    private Class<? extends Fragment> fragment;
    private Bundle bundle;

    public TabModel(){
        bundle = new Bundle();
    }
}
