package com.mbelisaire.popmovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mbelisaire.popmovies.databinding.DetailActivityBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetailActivity extends AppCompatActivity {

    private String movieVideosUrl = Constants.MOVIE_GET_VIDEOS_URL + Config.API_KEY;
    private String movieReviewsUrl = Constants.MOVIE_GET_REVIEWS_URL + Config.API_KEY;
    private String movieId = "";
    private JSONArray movieVideos, movieReviews;
    private MovieVideosAdapter movieVideosAdapter;
    private MovieReviewsAdapter movieReviewsAdapter;
    private RecyclerView movieVideosRecycler, movieReviewsRecycler;
    private boolean isFavoriteMovie = false;
    private Movie movie;
    private AppDatabase mDb;

    JsonObjectRequest movieVideosJsonObjectRequest, movieReviewsJsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.detail_activity);

        Intent intent = getIntent();
        if(intent == null) {
            closeOnError();
            return;
        }

        movie = (Movie) intent.getSerializableExtra(Constants.EXTRA_MOVIE);
        if(movie == null){
            closeOnError();
            return;
        }

        movieId = movie.getMovieId().toString();

        buildMovieVideosJsonObjectRequest();
        buildMovieReviewsJsonObjectRequest();

        movieVideosRecycler = findViewById(R.id.movieVideosRecycler);
        movieReviewsRecycler = findViewById(R.id.movieReviewsRecycler);

        mDb = AppDatabase.getInstance(getApplicationContext());


        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(movieVideosJsonObjectRequest);
        queue.add(movieReviewsJsonObjectRequest);

        populateUI(binding);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(DetailActivityBinding binding){
        binding.movieTitle.setText(movie.getTitle());
        binding.movieReleaseDate.setText(movie.getReleaseYear());
        binding.movieVoteAverage.setText(Double.toString(movie.getVoteAverage()).concat("/10"));
        binding.moviePlot.setText(movie.getPlot());
        ImageView posterView = findViewById(R.id.moviePoster);
        Picasso.get()
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(posterView);

        ImageView favoriteIcon = findViewById(R.id.toggleFavorite);
        checkIfFavoriteMovie();

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavoriteMovie){
                    removeFavoriteMovie(v);
                    disableFavoriteIcon();
                }else {
                    addFavoriteMovie(v);
                    enableFavoriteIcon();
                }
                isFavoriteMovie = !isFavoriteMovie;
            }
        });
    }

    private void disableFavoriteIcon(){
        ImageView favoriteIcon = findViewById(R.id.toggleFavorite);
        favoriteIcon.setColorFilter(Color.parseColor("#8A8A8A"));
    }

    private void enableFavoriteIcon() {
        ImageView favoriteIcon = findViewById(R.id.toggleFavorite);
        favoriteIcon.clearColorFilter();
    }

    private void buildMovieVideosJsonObjectRequest() {
        movieVideosJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.MOVIE_DATA_BASE_URL + movieId + movieVideosUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                movieVideos = response.optJSONArray(Constants.JSON_MOVIE_DATA_RESULTS_KEY);
                movieVideosAdapter = new MovieVideosAdapter(getApplicationContext(), movieVideos);
                if(movieVideosAdapter.getItemCount() < 1){
                    findViewById(R.id.movieNoVideoLabel).setVisibility(View.VISIBLE);
                }
                movieVideosRecycler.setAdapter(movieVideosAdapter);
                movieVideosRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                movieVideosRecycler.setNestedScrollingEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Failed to fetch movie videos data. " + error.getMessage());
            }
        });
    }

    private void buildMovieReviewsJsonObjectRequest() {
        movieReviewsJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.MOVIE_DATA_BASE_URL + movieId + movieReviewsUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                movieReviews = response.optJSONArray(Constants.JSON_MOVIE_DATA_RESULTS_KEY);
                movieReviewsAdapter = new MovieReviewsAdapter(getApplicationContext(), movieReviews);
                if(movieReviewsAdapter.getItemCount() < 1){
                    findViewById(R.id.movieNoReviewLabel).setVisibility(View.VISIBLE);
                }
                movieReviewsRecycler.setAdapter(movieReviewsAdapter);
                movieReviewsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                movieReviewsRecycler.setNestedScrollingEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Failed to fetch movie reviews data. " + error.getMessage());
            }
        });
    }

    private void addFavoriteMovie(View view) {
        addFavoriteMovie(movie);
    }

    private void removeFavoriteMovie(final View view) {
        removeFavoriteMovie(movie);
    }

    private void removeFavoriteMovie(final Movie movie) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> matchedFavoriteMovies = mDb.movieDao().findMovieWithMovieId(movie.getMovieId());
                if(matchedFavoriteMovies.size() == 1) {
                    mDb.movieDao().deleteFavoriteMovie(matchedFavoriteMovies.get(0));
                }
            }
        });
    }

    private void addFavoriteMovie(final Movie movie) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> matchedFavoriteMovies = mDb.movieDao().findMovieWithMovieId(movie.getMovieId());
                if(matchedFavoriteMovies.size() < 1) {
                    mDb.movieDao().insertFavoriteMovie(movie);
                }
            }
        });
    }

    public void checkIfFavoriteMovie() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> matchedFavoriteMovies = mDb.movieDao().findMovieWithMovieId(movie.getMovieId());
                if(matchedFavoriteMovies.size() == 1) {
                    isFavoriteMovie =  true;
                } else {
                    isFavoriteMovie = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            disableFavoriteIcon();
                        }
                    });
                }
            }
        });

    }
}
