package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.popularmovies.data.MoviesContract.BASE_CONTENT_URI;
import static com.example.android.popularmovies.data.MoviesContract.CONTENT_AUTHORITY;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClicked {

    private static final String TAG = "MyActivity";

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie selectedMovie;
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
    private Boolean isFavorite;

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
            Picasso.with( this ).load( selectedMovie.getMoviePoster() ).into( moviePoster );
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
            Log.i( LOG_TAG, "MovieDetailsActivity.getView() — get item number " + trailers );

        }
        Utils.buildURL( String.valueOf( selectedMovie.getMovieId() ) );

        Utils.buildURLReviews( String.valueOf( selectedMovie.getMovieId() ) );
        Utils.buildURLTrailers( String.valueOf( selectedMovie.getMovieId() ) );

        Uri QUERY_CONTENT_URI = Uri.parse( BASE_CONTENT_URI + "/" + MoviesContract.MoviesEntry.TABLE_MOVIES + "/" + selectedMovie.getMovieId() );
        String stringUri;
        stringUri = QUERY_CONTENT_URI.toString();
        Log.i( TAG, stringUri );

        ContentResolver contentResolver = getContentResolver();
        Cursor favoriteMovieCursor = getContentResolver().query(
                QUERY_CONTENT_URI,
                null,                       // The columns to return for each row
                null,                   // Either null, or the movie the user selected
                null,                    // Either empty, or the string the user entered
                null );
        favoriteMovieCursor.getCount();

        if (favoriteMovieCursor != null) {

            final ImageButton favoriteMovie = (ImageButton) findViewById( R.id.button_favorite );

                if (favoriteMovieCursor.getCount() > 0) {
                isFavorite = true;
                favoriteMovie.setActivated( true );
            } else {
                isFavorite = false;
                favoriteMovie.setActivated( false );
                Toast.makeText( getBaseContext(), "This movie is not a Favorite movie", Toast.LENGTH_LONG ).show();
            }
        }

        final ImageButton favoriteMovie = (ImageButton) findViewById( R.id.button_favorite );

        favoriteMovie.setOnClickListener( new View.OnClickListener() {

            public void onClick(View v) {
                if (isFavorite == false) {
                    insertData();
                    favoriteMovie.setActivated( true );

                } else {
                    deleteData();
                    favoriteMovie.setActivated( false );
                }
            }
        } );

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

    public void insertData() {

        isFavorite = true;

        Uri uriMovie;

        ContentValues movieValues = new ContentValues();
        movieValues.put( MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, selectedMovie.getMovieId() );
        movieValues.put( MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE, selectedMovie.getMoviePoster() );
        movieValues.put( MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, selectedMovie.getMovieTitle() );

        uriMovie = getContentResolver().insert(
                MoviesContract.MoviesEntry.CONTENT_URI,   // the movie content URI
                movieValues ); // the values to insert

        if (uriMovie != null)
            Toast.makeText( getBaseContext(), uriMovie.toString(), Toast.LENGTH_LONG ).show();

    }

    public void deleteData() {

        isFavorite = false;

        ContentResolver contentResolver = getContentResolver();

        long deleteMovie = contentResolver.delete(
                MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{String.valueOf( selectedMovie.getMovieId() )} );
        Log.i("deleteMovie",""+deleteMovie);
    }

}




















