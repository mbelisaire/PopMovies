package com.mbelisaire.popmovies;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
final public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private Long movieId;
    @ColumnInfo(name = "poster_url")
    private String posterUrl;
    private String title;
    @ColumnInfo(name = "release_date")
    private Date releaseDate;
    @ColumnInfo(name = "vote_average")
    private double voteAverage;
    private String plot;

    @Ignore
    public Movie (Long movieId, String posterUrl, String title, Date releaseDate, Double voteAverage, String plot)
    {
        this.movieId = movieId;
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public Movie (int id, Long movieId, String posterUrl, String title, Date releaseDate, Double voteAverage, String plot)
    {
        this.id = id;
        this.movieId = movieId;
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public int getId() {
        return id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(this.releaseDate);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
