package com.udacity.popularmoviesstage1.utilities;

import com.udacity.popularmoviesstage1.MovieRecord;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by HP Pavilion on 3/25/2018.
 */

public class JsonParser {

    private static final String JSON_RESULT_FLAG = "results";

    public static String parseJsonResponse(String jsonString) {
        String resultString = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.getJSONArray(JSON_RESULT_FLAG);
            resultString = resultsArray.toString();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return resultString;
        }
    }

    public static MovieRecord[] parseResponseToMovieRecordArray(String parsedJsonArrayString) {
        try {
            JSONArray parsedJsonArray = new JSONArray(parsedJsonArrayString);
            MovieRecord[] resultMovieRecordArray = new MovieRecord[parsedJsonArray.length()];
            for (int i=0; i < parsedJsonArray.length(); i++) {
                JSONObject jsonObject = parsedJsonArray.getJSONObject(i);
                int voteCount = jsonObject.getInt("vote_count");
                int id = jsonObject.getInt("id");
                Boolean video = jsonObject.getBoolean("video");
                float voteAverage = (float) jsonObject.getDouble("vote_average");
                String title = jsonObject.getString("title");
                float popularity = (float) jsonObject.getDouble("popularity");
                String posterPath = jsonObject.getString("poster_path");
                String originalLanguage = jsonObject.getString("original_language");
                String originalTitle = jsonObject.getString("original_title");
                String backdropPath = jsonObject.getString("backdrop_path");
                Boolean adult = jsonObject.getBoolean("adult");
                String overview = jsonObject.getString("overview");
                String releaseDate = jsonObject.getString("release_date");

                JSONArray genreIdsJsonArray = jsonObject.getJSONArray("genre_ids");
                int[] genreIds = new int[genreIdsJsonArray.length()];
                for (int j=0; j < genreIdsJsonArray.length(); j++) {
                    genreIds[j] = genreIdsJsonArray.getInt(j);
                }

                resultMovieRecordArray[i] = new MovieRecord(voteCount, id, video , voteAverage, title,
                        popularity, posterPath, originalLanguage, originalTitle, genreIds, backdropPath, adult,
                        overview, releaseDate);
            }
            return resultMovieRecordArray;
        } catch (Exception e) {
            e.printStackTrace();
            MovieRecord[] resultMovieRecordArray = {};
            return resultMovieRecordArray;
        }
    }
}
