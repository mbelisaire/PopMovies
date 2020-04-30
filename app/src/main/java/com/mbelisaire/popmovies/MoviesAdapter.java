package com.mbelisaire.popmovies;

import android.content.Context;
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

    private static final String JSON_MOVIE_IMAGE_PATH_KEY = "poster_path";
    private static final String JSON_MOVIE_IMAGE_URL_KEY = "https://image.tmdb.org/t/p/w500";

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
    public void onBindViewHolder(MoviesAdapter.MoviesHolder holder, int position) {
        JSONObject movie = movies.optJSONObject(position);
        String imagePath = movie.optString(JSON_MOVIE_IMAGE_PATH_KEY);
        String imageUrl = JSON_MOVIE_IMAGE_URL_KEY.concat(imagePath);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.movieImage);
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
}
