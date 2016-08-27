package com.kanmanus.kmutt.sit.ijoint.adapter.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.kanmanus.kmutt.sit.ijoint.Contextor;
import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.models.ExerciseVideoModel;

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
public class ExerciseVideoRecyclerViewItem extends AbstractFlexibleItem<ExerciseVideoRecyclerViewItem.ViewHolder> implements YouTubeThumbnailView.OnInitializedListener {

    private ExerciseVideoModel modelItem;
    private String id;
    private Listener listener;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;



    public interface Listener {
        void onExerciseItemClicked(ExerciseVideoModel item);
    }

    public ExerciseVideoRecyclerViewItem(ExerciseVideoModel modelItem) {
        this.modelItem = modelItem;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExerciseVideoRecyclerViewItem) {
            ExerciseVideoRecyclerViewItem inItem = (ExerciseVideoRecyclerViewItem) o;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onExerciseItemClicked(holder.modelItem);
        });
        return holder;
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.nameTextView.setText(modelItem.getName());
        holder.descriptionTextView.setText(modelItem.getDescription());
        holder.modelItem = this.modelItem;
        holder.thumbnailView.setTag(modelItem.getYoutubeLinkId());
        holder.thumbnailView.initialize(Contextor.getInstance().getContext().getString(R.string.API_KEY), this);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.list_item_sample_video;
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        youTubeThumbnailLoader.setVideo(String.valueOf(youTubeThumbnailView.getTag()));
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends FlexibleViewHolder {
        ExerciseVideoModel modelItem;
        @BindView(R.id.name_textView)
        TextView nameTextView;
        @BindView(R.id.description_textView)
        TextView descriptionTextView;
        @BindView(R.id.imageView_thumbnail)
        YouTubeThumbnailView thumbnailView;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
