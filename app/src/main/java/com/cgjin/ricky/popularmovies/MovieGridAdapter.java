package com.cgjin.ricky.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ricky on 15-07-12.
 */
public class MovieGridAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList<MovieItem> data = new ArrayList<MovieItem>();

    public MovieGridAdapter(Context context, int layoutResourceId, ArrayList<MovieItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.movieTitle = (TextView) row.findViewById(R.id.movie_title_tv);
            holder.movieThumbnail = (ImageView) row.findViewById(R.id.movie_thumbnail_iv);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        MovieItem item = data.get(position);

        holder.movieTitle.setText(item.getOriginal_title());
        //holder.movieThumbnail.setImageBitmap(item.getThumbnail());
        Picasso.with(context)
                .load(item.getThumbnail_url())
                .into(holder.movieThumbnail);

        return row;
    }

    static class ViewHolder {
        TextView movieTitle;
        ImageView movieThumbnail;
    }
}
