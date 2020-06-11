package com.mbelisaire.popmovies;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
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

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView moviesRecycler;
    private MoviesAdapter moviesAdapter;
    private JSONArray popularMovies, topMovies;
    private List<Movie> favoriteMovies;

    private int selectedMenuItemId = R.id.showPopular;

    private String popularMoviesUrl = Constants.POPULAR_MOVIES_URL + Config.API_KEY;
    private String topMoviesUrl = Constants.TOP_RATED_MOVIES_URL + Config.API_KEY;

    JsonObjectRequest popularMoviesJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, popularMoviesUrl, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            popularMovies = response.optJSONArray(Constants.JSON_MOVIES_RESULTS_KEY);
            moviesAdapter.setMovies(popularMovies);
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
            topMovies = response.optJSONArray(Constants.JSON_MOVIES_RESULTS_KEY);
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



        moviesRecycler = findViewById(R.id.moviesRecycler);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecycler.setAdapter(moviesAdapter);

        int spanCount = 2;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 3;
        }
        moviesRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), spanCount));



        if(savedInstanceState != null){
            selectedMenuItemId = savedInstanceState.getInt(Constants.CACHED_MENU_ITEM_SELECTED_KEY);
            try {
                popularMovies = new JSONArray(savedInstanceState.getString(Constants.CACHED_POP_MOVIES_KEY));
                topMovies = new JSONArray(savedInstanceState.getString(Constants.CACHED_TOP_MOVIES_KEY));
                filterMovies(selectedMenuItemId);
            } catch (JSONException e) {
                e.printStackTrace();
                sendMovieDbAPIRequests();
            }
        } else {
            sendMovieDbAPIRequests();
        }

        setViewModel();
    }

    private void sendMovieDbAPIRequests() {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(popularMoviesJsonObjectRequest);
        queue.add(topMoviesJsonObjectRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.CACHED_MENU_ITEM_SELECTED_KEY, selectedMenuItemId);
        outState.putString(Constants.CACHED_POP_MOVIES_KEY, popularMovies.toString());
        outState.putString(Constants.CACHED_TOP_MOVIES_KEY, topMovies.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        selectedMenuItemId = item.getItemId();
        if(filterMovies(selectedMenuItemId)){
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
    
    private boolean filterMovies(int selectedMenuItemId){
        if(selectedMenuItemId == R.id.showPopular){
            moviesAdapter.setMovies(popularMovies);
            return true;
        } else if(selectedMenuItemId == R.id.showTopRated){
            moviesAdapter.setMovies(topMovies);
            return true;
        } else if(selectedMenuItemId == R.id.showFavorites){
            moviesAdapter.setMovies(favoriteMovies);
            return true;
        }
        return false;
    }

    private void setViewModel(){
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d(TAG, "Receiving database update from LiveData.");
                favoriteMovies = movies;
                if(selectedMenuItemId == R.id.showFavorites){
                    moviesAdapter.setMovies(favoriteMovies);
                }
            }
        });
    }
}
