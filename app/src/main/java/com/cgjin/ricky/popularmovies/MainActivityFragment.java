package com.cgjin.ricky.popularmovies;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private GridView movieGridView;
    private MovieGridAdapter movieGridAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ImageView iv = (ImageView) findViewByID(R.id.picasso_imageview);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieGridView = (GridView) rootView.findViewById(R.id.movie_GridView);
        movieGridAdapter = new MovieGridAdapter(rootView.getContext(), R.layout.movie_item, getData());

        movieGridView.setAdapter(movieGridAdapter);

        //ImageView iv = (ImageView) rootView.findViewById(R.id.picasso_imageview);
        // Picasso.with(this.getActivity())
        //        .load("https://cms-assets.tutsplus.com/uploads/users/21/posts/19431/featured_image/CodeFeature.jpg")
        //        .into(iv);
        return rootView;
    }

    private ArrayList<MovieItem> getData()
    {
        final ArrayList<MovieItem> movieItems = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.movie01);
        movieItems.add(new MovieItem(bitmap,"Jurassic World"));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.movie02);
        movieItems.add(new MovieItem(bitmap,"Terminator Genisys"));
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.movie03);
        movieItems.add(new MovieItem(bitmap,"Minions"));
        return movieItems;
    }
}
