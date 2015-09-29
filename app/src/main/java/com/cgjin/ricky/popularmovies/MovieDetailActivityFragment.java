package com.cgjin.ricky.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        if(intent != null)
        {
            String movieTitle = intent.getStringExtra("original_title");
            String moviePlot = intent.getStringExtra("plot");
            String thumbnailURL = intent.getStringExtra("thumbnail_url");
            String movieReleaseDate = intent.getStringExtra("release_date");
            String movieVoteAverage = intent.getStringExtra("vote_average");

            //intent.putExtra("original_title", item.getOriginal_title());
            //intent.putExtra("release_date", item.getRelease_date().toString());
            //intent.putExtra("thumbnail_url",item.getThumbnail_url());
            //intent.putExtra("vote_average",item.getUser_rating().toString());
            //intent.putExtra("plot", item.getOverview());

            ((TextView) rootView.findViewById(R.id.detail_text)).setText(movieTitle);
            ImageView ivThumbnail = (ImageView)rootView.findViewById(R.id.detail_thumbnail);
            Picasso.with(getActivity())
                    .load(thumbnailURL)
                    .into(ivThumbnail);

            TextView movieDescription = (TextView) rootView.findViewById(R.id.detail_description);
            movieDescription.setText("Release date: "+movieReleaseDate+"\n\n"+"Vote average: "+movieVoteAverage+"\n\n"+"Plot:\n"+moviePlot);
        }
        return rootView;
    }
}
