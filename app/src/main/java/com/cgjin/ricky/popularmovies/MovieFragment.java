package com.cgjin.ricky.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {
    private GridView movieGridView;
    private MovieGridAdapter movieGridAdapter;
    private String mSortBy = null;
    public final String SORT_BY_SAVEDINSTANCESTATE_KEY = "SAVEd_INSTANCE_KEY";

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(SORT_BY_SAVEDINSTANCESTATE_KEY);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMovieInfoTask movieTask = new FetchMovieInfoTask();
            movieTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ImageView iv = (ImageView) findViewByID(R.id.picasso_imageview);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieGridView = (GridView) rootView.findViewById(R.id.movie_GridView);
        movieGridAdapter = new MovieGridAdapter(rootView.getContext(), R.layout.movie_item, new ArrayList<MovieItem>());

        movieGridView.setAdapter(movieGridAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem item = (MovieItem)movieGridAdapter.getItem(position);
                //String movieInfo = item.getOriginal_title();
                //Toast.makeText(getActivity(),movieInfo, Toast.LENGTH_SHORT).show();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("original_title", item.getOriginal_title());
                intent.putExtra("release_date", formatter.format(item.getRelease_date()));
                intent.putExtra("thumbnail_url",item.getThumbnail_url());
                intent.putExtra("vote_average",item.getUser_rating().toString());
                intent.putExtra("plot", item.getOverview());

                startActivity(intent);
            }
        });
        return rootView;
    }

    private void updateMovieInfo()
    {
        FetchMovieInfoTask movieInfoTask = new FetchMovieInfoTask();
        movieInfoTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        // get sort by from share preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPref.getString(
                getString(R.string.preference_sort_key),
                getString(R.string.preference_sort_default));

        if (mSortBy == null || mSortBy.compareTo(sortBy) != 0) {
            mSortBy = sortBy;

            updateMovieInfo();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SORT_BY_SAVEDINSTANCESTATE_KEY, mSortBy);
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(MOVIE_KEY, (ArrayList<? extends Parcelable>) listOfMovies);

    }

    public class FetchMovieInfoTask extends AsyncTask<String, Void, List<MovieItem>> {

        private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

        @Override
        protected List<MovieItem> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonString = null;
            //String sortOption = "popularity.desc";
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mSortBy = prefs.getString(getString(R.string.preference_sort_key),getString(R.string.preference_sort_default));
            String APIKey = "21ce5cf7d4a6b25066226b8888f7b082";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORTBY_PARAM = "sort_by";
                final String APIKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTBY_PARAM, mSortBy)
                        .appendQueryParameter(APIKEY_PARAM, APIKey).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonString = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonString = null;
                }
                movieJsonString = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieJsonString = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            super.onPostExecute(movieItems);
            if (movieItems != null) {
                movieGridAdapter.clear();
                for (MovieItem mi : movieItems) {
                    movieGridAdapter.add(mi);
                }
            }
        }

        private List<MovieItem> getMovieDataFromJson(String movieJsonStr) throws JSONException {

            final String MOVIEDB_LIST = "results";
            final String MOVIEDB_ID = "id";
            final String MOVIEDB_TITLE = "original_title";
            final String MOVIEDB_OVERVIEW = "overview";
            final String MOVIEDB_RELEASE_DATE = "release_date";
            final String MOVIEDB_POSTER = "poster_path";
            final String MOVIEDB_RATINGS = "vote_average";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(MOVIEDB_LIST);

            List<MovieItem> movieList = new ArrayList<MovieItem>();

            for (int i = 0; i < movieArray.length(); i++) {
                String movieID;
                String originalTitle;
                String overview;
                Date releaseDate;
                String posterURL;
                Double ratings;

                JSONObject movieObject = movieArray.getJSONObject(i);
                movieID = movieObject.getString(MOVIEDB_ID);
                originalTitle = movieObject.getString(MOVIEDB_TITLE);
                overview = movieObject.getString(MOVIEDB_OVERVIEW);
                posterURL = movieObject.getString(MOVIEDB_POSTER);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                releaseDate = new Date();
                try {
                    releaseDate = dateFormat.parse(movieObject.getString(MOVIEDB_RELEASE_DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ratings = Double.parseDouble(movieObject.getString(MOVIEDB_RATINGS));
                MovieItem mi = new MovieItem(movieID, originalTitle, posterURL, overview, ratings, releaseDate);
                movieList.add(mi);

            }

            for (MovieItem mi : movieList) {
                Log.v(LOG_TAG, mi.toString());
            }

            return movieList;

        }
    }
}
