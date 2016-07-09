package com.kanmanus.kmutt.sit.ijoint.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.AdapterMapping;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.ExerciseVideoRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.HeaderRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.datamanager.ExerciseVideoDataManager;
import com.kanmanus.kmutt.sit.ijoint.models.ExerciseVideoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Created by Pongpop Inkeaw on 01/01/2016.
 */
public class ExerciseSampleVideoFragment extends BaseFragment implements ExerciseVideoRecyclerViewItem.Listener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ExerciseVideoDataManager videoDataManager;
    private AdapterMapping adapterMapping;
    private FlexibleAdapter<IFlexible> exerciseVideoAdapter;

    public ExerciseSampleVideoFragment() {
        super();
    }

    public static ExerciseSampleVideoFragment newInstance() {
        ExerciseSampleVideoFragment fragment = new ExerciseSampleVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        videoDataManager = new ExerciseVideoDataManager();
        adapterMapping = new AdapterMapping();
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample_exercise_video, container, false);
        ButterKnife.bind(this, rootView);
        initInstances(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadVideo();
    }

    private void loadVideo() {
        List<IFlexible> videoFlexibleList = new ArrayList<>();
        List<ExerciseVideoModel> videoModels = videoDataManager.getAll();
        HeaderRecyclerViewItem headerItem = new HeaderRecyclerViewItem(getString(R.string.exercise_video_header));

        videoFlexibleList.add(headerItem);
        videoFlexibleList.addAll(adapterMapping.transformExerciseVideo(videoModels,this));
        exerciseVideoAdapter = new FlexibleAdapter<>(videoFlexibleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(exerciseVideoAdapter);
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    @Override
    public void onExerciseItemClicked(ExerciseVideoModel item) {
        navigator.navigateToExerciseVideoDetail(getActivity(),item);
    }
}
