package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kanmanus.kmutt.sit.ijoint.Contextor;
import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by niceinkeaw on 29/3/2559.
 */
public class TaskRecyclerViewItem extends AbstractFlexibleItem<TaskRecyclerViewItem.ViewHolder> {
    public interface Listener{
        void onTaskItemClicked(Task task);
    }
    private Task task;
    private String id;
    private Listener listener;

    public TaskRecyclerViewItem(Task task) {
        this.task = task;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TaskRecyclerViewItem) {
            TaskRecyclerViewItem inItem = (TaskRecyclerViewItem) o;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null)
                listener.onTaskItemClicked(holder.task);
        });
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.task = this.task;
        if(task.is_synced == null)
            return;
        int colorResId = R.color.complete;
        int statusNameResId = R.string.task_complete;
        if(task.is_synced.equals(Task.TASK_COMPLETE)){
            statusNameResId = R.string.task_complete;
            colorResId = R.color.complete;
        }
        if(task.is_synced.equals(Task.TASK_READY)){
            statusNameResId = R.string.task_ready;
            colorResId = R.color.ready;
        }
        if(task.is_synced.equals(Task.TASK_SYNCED)){
            statusNameResId = R.string.task_sync;
            colorResId = R.color.sync;
        }

        String side = (task.side.equals("l"))?"Left":"Right";
        String taskDate = getTaskDateText(task.date);
        int exerciseTypeRedId = R.string.task_exercise_type_extension;
        if(task.exercise_type.equals(Task.EXTENSION)){
            exerciseTypeRedId = R.string.task_exercise_type_extension;
        }
        if(task.exercise_type.equals(Task.HORIZONTAL)){
            exerciseTypeRedId = R.string.task_exercise_type_horizontal_flexion;
        }
        if(task.exercise_type.equals(Task.FLEXION)){
            exerciseTypeRedId = R.string.task_exercise_type_flexion;
        }

        holder.status.setText(Contextor.getInstance().getContext().getString(statusNameResId));
        holder.statusBarContainer.setBackgroundResource(colorResId);
        holder.tvExerciseName.setText(side + " Shoulder Exercise" + ((task.is_abf.equals("y"))?" (ABF)":""));
        holder.tvDate.setText(taskDate);
        holder.tvTypeName.setText(Contextor.getInstance().getContext().getString(exerciseTypeRedId));
        holder.tvTargetAngle.setText(task.target_angle + "\u00b0");
        holder.tvNumberOfRound.setText(task.number_of_round);
    }

    private String getTaskDateText(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd MMM yyyy");
        String dateText = format.format(newDate);
        return dateText;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_task;
    }

    static class ViewHolder extends FlexibleViewHolder {
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.statusBar_container)
        LinearLayout statusBarContainer;
        @BindView(R.id.tv_exercise_name)
        TextView tvExerciseName;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_type_name)
        TextView tvTypeName;
        @BindView(R.id.tv_target_angle)
        TextView tvTargetAngle;
        @BindView(R.id.tv_number_of_round)
        TextView tvNumberOfRound;
        public Task task;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this,view);
        }
    }
}
