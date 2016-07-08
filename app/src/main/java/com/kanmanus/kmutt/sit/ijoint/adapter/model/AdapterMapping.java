package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import com.kanmanus.kmutt.sit.ijoint.models.Task;
import com.kanmanus.kmutt.sit.ijoint.models.TreatmentModel;

import java.util.ArrayList;
import java.util.List;

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
}
