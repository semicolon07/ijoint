package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import com.kanmanus.kmutt.sit.ijoint.models.ExerciseVideoModel;
import com.kanmanus.kmutt.sit.ijoint.models.Task;
import com.kanmanus.kmutt.sit.ijoint.models.TaskHistoryHeaderModel;
import com.kanmanus.kmutt.sit.ijoint.models.TreatmentModel;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by Semicolon07 on 7/7/2016 AD.
 */

public class AdapterMapping {
    public List<TreatmentRecyclerViewItem> transform(List<TreatmentModel> treatmentModels,TreatmentRecyclerViewItem.Listener listener){
        List<TreatmentRecyclerViewItem> treatmentRecyclerViewItems = new ArrayList<>();
        for (TreatmentModel model : treatmentModels){
            TreatmentRecyclerViewItem viewItem = new TreatmentRecyclerViewItem(model);
            viewItem.setListener(listener);
            treatmentRecyclerViewItems.add(viewItem);
        }
        return treatmentRecyclerViewItems;
    }

    public List<TaskRecyclerViewItem> transformTask(List<Task> taskList, TaskRecyclerViewItem.Listener listener){
        List<TaskRecyclerViewItem> taskRecyclerViewItems = new ArrayList<>();
        for (Task task : taskList){
            TaskRecyclerViewItem item = new TaskRecyclerViewItem(task);
            item.setListener(listener);
            taskRecyclerViewItems.add(item);
        }
        return taskRecyclerViewItems;
    }

    public List<TaskRecyclerViewItem> transformSyncedTask(List<Task> taskList, TaskRecyclerViewItem.Listener listener){
        List<TaskRecyclerViewItem> taskRecyclerViewItems = new ArrayList<>();
        for (Task task : taskList){
            task.is_synced = Task.TASK_SYNCED;
            TaskRecyclerViewItem item = new TaskRecyclerViewItem(task);
            item.setListener(listener);
            taskRecyclerViewItems.add(item);
        }
        return taskRecyclerViewItems;
    }
    public List<ExerciseVideoRecyclerViewItem> transformExerciseVideo(List<ExerciseVideoModel> list, ExerciseVideoRecyclerViewItem.Listener listener){
        List<ExerciseVideoRecyclerViewItem> recyclerViewItems = new ArrayList<>();
        for (ExerciseVideoModel model : list){
            ExerciseVideoRecyclerViewItem item = new ExerciseVideoRecyclerViewItem(model);
            item.setListener(listener);
            recyclerViewItems.add(item);
        }
        return recyclerViewItems;
    }


    public List<IFlexible> transformTaskHistory(List<TaskHistoryHeaderModel> list,TaskHistoryRecyclerViewItem.Listener listener) {
        List<IFlexible> recyclerViewItems = new ArrayList<>();
        for (TaskHistoryHeaderModel model : list){
            TaskHistoryRecyclerViewItem item = new TaskHistoryRecyclerViewItem(model);
            item.setListener(listener);
            recyclerViewItems.add(item);
        }
        return recyclerViewItems;
    }
}
