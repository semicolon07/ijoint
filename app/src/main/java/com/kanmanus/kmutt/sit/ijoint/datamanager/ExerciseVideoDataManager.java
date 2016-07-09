package com.kanmanus.kmutt.sit.ijoint.datamanager;

import com.kanmanus.kmutt.sit.ijoint.models.ExerciseVideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semicolon07 on 7/8/2016 AD.
 */

public class ExerciseVideoDataManager extends BaseDataManager {
    public List<ExerciseVideoModel> getAll(){
        List<ExerciseVideoModel> exerciseVideoModels = new ArrayList<>();
        ExerciseVideoModel videoModel1 = new ExerciseVideoModel();
        ExerciseVideoModel videoModel2 = new ExerciseVideoModel();

        videoModel1.setName("Video 1");
        videoModel1.setDescription("Video 1 Description");
        videoModel1.setVideoFileName("example_video_01.mp4");

        videoModel2.setName("Video 2");
        videoModel2.setDescription("Video 2 Description");
        videoModel2.setVideoFileName("example_video_01.mp4");

        exerciseVideoModels.add(videoModel1);
        exerciseVideoModels.add(videoModel2);
        return exerciseVideoModels;
    }
}
