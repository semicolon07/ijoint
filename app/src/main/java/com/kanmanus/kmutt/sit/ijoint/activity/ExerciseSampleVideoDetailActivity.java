package com.kanmanus.kmutt.sit.ijoint.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.google.gson.Gson;
import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.models.ExerciseVideoModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseSampleVideoDetailActivity extends BaseActivity implements EasyVideoCallback {

    @BindView(R.id.name_textView)
    TextView nameTextView;
    @BindView(R.id.desc_textView)
    TextView descTextView;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.videoPlayer)
    EasyVideoPlayer videoPlayer;
    private ExerciseVideoModel exerciseVideoModel;

    public static Intent callingIntent(Context context, ExerciseVideoModel item) {
        Intent intent = new Intent(context, ExerciseSampleVideoDetailActivity.class);
        intent.putExtra(INTENT_PARAM_MODEL, new Gson().toJson(item));
        return intent;
    }

    public static final String INTENT_PARAM_MODEL = "INTENT_PARAM_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_sample_video_detail);
        ButterKnife.bind(this);
        setupUpButton();

        String json = getIntent().getStringExtra(INTENT_PARAM_MODEL);
        exerciseVideoModel = new Gson().fromJson(json, ExerciseVideoModel.class);
        exerciseVideoModel.setVideoUri(getResourceUriName(exerciseVideoModel.getVideoFileName()));
        setTitle(exerciseVideoModel.getName());
        nameTextView.setText(exerciseVideoModel.getName());
        descTextView.setText(exerciseVideoModel.getDescription());

        /*MediaController vidControl = new MediaController(this);
        videoView.setVideoURI(exerciseVideoModel.getVideoUri());
        vidControl.setAnchorView(videoView);

        videoView.setMediaController(vidControl);*/
        videoPlayer.setCallback(this);
        videoPlayer.setSource(exerciseVideoModel.getVideoUri());
    }

    public Uri getResourceUriName(String fileName) {
        int rawFile = R.raw.example_video_01;
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + rawFile);
        return uri;
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        player.pause();
        navigator.navigateToFullScreen(this,exerciseVideoModel.getVideoUri(),player.getCurrentPosition());
    }
}
