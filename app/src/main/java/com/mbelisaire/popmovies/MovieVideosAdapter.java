package com.mbelisaire.popmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.recyclerview.widget.RecyclerView;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideosHolder> {

    private Context ctx;
    private JSONArray movieVideos;

    public MovieVideosAdapter(Context ctx, JSONArray movieVideos){
        this.ctx = ctx;
        this.movieVideos = movieVideos;
    }

    @Override
    public MovieVideosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater movieInflater = LayoutInflater.from(ctx);
        View movieVideoView = movieInflater.inflate(R.layout.movie_video_row, parent, false);
        return new MovieVideosAdapter.MovieVideosHolder(movieVideoView);
    }

    @Override
    public void onBindViewHolder(MovieVideosHolder holder, int position) {
        JSONObject movieVideoJson = movieVideos.optJSONObject(position);
        holder.textView.setText(movieVideoJson.optString(Constants.JSON_MOVIE_VIDEO_NAME));
        if(position == movieVideos.length()-1){
            holder.dividerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return movieVideos.length();
    }

    public class MovieVideosHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View dividerView;
        public MovieVideosHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchVideo(getAdapterPosition());
                }
            });
            textView = itemView.findViewById(R.id.videoName);
            dividerView = itemView.findViewById(R.id.videoDivider);
        }
    }

    private void launchVideo(int position){
        JSONObject movieVideoJson = movieVideos.optJSONObject(position);
        String videoKey = movieVideoJson.optString(Constants.JSON_MOVIE_VIDEO_KEY);
        String videoUrl = Constants.JSON_MOVIE_VIDEO_URL_BASE.concat(videoKey);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        try {
            ctx.startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            Log.e("MovieVideosAdapter", "Error during videos' links creation. " + ex.getMessage());
        }
    }
}
