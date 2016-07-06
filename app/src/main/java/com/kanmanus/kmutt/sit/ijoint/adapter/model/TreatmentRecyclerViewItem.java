package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanmanus.kmutt.sit.ijoint.R;

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

    private String id;

    public TreatmentRecyclerViewItem() {

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
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_treatment;
    }

    static class ViewHolder extends FlexibleViewHolder {
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
