package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.support.v7.widget.RecyclerView;

import java.util.UUID;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by Semicolon07 on 7/8/2016 AD.
 */

public abstract class BaseAbstractFlexibleItem<VH extends RecyclerView.ViewHolder> extends AbstractFlexibleItem {
    private String id;

    public BaseAbstractFlexibleItem(){
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseAbstractFlexibleItem) {
            BaseAbstractFlexibleItem inItem = (BaseAbstractFlexibleItem) o;
            return this.id.equals(inItem.id);
        }
        return false;
    }
}
