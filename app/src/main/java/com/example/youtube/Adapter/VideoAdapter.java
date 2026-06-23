package com.example.youtube.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.youtube.R;
import com.example.youtube.model.VideoItem;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<VideoItem> videoList;

    public VideoAdapter(List<VideoItem> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem video = videoList.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videoList != null ? videoList.size() : 0;
    }

    public void updateList(List<VideoItem> newList) {
        this.videoList = newList;
        notifyDataSetChanged();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView channelTitleTextView;
        private TextView publishedAtTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            channelTitleTextView = itemView.findViewById(R.id.channelTitleTextView);
            publishedAtTextView = itemView.findViewById(R.id.publishedAtTextView);
        }

        public void bind(VideoItem video) {
            VideoItem.Snippet snippet = video.getSnippet();

            titleTextView.setText(snippet.getTitle());
            descriptionTextView.setText(snippet.getDescription());
            channelTitleTextView.setText(snippet.getChannelTitle());
            publishedAtTextView.setText(snippet.getPublishedAt());

            if (snippet.getThumbnails() != null && snippet.getThumbnails().getMedium() != null) {
                String thumbnailUrl = snippet.getThumbnails().getMedium().getUrl();
                Glide.with(itemView.getContext())
                        .load(thumbnailUrl)
                        .into(thumbnailImageView);
            }
        }
    }
}

