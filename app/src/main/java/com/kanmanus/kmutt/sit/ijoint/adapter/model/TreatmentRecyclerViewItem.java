package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.models.TreatmentModel;

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
public class TreatmentRecyclerViewItem extends AbstractFlexibleItem<TreatmentRecyclerViewItem.ViewHolder> {
    private TreatmentModel treatmentModel;
    private String id;
    private Listener listener;

    public interface Listener{
        void onTreatmentItemClicked(TreatmentModel treatmentModel);
    }

    public TreatmentRecyclerViewItem(TreatmentModel treatmentModel) {
        this.treatmentModel = treatmentModel;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TreatmentRecyclerViewItem) {
            TreatmentRecyclerViewItem inItem = (TreatmentRecyclerViewItem) o;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null)
                listener.onTreatmentItemClicked(holder.treatmentModel);
        });
        return holder;
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.treatmentSubjectTextView.setText(treatmentModel.getSubject());
        holder.treatmentDateTextView.setText(treatmentModel.getCreateDateText());
        holder.treatmentArmSideTextView.setText(treatmentModel.getArmSide().getLabel());
        holder.treatmentModel = this.treatmentModel;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.list_item_treatment;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends FlexibleViewHolder {
        TreatmentModel treatmentModel;
        @BindView(R.id.treatmentSubject_textView)
        TextView treatmentSubjectTextView;
        @BindView(R.id.treatmentDate_textView)
        TextView treatmentDateTextView;
        @BindView(R.id.treatmentArmSide_textView)
        TextView treatmentArmSideTextView;
        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this,view);
        }
    }
}
