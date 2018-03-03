package com.example.android.popularmovies;

import android.net.Uri;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class Utils {

    /**
     * These utilities will be used to communicate with TheMovieDB servers, to perform requests regarding API and to provide responses for network related issues.
     */
        private static final String TAG = Utils.class.getSimpleName();

     // URL to fetch data from TheMovieDB
        private static final String THE_MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/movie/";
    
     // API Key for building the URL for Top Rated Movie
        private static final String MOVIES_API_KEY = "api_key";

        private static final String RESULTS  = "results";

        private static final String TITLE = "title";

        private static final String RELEASE_DATE = "release_date";

        private static final String MOVIE_POSTER = "poster_path";

        private static final String VOTE_AVERAGE = "vote_average";

        private static final String PLOT_SYNOPSIS = "overview";

        public static URL buildURL(String sortMode) {

            Uri buildUri = Uri.parse(THE_MOVIE_DATABASE_URL).buildUpon()
                    .appendPath(sortMode)
                    .appendQueryParameter(MOVIES_API_KEY , BuildConfig.THE_MOVIE_DATABASE_API_KEY)
                    .build();

            URL url = null;

            try {
                url = new URL(buildUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return url;
        }

        public static String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }

    public static ArrayList<Movie> fetchMoviesData(String searchUrl) {

        URL url = buildURL(searchUrl);

        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractMoviesFromJson(jsonResponse);
    }

    private static ArrayList<Movie> extractMoviesFromJson(String movieJSON) {

        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(movieJSON);

            JSONObject currentMovie;

            JSONArray moviesArray = baseJsonResponse.getJSONArray(RESULTS);

            int i = 0;

            for (i = 0; i < moviesArray.length(); i++) {

                String movieTitle = "";

                String releaseDate = "";

                String moviePoster = "";

                double voteAverage;

                String plotSynopsis = "";

                currentMovie = moviesArray.getJSONObject(i);

                movieTitle = currentMovie.optString(TITLE);

                releaseDate = currentMovie.optString(RELEASE_DATE);

                moviePoster = currentMovie.optString(MOVIE_POSTER);

                voteAverage = currentMovie.optDouble(VOTE_AVERAGE);

                plotSynopsis = currentMovie.optString(PLOT_SYNOPSIS);

                Movie movie = new Movie(movieTitle, releaseDate, moviePoster, voteAverage, plotSynopsis);

                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}

