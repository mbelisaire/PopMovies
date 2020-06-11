package com.mbelisaire.popmovies;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private  static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;
    private AppDatabase database;

    public MainViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the videos from the DataBase");
        movies = database.movieDao().loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movies;
    }
}
