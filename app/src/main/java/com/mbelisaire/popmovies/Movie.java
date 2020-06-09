package com.mbelisaire.popmovies;

import java.text.SimpleDateFormat;
import java.util.Date;

final public class Movie {

    private Double id;
    private String posterUrl;
    private String title;
    private Date releaseDate;
    private double voteAverage;
    private String plot;

    public Movie (Double id, String posterUrl, String title, Date releaseDate, Double voteAverage, String plot)
    {
        this.id = id;
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public Double getId() {
        return id;
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

    public void setId(Double id) {
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
