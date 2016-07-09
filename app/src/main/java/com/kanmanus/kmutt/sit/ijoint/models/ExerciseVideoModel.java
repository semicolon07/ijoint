package com.kanmanus.kmutt.sit.ijoint.models;

import android.net.Uri;

import lombok.Data;

/**
 * Created by Semicolon07 on 7/8/2016 AD.
 */
@Data
public class ExerciseVideoModel {
    private String name;
    private String description;
    private String videoFileName;
    private Uri videoUri;
}
