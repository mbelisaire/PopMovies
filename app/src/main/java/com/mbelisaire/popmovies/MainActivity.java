package com.mbelisaire.popmovies;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    public static final String JSON_MOVIES_RESULTS_KEY = "results";

    RecyclerView moviesRecycler;
    MoviesAdapter adapter;
    JSONArray movies;


    //Please, provide API Key from The Movie Database API

    String API_KEY = "";
    String url = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
                movies = response.optJSONArray(JSON_MOVIES_RESULTS_KEY);
                adapter = new MoviesAdapter(getApplicationContext(), movies);
                moviesRecycler.setAdapter(adapter);
                moviesRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //TODO: Handle API Response Error.
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        moviesRecycler = findViewById(R.id.moviesRecycler);
        queue.add(jsonObjectRequest);

    }
}
