package com.mbelisaire.popmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {



    private String baseUrl = "http://api.themoviedb.org/3/movie/";
    private String movieVideosUrl = "/videos?api_key=" + Constants.API_KEY;
    private String movieId = "";
    private JSONArray movieVideos;
    private MovieVideosAdapter movieVideosAdapter;
    private RecyclerView movieVideosRecycler;

    JsonObjectRequest movieVideosJsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.detail_activity);

        Intent intent = getIntent();
        if(intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(Constants.EXTRA_POSITION, Constants.DEFAULT_POSITION);
        if(position == Constants.DEFAULT_POSITION){
            closeOnError();
            return;
        }

        String moviesJSONString = intent.getStringExtra(Constants.EXTRA_MOVIES);
        if(moviesJSONString == null){
            closeOnError();
            return;
        }

        JSONArray movies;
        try {
            movies = new JSONArray(moviesJSONString);
        } catch (JSONException e) {
            e.printStackTrace();
            closeOnError();
            return;
        }

        JSONObject movieJson = movies.optJSONObject(position);
        Double id = movieJson.optDouble(Constants.JSON_MOVIE_ID_KEY);
        movieId = id.toString();
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

        buildMovieVideosJsonObjectRequest();

        movieVideosRecycler = findViewById(R.id.movieVideosRecycler);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(movieVideosJsonObjectRequest);


        Movie movie = new Movie(id, posterUrl, title, releaseDate , vote, plot);
        populateUI(movie, binding);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie, DetailActivityBinding binding){
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
    }

    private void buildMovieVideosJsonObjectRequest() {
        movieVideosJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl + movieId + movieVideosUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                movieVideos = response.optJSONArray(Constants.JSON_MOVIE_VIDEOS_RESULTS_KEY);
                movieVideosAdapter = new MovieVideosAdapter(getApplicationContext(), movieVideos);
                movieVideosRecycler.setAdapter(movieVideosAdapter);
                movieVideosRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Failed to fetch movie videos data. " + error.getMessage());
            }
        });
    }
}
