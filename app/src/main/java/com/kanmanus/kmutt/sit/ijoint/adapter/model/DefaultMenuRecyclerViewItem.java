package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.fix.AppMenu;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by niceinkeaw on 29/3/2559.
 */
public class DefaultMenuRecyclerViewItem extends AbstractFlexibleItem<DefaultMenuRecyclerViewItem.ViewHolder> {
    public interface Listener{
        void onMenuItemClicked(AppMenu appMenu);
    }

    private int iconResId;
    private String name;
    private Class<? extends Activity> activity;
    private String id;
    private AppMenu appMenu;
    private Listener listener;

    public DefaultMenuRecyclerViewItem(String text, int iconResId, AppMenu appMenu){
        this.iconResId = iconResId;
        this.name = text;
        this.appMenu = appMenu;
        this.id = UUID.randomUUID().toString();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DefaultMenuRecyclerViewItem) {
            DefaultMenuRecyclerViewItem inItem = (DefaultMenuRecyclerViewItem) o;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onMenuItemClicked(holder.appMenu);
            }
        });
        return holder;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.nameTextView.setText(name);
        holder.avatar.setImageResource(iconResId);
        holder.appMenu = appMenu;

    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_image_with_text;
    }

    static class ViewHolder extends FlexibleViewHolder {
        AppMenu appMenu;
        @BindView(R.id.avatar)
        CircleImageView avatar;
        @BindView(R.id.name_textView)
        TextView nameTextView;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this,view);
        }
    }
}
