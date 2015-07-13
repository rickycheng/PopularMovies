package com.cgjin.ricky.popularmovies;

import android.graphics.Bitmap;

/**
 * Created by ricky on 15-07-12.
 */
public class MovieItem {
    private Bitmap thumbnail;
    private String title;

    public MovieItem(Bitmap thumbnail, String title) {
        super();
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
