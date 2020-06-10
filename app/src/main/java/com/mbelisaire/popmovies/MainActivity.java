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

    private RecyclerView moviesRecycler;
    private MoviesAdapter popAdapter, topAdapter;
    private boolean isSortedByPopular = true;

    private String popularMoviesUrl = Constants.POPULAR_MOVIES_URL + Config.API_KEY;
    private String topMoviesUrl = Constants.TOP_RATED_MOVIES_URL + Config.API_KEY;

    JsonObjectRequest popularMoviesJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, popularMoviesUrl, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray popularMovies = response.optJSONArray(Constants.JSON_MOVIES_RESULTS_KEY);
                popAdapter = new MoviesAdapter(getApplicationContext(), popularMovies);
                moviesRecycler.setAdapter(popAdapter);
                moviesRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", "Failed to fetch data. " + error.getMessage());
        }
    });
    JsonObjectRequest topMoviesJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, topMoviesUrl, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONArray topMovies = response.optJSONArray(Constants.JSON_MOVIES_RESULTS_KEY);
            topAdapter = new MoviesAdapter(getApplicationContext(), topMovies);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Volley", "Failed to fetch data. " +  error.getMessage());
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
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
