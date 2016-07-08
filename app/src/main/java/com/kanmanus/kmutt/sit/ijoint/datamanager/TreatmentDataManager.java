package com.kanmanus.kmutt.sit.ijoint.datamanager;

import android.content.Context;
import android.util.Log;

import com.kanmanus.kmutt.sit.ijoint.Contextor;
import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.AdapterMapping;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.HeaderRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.adapter.model.TaskRecyclerViewItem;
import com.kanmanus.kmutt.sit.ijoint.db.ResultItemDataSource;
import com.kanmanus.kmutt.sit.ijoint.db.TaskDataSource;
import com.kanmanus.kmutt.sit.ijoint.models.ResultItem;
import com.kanmanus.kmutt.sit.ijoint.models.Task;
import com.kanmanus.kmutt.sit.ijoint.models.response.AllTreatmentResponse;
import com.kanmanus.kmutt.sit.ijoint.net.DefaultSubscriber;
import com.kanmanus.kmutt.sit.ijoint.net.HttpManager;
import com.kanmanus.kmutt.sit.ijoint.utils.DateTimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Semicolon07 on 7/5/2016 AD.
 */

public class TreatmentDataManager extends BaseDataManager{
    public Subscription getAllTreatment(String patientId, DefaultSubscriber subscriber){
        Observable<AllTreatmentResponse> call = HttpManager.getInstance().getService().getAllTreatment(patientId, DateTimeUtils.getDate(DateTimeUtils.yyyy_MM_dd));
        return executeObservable(call,subscriber);
    }

    public Subscription syncTask(String pId, String treatmentNo, String treatmentStatus, DefaultSubscriber subscriber, TaskRecyclerViewItem.Listener listener){

        Observable<List<IFlexible>> syncDataObservable = Observable.create(new Observable.OnSubscribe<List<IFlexible>>() {
            TaskDataSource taskDataSource = new TaskDataSource(Contextor.getInstance().getContext());
            ResultItemDataSource resultItemDataSource = new ResultItemDataSource(Contextor.getInstance().getContext());
            String currDate = DateTimeUtils.getDate(DateTimeUtils.yyyy_MM_dd);

            @Override
            public void call(Subscriber<? super List<IFlexible>> subscriber) {
                String taskType = treatmentStatus.equals("1")?"1":"2";
                taskDataSource.open();
                resultItemDataSource.open();
                boolean isInternetConnect = isThereInternetConnection();
                if(isInternetConnect){
                    uploadTasks();
                    downloadTasks();
                }

                List<IFlexible> taskFlexibleList = new ArrayList<>();
                Task.TaskType type = Task.TaskType.transform(taskType);
                Context context = Contextor.getInstance().getContext();
                AdapterMapping adapterMapping = new AdapterMapping();

                if(type == Task.TaskType.Initial){
                    ArrayList<Task> allTasks = (ArrayList<Task>) taskDataSource.getAll(pId,treatmentNo,taskType);
                    HeaderRecyclerViewItem newTreatmentHeaderItem = new HeaderRecyclerViewItem(context.getString(R.string.task_new_treatment_header));
                    taskFlexibleList.add(newTreatmentHeaderItem);
                    taskFlexibleList.addAll(adapterMapping.transformTask(allTasks,listener));
                }
                if(type == Task.TaskType.Treatment){
                    ArrayList<Task> todayTasks = (ArrayList<Task>) taskDataSource.getAll(pId,treatmentNo,taskType,currDate,"=");
                    ArrayList<Task> olderTasks = (ArrayList<Task>) taskDataSource.getAll(pId,treatmentNo,taskType,currDate,"<");
                    HeaderRecyclerViewItem todayTaskHeaderItem = new HeaderRecyclerViewItem(context.getString(R.string.task_during_today_treatment_header));
                    HeaderRecyclerViewItem olderTaskHeaderItem = new HeaderRecyclerViewItem(context.getString(R.string.task_during_older_treatment_header));

                    if(todayTasks.size() > 0){
                        taskFlexibleList.add(todayTaskHeaderItem);
                        taskFlexibleList.addAll(adapterMapping.transformTask(todayTasks,listener));
                    }
                    if(olderTasks.size() > 0){
                        taskFlexibleList.add(olderTaskHeaderItem);
                        taskFlexibleList.addAll(adapterMapping.transformTask(olderTasks,listener));
                    }
                }

                //subscriber.onNext(allTasks);
                subscriber.onNext(taskFlexibleList);
                subscriber.onCompleted();
                taskDataSource.close();
                resultItemDataSource.close();

            }

            private void downloadTasks() {
                try {

                    List<Task> tasks = HttpManager.getInstance().getService().getTasksByTreatment(treatmentNo,treatmentStatus,currDate).execute().body();
                    Log.d("DataManager","Treatment No = "+treatmentNo+",Treatment Status = "+treatmentStatus+", Date = "+currDate+", Task size = "+tasks.size());
                    if(tasks!=null){
                        for(Task task : tasks){
                            if (taskDataSource.get(task.tid) == null){
                                taskDataSource.create(task.tid, task.pid, task.date, task.side, task.target_angle, task.number_of_round, task.is_abf, "n", "0000-00-00",task.exercise_type,task.treatmentNo,task.taskType);
                            }
                            else{
                                taskDataSource.edit(task.tid, task.pid, task.date, task.side, task.target_angle, task.number_of_round, task.is_abf,task.exercise_type,task.taskType);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void uploadTasks() {
                List<Task> finishedTasks = taskDataSource.getFinishedTasks(treatmentNo);
                Iterator<Task> iter = finishedTasks.iterator();
                int i = 1;
                String tasksId = "";
                String performDateTimes = "";
                while (iter.hasNext()){
                    Task t = iter.next();
                    String tid = t.tid;
                    String performDateTime =  t.perform_datetime ;
                    List<ResultItem> resultItemList = resultItemDataSource.getByTid(tid);
                    JSONArray resultJSONArray = new JSONArray();

                    Iterator<ResultItem> iterItem = resultItemList.iterator();
                    while (iterItem.hasNext()){
                        ResultItem resultItem = iterItem.next();
                        resultJSONArray.put(resultItem.getJSONObject());
                    }

                    JSONObject json = new JSONObject();

                    try {
                        json.put("perform_datetime", performDateTime);
                        json.put("score", t.score);
                        json.put("result", resultJSONArray);
                        HttpManager.getInstance().getService().uploadResultItems(json.toString()).execute();
                        taskDataSource.updateIsSynced(tid, "y");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return executeObservable(syncDataObservable,subscriber);
    }
}
