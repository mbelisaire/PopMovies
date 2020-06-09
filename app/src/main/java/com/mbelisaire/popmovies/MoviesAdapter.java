package com.mbelisaire.popmovies;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by myriambelisaire on 30/04/2020.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder>{

    private static final String JSON_MOVIE_POSTER_PATH_KEY = "poster_path";


    private Context ctx;
    private JSONArray movies;

    public MoviesAdapter(Context ct, JSONArray movies) {
        ctx = ct;
        this.movies = movies;
    }

    @Override
    public MoviesAdapter.MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater movieInflater = LayoutInflater.from(ctx);
        View movieView = movieInflater.inflate(R.layout.movies_cell, parent, false);
        return new MoviesHolder(movieView);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MoviesHolder holder, int position) {
        JSONObject movieJson = movies.optJSONObject(position);
        String posterPath = movieJson.optString(JSON_MOVIE_POSTER_PATH_KEY);
        String posterUrl = Constants.JSON_MOVIE_IMAGE_URL_KEY.concat(posterPath);
        Picasso.get()
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.movieImage);
        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMovieDetailActivity(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.length();
    }

    public class MoviesHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        public MoviesHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImage);
        }
    }

    private void launchMovieDetailActivity(int position) {
        Intent intent = new Intent(ctx, MovieDetailActivity.class);
        intent.putExtra(Constants.EXTRA_POSITION, position);
        intent.putExtra(Constants.EXTRA_MOVIES, movies.toString());
        ctx.startActivity(intent);
    }
}
