package com.mbelisaire.popmovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by myriambelisaire on 30/04/2020.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder>{

    private static final String JSON_MOVIE_POSTER_PATH_KEY = "poster_path";


    private Context ctx;
    private JSONArray moviesJson;
    private List<Movie> movies;
    private boolean isJsonData = false;


    public MoviesAdapter(Context ct) {
        this.ctx = ct;
        this.movies = new ArrayList<>();
    }

    @Override
    public MoviesAdapter.MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater movieInflater = LayoutInflater.from(ctx);
        View movieView = movieInflater.inflate(R.layout.movies_cell, parent, false);
        return new MoviesHolder(movieView);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MoviesHolder holder, int position) {

        String posterUrl;
        if(isJsonData){
            JSONObject movieJson = moviesJson.optJSONObject(position);
            String posterPath = movieJson.optString(JSON_MOVIE_POSTER_PATH_KEY);
            posterUrl = Constants.JSON_MOVIE_IMAGE_URL_KEY.concat(posterPath);
        } else {
            Movie movie = movies.get(position);
            posterUrl = movie.getPosterUrl();
        }

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
        return isJsonData ?  moviesJson.length() : movies.size();
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
        Movie movie;
        if(isJsonData){
            movie = movieJsonToMovieObject(moviesJson.optJSONObject(position));
        } else {
            movie = movies.get(position);
        }
        intent.putExtra(Constants.EXTRA_MOVIE, movie);
        ctx.startActivity(intent);
    }

    private Movie movieJsonToMovieObject(JSONObject movieJson) {
        Long id = movieJson.optLong(Constants.JSON_MOVIE_ID_KEY);
        String posterPath = movieJson.optString(Constants.JSON_MOVIE_POSTER_PATH_KEY);
        String posterUrl = Constants.JSON_MOVIE_IMAGE_URL_KEY.concat(posterPath);
        String title = movieJson.optString(Constants.JSON_MOVIE_TITLE_KEY);
        String release = movieJson.optString(Constants.JSON_MOVIE_RELEASE_KEY);
        Double vote = movieJson.optDouble(Constants.JSON_MOVIE_VOTE_KEY);
        String plot = movieJson.optString(Constants.JSON_MOVIE_PLOT_KEY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = null;
        try {
            releaseDate = sdf.parse(release);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Movie(id, posterUrl, title, releaseDate , vote, plot);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        this.isJsonData = false;
        notifyDataSetChanged();
    }

    public void setMovies(JSONArray moviesJson) {
        this.moviesJson = moviesJson;
        this.isJsonData = true;
        notifyDataSetChanged();
    }
}
