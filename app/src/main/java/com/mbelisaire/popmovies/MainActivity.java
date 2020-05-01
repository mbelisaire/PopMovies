package com.mbelisaire.popmovies;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_MOVIES_RESULTS_KEY = "results";
    public static JSONArray currentMovies;

    RecyclerView moviesRecycler;
    MoviesAdapter popAdapter, topAdapter;
    JSONArray popularMovies, topMovies;
    boolean isSortedByPopular = true;


    //Please, provide API Key from The Movie Database API

    String API_KEY = "";
    String popularMoviesUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    String topMoviesUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;

    JsonObjectRequest popularMoviesJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, popularMoviesUrl, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
                popularMovies = response.optJSONArray(JSON_MOVIES_RESULTS_KEY);
                popAdapter = new MoviesAdapter(getApplicationContext(), popularMovies);
                moviesRecycler.setAdapter(popAdapter);
                currentMovies = popularMovies;
                moviesRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", error.getMessage());
        }
    });
    JsonObjectRequest topMoviesJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, topMoviesUrl, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            topMovies = response.optJSONArray(JSON_MOVIES_RESULTS_KEY);
            topAdapter = new MoviesAdapter(getApplicationContext(), topMovies);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", error.getMessage());
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        moviesRecycler = findViewById(R.id.moviesRecycler);
        queue.add(popularMoviesJsonObjectRequest);
        queue.add(topMoviesJsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.sorting_mode){
            isSortedByPopular = !isSortedByPopular;
            MoviesAdapter adapter = isSortedByPopular ? popAdapter : topAdapter;
            moviesRecycler.setAdapter(adapter);
            currentMovies = isSortedByPopular ? popularMovies : topMovies;
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


}
