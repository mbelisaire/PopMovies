package com.mbelisaire.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final  int DEFAULT_POSITION = -1;
    private static final String JSON_MOVIE_POSTER_PATH_KEY = "poster_path";
    private static final String JSON_MOVIE_TITLE_KEY = "title";
    private static final String JSON_MOVIE_RELEASE_KEY = "release_date";
    private static final String JSON_MOVIE_VOTE_KEY = "vote_average";
    private static final String JSON_MOVIE_PLOT_KEY = "overview";
    public static final String JSON_MOVIE_IMAGE_URL_KEY = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent intent = getIntent();
        if(intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if(position == DEFAULT_POSITION){
            closeOnError();
            return;
        }

        JSONArray movies = MainActivity.currentMovies;

        JSONObject movieJson = movies.optJSONObject(position);
        String posterPath = movieJson.optString(JSON_MOVIE_POSTER_PATH_KEY);
        String posterUrl = JSON_MOVIE_IMAGE_URL_KEY.concat(posterPath);
        String title = movieJson.optString(JSON_MOVIE_TITLE_KEY);
        String release = movieJson.optString(JSON_MOVIE_RELEASE_KEY);
        Double vote = movieJson.optDouble(JSON_MOVIE_VOTE_KEY);
        String plot = movieJson.optString(JSON_MOVIE_PLOT_KEY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = null;
        try {
            releaseDate = sdf.parse(release);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Movie movie = new Movie(posterUrl, title, releaseDate , vote, plot);
        populateUI(movie);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie){
        TextView titleTV = findViewById(R.id.movieTitle);
        titleTV.setText(movie.getTitle());
        TextView releaseDateTV = findViewById(R.id.movieReleaseDate);
        releaseDateTV.setText(movie.getReleaseYear());
        TextView voteTV = findViewById(R.id.movieVoteAverage);
        voteTV.setText(Double.toString(movie.getVoteAverage()).concat("/10"));
        TextView plotTV = findViewById(R.id.moviePlot);
        plotTV.setText(movie.getPlot());
        ImageView posterView = findViewById(R.id.moviePoster);
        Picasso.get()
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(posterView);
    }
}
