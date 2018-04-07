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

        private static final String MOVIE_ID = "id";

        private static final String TITLE = "title";

        private static final String RELEASE_DATE = "release_date";

        private static final String MOVIE_POSTER = "poster_path";

        private static final String VOTE_AVERAGE = "vote_average";

        private static final String PLOT_SYNOPSIS = "overview";

        private static final String MOVIE_REVIEWS = "reviews";

        private static final String MOVIE_TRAILERS = "videos";

        private static final String MOVIE_REVIEWS_ID = "id";

        private static final String MOVIE_REVIEWS_AUTHOR = "author";

        private static final String MOVIE_REVIEWS_CONTENT = "content";

        private static final String MOVIE_REVIEWS_URL = "url";

        private static final String MOVIE_TRAILERS_ID = "id";

        private static final String MOVIE_TRAILERS_KEY = "key";

        private static final String MOVIE_TRAILERS_IMAGE = "poster_path";

        private static final String MOVIE_TRAILERS_NAME = "name";

        private static final String MOVIE_TRAILERS_SIZE = "size";

        private static final String MOVIE_TRAILERS_TYPE = "type";

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

                int movieId;

                String movieTitle = "";

                String releaseDate = "";

                String moviePoster = "";

                double voteAverage;

                String plotSynopsis = "";

                currentMovie = moviesArray.getJSONObject(i);

                movieId = currentMovie.optInt(MOVIE_ID);

                movieTitle = currentMovie.optString(TITLE);

                releaseDate = currentMovie.optString(RELEASE_DATE);

                moviePoster = currentMovie.optString(MOVIE_POSTER);

                voteAverage = currentMovie.optDouble(VOTE_AVERAGE);

                plotSynopsis = currentMovie.optString(PLOT_SYNOPSIS);

                Movie movie = new Movie(movieId, movieTitle, releaseDate, moviePoster, voteAverage, plotSynopsis);

                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static URL buildURLReviews(String movieId) {

        Uri buildUri = Uri.parse(THE_MOVIE_DATABASE_URL).buildUpon()
                .appendPath( String.valueOf( movieId ) )
                .appendPath(MOVIE_REVIEWS)
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

    public static ArrayList<Reviews> fetchMovieReviewsData(String searchUrl) {

        URL url = buildURLReviews(searchUrl);

        String jsonResponseReviews = null;
        try {
            jsonResponseReviews = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractMovieReviewsFromJson(jsonResponseReviews);
    }

    private static ArrayList<Reviews> extractMovieReviewsFromJson(String movieJSON) {

        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        ArrayList<Reviews> reviews = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(movieJSON);

            JSONObject currentMovieReviews;

            JSONArray reviewsArray = baseJsonResponse.getJSONArray(RESULTS);

            int i = 0;

            for (i = 0; i < reviewsArray.length(); i++) {

                String movieReviewsId = "";

                String movieReviewsAuthor = "";

                String movieReviewsContent = "";

                String movieReviewsUrl = "";

                currentMovieReviews = reviewsArray.getJSONObject(i);

                movieReviewsId = currentMovieReviews.optString(MOVIE_REVIEWS_ID);

                movieReviewsAuthor = currentMovieReviews.optString(MOVIE_REVIEWS_AUTHOR);

                movieReviewsContent = currentMovieReviews.optString(MOVIE_REVIEWS_CONTENT);

                movieReviewsUrl = currentMovieReviews.optString(MOVIE_REVIEWS_URL);

                Reviews review = new Reviews (movieReviewsId, movieReviewsAuthor, movieReviewsContent, movieReviewsUrl);

                reviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static URL buildURLTrailers(String movieId) {

        Uri buildUri = Uri.parse(THE_MOVIE_DATABASE_URL).buildUpon()
                .appendPath( String.valueOf( movieId ) )
                .appendPath(MOVIE_TRAILERS)
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

    public static ArrayList<Trailers> fetchMovieTrailersData(String searchUrl) {

        URL url = buildURLTrailers(searchUrl);

        String jsonResponseTrailers = null;
        try {
            jsonResponseTrailers = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractMovieTrailersFromJson(jsonResponseTrailers);
    }

    private static ArrayList<Trailers> extractMovieTrailersFromJson(String movieJSON) {

        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        ArrayList<Trailers> trailers = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(movieJSON);

            JSONObject currentMovieTrailers;

            JSONArray trailersArray = baseJsonResponse.getJSONArray(RESULTS);

            int i = 0;

            for (i = 0; i < trailersArray.length(); i++) {

                String movieTrailersId = "";

                String movieTrailersKey = "";

                String movieTrailersImage = "";

                String movieTrailersName = "";

                String movieTrailersSize = "";

                String movieTrailersType = "";

                currentMovieTrailers = trailersArray.getJSONObject(i);

                movieTrailersId = currentMovieTrailers.optString(MOVIE_TRAILERS_ID);

                movieTrailersKey = currentMovieTrailers.optString(MOVIE_TRAILERS_KEY);

                movieTrailersImage = currentMovieTrailers.optString(MOVIE_TRAILERS_IMAGE);

                movieTrailersName = currentMovieTrailers.optString(MOVIE_TRAILERS_NAME);

                movieTrailersSize = currentMovieTrailers.optString(MOVIE_TRAILERS_SIZE);

                movieTrailersType = currentMovieTrailers.optString(MOVIE_TRAILERS_TYPE);

                Trailers trailer = new Trailers (movieTrailersId, movieTrailersKey, movieTrailersImage, movieTrailersName, movieTrailersSize, movieTrailersType);

                trailers.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}



