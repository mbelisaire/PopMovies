package com.mbelisaire.popmovies;

public class Constants {
    public static final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String TOP_RATED_MOVIES_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=";

    public static String MOVIE_DATA_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static String MOVIE_GET_VIDEOS_URL = "/videos?api_key=";
    public static String MOVIE_GET_REVIEWS_URL = "/reviews?api_key=";

    public static final String JSON_MOVIES_RESULTS_KEY = "results";
    public static final String EXTRA_MOVIE = "extra_movie";

    public static final String JSON_MOVIE_ID_KEY = "id";
    public static final String JSON_MOVIE_POSTER_PATH_KEY = "poster_path";
    public static final String JSON_MOVIE_TITLE_KEY = "title";
    public static final String JSON_MOVIE_RELEASE_KEY = "release_date";
    public static final String JSON_MOVIE_VOTE_KEY = "vote_average";
    public static final String JSON_MOVIE_PLOT_KEY = "overview";
    public static final String JSON_MOVIE_IMAGE_URL_KEY = "https://image.tmdb.org/t/p/w500";
    public static final String JSON_MOVIE_DATA_RESULTS_KEY = "results";
    public static final String JSON_MOVIE_VIDEO_KEY = "key";
    public static final String JSON_MOVIE_VIDEO_NAME = "name";
    public static final String JSON_MOVIE_VIDEO_URL_BASE = "https://www.youtube.com/watch?v=";
    public static final String JSON_MOVIE_REVIEW_AUTHOR = "author";
    public static final String JSON_MOVIE_REVIEW_CONTENT = "content";

    public static final String DATABASE_NAME = "popular_movies";
    public static final String CACHED_MENU_ITEM_SELECTED_KEY= "menu_item_selected";
    public static final String CACHED_POP_MOVIES_KEY= "popular_movies";
    public static final String CACHED_TOP_MOVIES_KEY= "top_rated_movies";
}
