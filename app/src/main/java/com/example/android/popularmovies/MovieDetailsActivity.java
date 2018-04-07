package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDBHelper;
import com.example.android.popularmovies.data.MoviesProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.os.Build.ID;
import static com.example.android.popularmovies.data.MoviesContract.CONTENT_AUTHORITY;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClicked {

    private static final String TAG = "MyActivity";

    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper moviesHelper;

    // Code for the UriMatcher //////
    private static final int MOVIES_WITH_ID = 110;
    ////////

    private Movie selectedMovie;
    Context context;
    private ArrayList reviewsList = new ArrayList<>();
    private ArrayList<Trailers> trailersList = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private TrailersAdapter trailersAdapter;
    private RecyclerView movieReviewsRecyclerView;
    private RecyclerView movieTrailersRecyclerView;
    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<Reviews> reviews = new ArrayList<>();
    ArrayList<Trailers> trailers = new ArrayList<>();
    private Reviews movieReviews;
    private Trailers movieTrailers;
    int movieId;
    Cursor moviesCursor;
    String searchString;
    // Defines a string to contain the selection clause
    String selection = null;
    // Initializes an array to contain selection arguments
    String[] selectionArgs = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie_details );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        Intent intent = getIntent();
        selectedMovie = intent.getParcelableExtra( "movie" );
        movieReviews = intent.getParcelableExtra( "reviews" );
        movieTrailers = intent.getParcelableExtra( "trailers" );

        if (selectedMovie != null) {
            ImageView moviePoster = (ImageView) findViewById( R.id.movie_poster );
            Picasso.with( context ).load( selectedMovie.getMoviePoster() ).into( moviePoster );
            TextView movieTitle = (TextView) findViewById( R.id.movie_title );
            movieTitle.setText( selectedMovie.getMovieTitle() );
            TextView releaseDateText = (TextView) findViewById( R.id.movie_release_date );
            releaseDateText.setText( R.string.release_date );
            TextView releaseDate = (TextView) findViewById( R.id.movie_release_date_content );
            releaseDate.setText( selectedMovie.getReleaseDate() );
            TextView voteAverageText = (TextView) findViewById( R.id.movie_vote_average );
            voteAverageText.setText( R.string.vote_average );
            TextView voteAverage = (TextView) findViewById( R.id.movie_vote_average_content );
            voteAverage.setText( Double.toString( selectedMovie.getVoteAverage() ) );
            TextView plotSynopsis = (TextView) findViewById( R.id.movie_plot_synopsis );
            plotSynopsis.setText( selectedMovie.getPlotSynopsis() );
        }

        movieReviewsRecyclerView = (RecyclerView) findViewById( R.id.recycler_view_reviews );

        movieTrailersRecyclerView = (RecyclerView) findViewById( R.id.recycler_view_trailers );

        reviewsAdapter = new ReviewsAdapter( reviews, this );

        trailersAdapter = new TrailersAdapter( trailers, this );

        trailersAdapter.setOnClick( this );

        movieReviewsRecyclerView.setAdapter( reviewsAdapter );

        movieTrailersRecyclerView.setAdapter( trailersAdapter );

        // set a GridLayoutManager with default vertical orientation and 2 columns
        GridLayoutManager gridLayoutManagerReviews = new GridLayoutManager( this, 2 );
        movieReviewsRecyclerView.setLayoutManager( gridLayoutManagerReviews ); // set LayoutManager to RecyclerView

        // set a GridLayoutManager with default vertical orientation and 2 columns
        GridLayoutManager gridLayoutManagerTrailers = new GridLayoutManager( this, 2 );
        movieTrailersRecyclerView.setLayoutManager( gridLayoutManagerTrailers ); // set LayoutManager to RecyclerView
        trailersAdapter.setOnClick(); // Bind the listener

        new MovieDetailsActivity.MoviesAsyncTaskReviews().execute( String.valueOf( selectedMovie.getMovieId() ) );

        if (movieReviews != null) {
            TextView movieReviewsAuthor = (TextView) findViewById( R.id.reviews_author );
            movieReviewsAuthor.setText( movieReviews.getMovieReviewsAuthor() );
            TextView movieReviewsContent = (TextView) findViewById( R.id.reviews_content );
            movieReviewsContent.setText( movieReviews.getMovieReviewsContent() );
            Log.i( TAG, "MovieDetailsActivity.getView() — get item number " + reviews );
        }

        new MovieDetailsActivity.MoviesAsyncTaskTrailers().execute( String.valueOf( selectedMovie.getMovieId() ) );

        if (movieTrailers != null) {
            ImageView movieTrailersImage = (ImageView) findViewById( R.id.trailers_image );
            TextView movieTrailersName = (TextView) findViewById( R.id.trailers_name );
            movieTrailersName.setText( movieTrailers.getMovieTrailersName() );
            Button trailersButton = (Button) findViewById( R.id.trailers_button );
            Log.i( TAG, "MovieDetailsActivity.getView() — get item number " + trailers );

        }
        Utils.buildURL( String.valueOf( selectedMovie.getMovieId() ) );

        Utils.buildURLReviews( String.valueOf( selectedMovie.getMovieId() ) );
        Utils.buildURLTrailers( String.valueOf( selectedMovie.getMovieId() ) );

        Uri QUERY_CONTENT_URI = Uri.parse( CONTENT_AUTHORITY + "/" + MoviesContract.MoviesEntry.TABLE_MOVIES + "/" + selectedMovie.getMovieId() );
        String stringUri;
        stringUri = QUERY_CONTENT_URI.toString();
        Log.i( TAG, stringUri );

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class MoviesAsyncTaskReviews extends AsyncTask<String, Void, ArrayList<Reviews>> {

        @Override
        protected ArrayList<Reviews> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<Reviews> resultReviews = Utils.fetchMovieReviewsData( urls[0] );

            return resultReviews;
        }


        @Override
        protected void onPostExecute(ArrayList<Reviews> reviews) {

            if (reviews != null && !reviews.isEmpty()) {
                reviewsList = reviews;
                reviewsAdapter.addAll( reviews );
            }
        }
    }

    private class MoviesAsyncTaskTrailers extends AsyncTask<String, Void, ArrayList<Trailers>> {

        @Override
        protected ArrayList<Trailers> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<Trailers> resultTrailers = Utils.fetchMovieTrailersData( urls[0] );

            return resultTrailers;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailers> trailers) {

            if (trailers != null && !trailers.isEmpty()) {
                trailersList = trailers;
                trailersAdapter.addAll( trailers );
            }
        }
    }

    @Override
    public void onTrailerClick(Trailers trailer) {

        watchTrailers( trailer );

        Log.i( TAG, "MovieDetailsActivity.onItemClick() " + trailer );
    }

    public void watchTrailers(Trailers trailer) {
        Intent appIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( "vnd.youtube:" + trailer.getMovieTrailersKey() ) );
        Intent webIntent = new Intent( Intent.ACTION_VIEW,
                Uri.parse( "http://www.youtube.com/watch?v=" + trailer.getMovieTrailersKey() ) );
        try {
            startActivity( appIntent );
        } catch (ActivityNotFoundException ex) {
            startActivity( webIntent );
        }
    }

    // A "projection" defines the columns that will be returned for each row
    String[] favoriteMovies =
            {
                    MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,    // Contract class constant for the _ID column name
                    MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE,   // Contract class constant for the image column name
                    MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE  // Contract class constant for the title column name
            };


    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher( UriMatcher.NO_MATCH );
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI( authority, MoviesContract.MoviesEntry.TABLE_MOVIES + "/#", MOVIES_WITH_ID );

        return matcher;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor moviesCursor;
        switch (sUriMatcher.match( uri )) {

            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException( "Unknown uri: " + uri );
            }
            // Individual movie based on Id selected
            case MOVIES_WITH_ID: {
                moviesCursor = moviesHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_MOVIES,
                        projection,
                        MoviesContract.MoviesEntry._ID + " = ?",
                        new String[]{String.valueOf( ContentUris.parseId( uri ) )},
                        null,
                        null,
                        sortOrder );
            }
        }

        moviesCursor = getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,  // The content URI of the movies table
                favoriteMovies,                       // The columns to return for each row
                selection,                   // Either null, or the word the user entered
                selectionArgs,                    // Either empty, or the string the user entered
                sortOrder );                       // The sort order for the returned rows
        if (null == moviesCursor) {
            /*
             * Insert code here to handle the error. Be sure not to use the cursor! You may want to
             * call android.util.Log.e() to log this error.
             *
             */
            // If the Cursor is empty, the provider found no matches
        } else if (moviesCursor.getCount() < 1) {

            /*
             * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
             * an error. You may want to offer the user the option to insert a new row, or re-type the
             * search term.
             */

        }
        return moviesCursor;
    }
}

















