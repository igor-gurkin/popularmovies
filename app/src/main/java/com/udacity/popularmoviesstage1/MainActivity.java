package com.udacity.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmoviesstage1.utilities.JsonParser;
import com.udacity.popularmoviesstage1.utilities.NetworkUtils;

import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieRecord[]>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    TextView mErrorMessageTextView;
    ProgressBar mDownloadProgressBar;
    GridView mContentListView;

    private static final String MOVIE_DB_URL_EXTRA = "url_query";
    private static final int MOVIES_DB_LOADER = 15;
    private static final String PARCELABLE_KEY = "movie_records";

    private MovieRecord[] movieDbResponse;

    private MovieRecordAdapter movieRecordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageTextView = (TextView) findViewById(R.id.conection_error_text);
        mDownloadProgressBar = (ProgressBar) findViewById(R.id.download_progress_bar);
        mContentListView = (GridView) findViewById(R.id.grid_view_movie);

        if ((savedInstanceState == null) || !savedInstanceState.containsKey(PARCELABLE_KEY)) {
            if (isConnected()) {
                loadDataFromPreferences();
            } else {
                showErrorMessage();
            }
        } else {
            movieDbResponse = (MovieRecord[]) savedInstanceState.getParcelableArray(PARCELABLE_KEY);
            if (movieDbResponse != null && movieDbResponse.length > 0) {
                showDataView();
            } else {
                showErrorMessage();
            }
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(PARCELABLE_KEY, movieDbResponse);
        super.onSaveInstanceState(outState);
    }

    private boolean isConnected() {
        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return isConnected;
    }

    private void loadDataFromPreferences() {
        String preferenceVal = getCurrentPreference();
        Bundle dbUrlBundle = buildBundle(preferenceVal);
        handleLoader(dbUrlBundle);
    }

    private String getCurrentPreference() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String preferenceResult = sharedPreferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_option));
        return preferenceResult;
    }

    private Bundle buildBundle(String preferenceVal) {
        URL movieDbuUrl = NetworkUtils.buildUrl(preferenceVal);

        Bundle dbUrlBundle = new Bundle();
        dbUrlBundle.putString(MOVIE_DB_URL_EXTRA, movieDbuUrl.toString());

        return dbUrlBundle;
    }

    private void handleLoader(Bundle dbUrlBundle) {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieRecord[]> movieDbLoader = loaderManager.getLoader(MOVIES_DB_LOADER);

        if (movieDbLoader == null) {
            loaderManager.initLoader(MOVIES_DB_LOADER, dbUrlBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_DB_LOADER, dbUrlBundle, this);
        }
    }

    private void showDataView() {
        if (movieDbResponse != null) {
            mErrorMessageTextView.setVisibility(View.INVISIBLE);
            movieRecordsAdapter = new MovieRecordAdapter(this, Arrays.asList(movieDbResponse));
            mContentListView.setAdapter(movieRecordsAdapter);
            mContentListView.setVisibility(View.VISIBLE);
            mContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("object_parcel", movieDbResponse[i]);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<MovieRecord[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieRecord[]>(this) {

            MovieRecord[] mMovieDbQueryResult;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mDownloadProgressBar.setVisibility(View.VISIBLE);
                if (mMovieDbQueryResult != null) {
                    Log.v("CACHED RESULT", "RETURNING CACHED RESULT");
                    deliverResult(mMovieDbQueryResult);
                    mDownloadProgressBar.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    forceLoad();
                }
            }

            @Override
            public MovieRecord[] loadInBackground() {
                String queryUrlString = args.getString(MOVIE_DB_URL_EXTRA);
                MovieRecord[] result = null;
                if ((queryUrlString == null) || (queryUrlString.isEmpty())) {
                    return null;
                }
                try {
                    URL queryURL = new URL(queryUrlString);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryURL);
                    String parsedResponse = JsonParser.parseJsonResponse(jsonResponse);
                    result = JsonParser.parseResponseToMovieRecordArray(parsedResponse);
                } catch(Exception e) {
                    Log.e("GOT EXCEPTION", "exception", e);
                }
                return result;
            }

            @Override
            public void deliverResult(MovieRecord[] data) {
                mMovieDbQueryResult = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieRecord[]> loader, MovieRecord[] data) {
        mDownloadProgressBar.setVisibility(View.INVISIBLE);
        if (data != null && data.length != 0) {
            movieDbResponse = data;
            showDataView();
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        mContentListView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<MovieRecord[]> loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            movieDbResponse = null;
            Bundle urlBundle = buildBundle(sharedPreferences.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popular_option)));
            handleLoader(urlBundle);
        }
    }
}
