package com.udacity.popularmoviesstage1.utilities;

import android.net.Uri;

import com.udacity.popularmoviesstage1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by HP Pavilion on 3/11/2018.
 */

public class NetworkUtils {

    final static String MOVIE_DB_URL_BASE = "https://api.themoviedb.org/3/movie/";
    final static String POPULAR_MOVIES = "popular";
    final static String TOP_RATED_MOVIES = "top_rated";

    final static String PARAM_API_KEY = "api_key";
    final static String API_KEY = BuildConfig.API_KEY;

    public static URL buildUrl(String preferenceVal) {
        Uri builtUri;

        if (preferenceVal.equals(POPULAR_MOVIES)) {
            builtUri = Uri.parse(MOVIE_DB_URL_BASE).buildUpon()
                    .appendPath(POPULAR_MOVIES)
                    .appendQueryParameter(PARAM_API_KEY, API_KEY)
                    .build();
        } else {
            builtUri = Uri.parse(MOVIE_DB_URL_BASE).buildUpon()
                    .appendPath(TOP_RATED_MOVIES)
                    .appendQueryParameter(PARAM_API_KEY, API_KEY)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream input = connection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

}
