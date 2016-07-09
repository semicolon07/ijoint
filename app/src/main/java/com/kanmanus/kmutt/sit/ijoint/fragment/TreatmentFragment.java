package com.kanmanus.kmutt.sit.ijoint.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.kanmanus.kmutt.sit.ijoint.MyApplication;
import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.AdapterMapping;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.HeaderRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.TreatmentRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.datamanager.TreatmentDataManager;
import com.kanmanus.kmutt.sit.ijoint.models.PatientProfileViewModel;
import com.kanmanus.kmutt.sit.ijoint.models.TreatmentModel;
import com.kanmanus.kmutt.sit.ijoint.models.mapping.ServerResponseMapping;
import com.kanmanus.kmutt.sit.ijoint.models.response.AllTreatmentResponse;
import com.kanmanus.kmutt.sit.ijoint.models.response.TreatmentResponse;
import com.kanmanus.kmutt.sit.ijoint.net.DefaultSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import rx.Subscription;


/**
 * Created by Pongpop Inkeaw on 01/01/2016.
 */
public class TreatmentFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, TreatmentRecyclerViewItem.Listener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stub)
    ViewStub stub;

    private List<IFlexible> treatmentList;
    private FlexibleAdapter<IFlexible> adapter;
    private PatientProfileViewModel profileViewModel;
    private TreatmentDataManager dataManager;
    private Subscription loadAllTreatmentSubscription;

    public TreatmentFragment() {
        super();
    }

    public static TreatmentFragment newInstance() {
        TreatmentFragment fragment = new TreatmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        profileViewModel = MyApplication.getInstance().getSession();
        treatmentList = new ArrayList<>();
        adapter = new FlexibleAdapter<>(treatmentList);
        dataManager = new TreatmentDataManager();

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_treatment, container, false);
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
        //stub.inflate();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        loadAllTreatment();
    }

    private void loadAllTreatment() {
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            loadAllTreatmentSubscription = dataManager.getAllTreatment(profileViewModel.getPId(), new LoadAllTreatmentSubscriber());
        });

    }

    @Override
    public void onTreatmentItemClicked(TreatmentModel treatmentModel) {
        navigator.navigateToTasks(getActivity(), treatmentModel);
    }

    class LoadAllTreatmentSubscriber extends DefaultSubscriber<AllTreatmentResponse> {
        @Override
        public void onCompleted() {
            swipeRefreshLayout.setRefreshing(false);
            removeRetryConnectView();
        }

        @Override
        public void onError(Throwable e) {
            //showError(e);
            showRetryConnect(e, onRetryConnectClickListener);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(AllTreatmentResponse allTreatmentResponse) {
            renderTreatmentList(allTreatmentResponse);
        }
    }

    private final View.OnClickListener onRetryConnectClickListener = v -> loadAllTreatment();

    private void renderTreatmentList(AllTreatmentResponse allTreatmentResponse) {
        treatmentList.clear();
        List<TreatmentResponse> newTreatments = allTreatmentResponse.getNewTreatmentResponses();
        List<TreatmentResponse> duringTreatments = allTreatmentResponse.getDuringTreatmentResponses();
        if (newTreatments.size() > 0) {
            HeaderRecyclerViewItem newTreatmentHeaderItem = new HeaderRecyclerViewItem(getString(R.string.treatment_new_header));
            treatmentList.add(newTreatmentHeaderItem);
            treatmentList.addAll(convert(newTreatments));
        }
        if (duringTreatments.size() > 0) {
            HeaderRecyclerViewItem duringTreatmentHeaderItem = new HeaderRecyclerViewItem(getString(R.string.treatment_during_header));
            treatmentList.add(duringTreatmentHeaderItem);
            treatmentList.addAll(convert(duringTreatments));
        }
        adapter.notifyDataSetChanged();
    }

    public List<TreatmentRecyclerViewItem> convert(List<TreatmentResponse> models) {
        List<TreatmentModel> treatmentModels = new ServerResponseMapping().transformTreatment(models);
        List<TreatmentRecyclerViewItem> recyclerViewItems = new AdapterMapping().transform(treatmentModels, this);
        return recyclerViewItems;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onRefresh() {
        loadAllTreatment();
    }
}
