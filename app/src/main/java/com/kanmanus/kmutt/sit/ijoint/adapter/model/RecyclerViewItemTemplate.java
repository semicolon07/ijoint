package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanmanus.kmutt.sit.ijoint.R;

import java.util.List;
import java.util.UUID;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by niceinkeaw on 29/3/2559.
 */
public class RecyclerViewItemTemplate extends AbstractFlexibleItem<RecyclerViewItemTemplate.ViewHolder> {
    private String headerText;
    private String id;

    public RecyclerViewItemTemplate(String headerText){
        this.headerText = headerText;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RecyclerViewItemTemplate) {
            RecyclerViewItemTemplate inItem = (RecyclerViewItemTemplate) o;
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
        holder.headerTextView.setText(headerText);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_header;
    }

    static class ViewHolder extends FlexibleViewHolder {

        public TextView headerTextView;
        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            headerTextView  = (TextView)view.findViewById(R.id.header_textView);
        }
    }
}
