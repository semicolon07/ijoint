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
import de.hdodenhof.circleimageview.CircleImageView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by niceinkeaw on 29/3/2559.
 */
public class ProfileRecyclerViewItem extends AbstractFlexibleItem<ProfileRecyclerViewItem.ViewHolder> {

    private int avatarResId;
    private String fullName;
    private String email;
    private String gender;
    private String age;
    private String id;

    public ProfileRecyclerViewItem() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProfileRecyclerViewItem) {
            ProfileRecyclerViewItem inItem = (ProfileRecyclerViewItem) o;
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
        holder.avatar.setImageResource(avatarResId);
        holder.nameTextView.setText(fullName);
        holder.emailTextView.setText(email);
        holder.genderTextView.setText(gender);
        holder.ageTextView.setText(age);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_profile;
    }

    static class ViewHolder extends FlexibleViewHolder {
        @BindView(R.id.avatar)
        CircleImageView avatar;
        @BindView(R.id.name_textView)
        TextView nameTextView;
        @BindView(R.id.email_textView)
        TextView emailTextView;
        @BindView(R.id.gender_textView)
        TextView genderTextView;
        @BindView(R.id.age_textView)
        TextView ageTextView;
        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this,view);
        }
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
