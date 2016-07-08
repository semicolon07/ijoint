package com.kanmanus.kmutt.sit.ijoint.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanmanus.kmutt.sit.ijoint.MyApplication;
import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.activity.ApplicationSettingActivity;
import com.kanmanus.kmutt.sit.ijoint.activity.TaskHistoryActivity;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.DefaultMenuRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.ProfileRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.fix.AppMenu;
import com.kanmanus.kmutt.sit.ijoint.fix.Navigator;
import com.kanmanus.kmutt.sit.ijoint.models.enumtype.Gender;
import com.kanmanus.kmutt.sit.ijoint.models.PatientProfileViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;


/**
 * Created by Pongpop Inkeaw on 01/01/2016.
 */
public class ProfileFragment extends Fragment implements DefaultMenuRecyclerViewItem.Listener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public ProfileFragment() {
        super();
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        initInstances(rootView, savedInstanceState);

        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        //Profile
        PatientProfileViewModel profileViewModel = MyApplication.getInstance().getSession();
        ProfileRecyclerViewItem profileRecyclerViewItem = new ProfileRecyclerViewItem();
        profileRecyclerViewItem.setFullName(profileViewModel.getFullName());
        profileRecyclerViewItem.setEmail(profileViewModel.getEmail());
        profileRecyclerViewItem.setGender(profileViewModel.getGenderName());
        profileRecyclerViewItem.setAge(profileViewModel.getAgeText());
        profileRecyclerViewItem.setAvatarResId(profileViewModel.getGender() == Gender.MALE ? R.drawable.male_icon : R.drawable.female_icon);
        //Menu
        DefaultMenuRecyclerViewItem applicationSettingMenuItem = new DefaultMenuRecyclerViewItem(getString(R.string.profile_menu_app_setting),R.drawable.ic_settings_applications, AppMenu.APPLICATION_SETTING);
        DefaultMenuRecyclerViewItem taskHistoryMenuItem = new DefaultMenuRecyclerViewItem(getString(R.string.profile_menu_task_history),R.drawable.ic_history, AppMenu.TASK_HISTORY);
        DefaultMenuRecyclerViewItem signOutMenuItem = new DefaultMenuRecyclerViewItem(getString(R.string.profile_menu_sign_out),R.drawable.ic_power_settings_new, AppMenu.SIGN_OUT);

        applicationSettingMenuItem.setListener(this);

        List<IFlexible> items = new ArrayList<>();
        items.add(profileRecyclerViewItem);
        items.add(applicationSettingMenuItem);
        items.add(taskHistoryMenuItem);
        items.add(signOutMenuItem);
        FlexibleAdapter<IFlexible> adapter = new FlexibleAdapter<>(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
    public void onMenuItemClicked(AppMenu appMenu) {
        switch (appMenu){
            case APPLICATION_SETTING:
                Intent intent = new Intent(getActivity(), ApplicationSettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case TASK_HISTORY:
                Intent taskHistoryIntent = new Intent(getActivity(), TaskHistoryActivity.class);
                getActivity().startActivity(taskHistoryIntent);
                break;
            case SIGN_OUT:
                MyApplication.getInstance().clearSession();
                Navigator.getInstance().navigateToLogin(getActivity());
                getActivity().finish();
                break;
        }
    }
}
