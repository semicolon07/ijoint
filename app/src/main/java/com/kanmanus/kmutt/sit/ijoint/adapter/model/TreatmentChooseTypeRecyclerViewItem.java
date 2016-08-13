package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.datamanager.TreatmentDataManager;

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
public class TreatmentChooseTypeRecyclerViewItem extends AbstractFlexibleItem<TreatmentChooseTypeRecyclerViewItem.ViewHolder> {

    private final Listener listener;
    private String id;

    public interface Listener{
        void onTreatmentArmSideClick(String armSide);
    }

    public TreatmentChooseTypeRecyclerViewItem(Listener listener) {
        this.id = UUID.randomUUID().toString();
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TreatmentChooseTypeRecyclerViewItem) {
            TreatmentChooseTypeRecyclerViewItem inItem = (TreatmentChooseTypeRecyclerViewItem) o;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
        holder.btnLeft.setOnClickListener(onTreatmentTypeButtonClick);
        holder.btnAll.setOnClickListener(onTreatmentTypeButtonClick);
        holder.btnRight.setOnClickListener(onTreatmentTypeButtonClick);
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_treatment_type;
    }

    private View.OnClickListener onTreatmentTypeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_all:
                    listener.onTreatmentArmSideClick(TreatmentDataManager.ARM_SIDE_ALL);
                    break;
                case R.id.btn_right:
                    listener.onTreatmentArmSideClick(TreatmentDataManager.ARM_SIDE_RIGHT);
                    break;
                case R.id.btn_left:
                    listener.onTreatmentArmSideClick(TreatmentDataManager.ARM_SIDE_LEFT);
                    break;
                default:
                    break;
            }
        }
    };

    static class ViewHolder extends FlexibleViewHolder {

        @BindView(R.id.btn_all)
        Button btnAll;
        @BindView(R.id.btn_left)
        Button btnLeft;
        @BindView(R.id.btn_right)
        Button btnRight;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
