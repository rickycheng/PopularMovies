package com.cgjin.ricky.popularmovies;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by ricky on 15-07-12.
 */
public class MovieItem {
    private Bitmap thumbnail;
    private String thumbnail_url="/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
    private String original_title;
    private String overview;
    private Double user_rating;
    private Date release_date;
    private String id;
    private final String POSTER_BASEURL = "http://image.tmdb.org/t/p/w185/";

    public MovieItem(String id, String original_title, String thumbnail_url, String overview, Double user_rating, Date release_date) {
        this.thumbnail_url = thumbnail_url;
        this.original_title = original_title;
        this.overview = overview;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.id = id;
    }

    public MovieItem(Bitmap thumbnail, String title) {
        super();
        this.thumbnail = thumbnail;
        this.original_title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail_url() {
        return POSTER_BASEURL + thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(Double user_rating) {
        this.user_rating = user_rating;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "MovieItem{" +
                "thumbnail_url='" + thumbnail_url + '\'' +
                ", original_title='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", user_rating=" + user_rating +
                ", release_date=" + release_date +
                ", id='" + id + '\'' +
                '}';
    }
}
