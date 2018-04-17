package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClicked, LoaderManager.LoaderCallbacks<Cursor>{

    private ArrayList<Movie> moviesList = new ArrayList<>();
    private ArrayList<Reviews> reviewsList = new ArrayList<>();
    private ArrayList<Trailers> trailersList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView moviesRecyclerView;
    ArrayList<Movie> movies = new ArrayList<>();
    Context context;
    private String sortOrder = "popular";
    static final String SORT_ORDER_MOVIE = "sort_order_movie";
    static final String RECYCLER_VIEW_LAYOUT = "recycler_view_layout";
    private Parcelable layoutManagerSavedState;
    private ProgressBar moviesLoadingIndicator;

    public static final String[] MOVIES_DETAILS = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE,
    };

    // Create constant int values representing each column name's position above
    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_COLUMN_MOVIE_ID = 0;
    public static final int INDEX_COLUMN_MOVIE_IMAGE = 1;
    public static final int INDEX_COLUMN_MOVIE_TITLE = 2;

    /*
     * This ID will be used to identify the Loader responsible for loading the movies. In
     * some cases, one Activity can deal with many Loaders. However, in our case, there is only one.
     * We will still use this ID to initialize the loader and create the loader for best practice.
     * Please note that 30 was chosen arbitrarily. You can use whatever number you like, so long as
     * it is unique and consistent.
     */
    private static final int ID_MOVIES_LOADER = 30;

    private int moviePosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieAdapter = new MovieAdapter(movies, this);

        moviesRecyclerView.setAdapter(movieAdapter);

        // set a GridLayoutManager with default vertical orientation and 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        movieAdapter.setOnClick(this); // Bind the listener

        showLoading();

        getSupportLoaderManager().initLoader(ID_MOVIES_LOADER , null, this);

        if (savedInstanceState != null) {
            sortOrder = savedInstanceState.getString(SORT_ORDER_MOVIE); }
        new MoviesAsyncTask().execute(sortOrder);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SORT_ORDER_MOVIE, sortOrder);
        savedInstanceState.putParcelable(RECYCLER_VIEW_LAYOUT, moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        layoutManagerSavedState = savedInstanceState.getParcelable(RECYCLER_VIEW_LAYOUT);
        moviesRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId) {

            case ID_MOVIES_LOADER:
                /* URI for all rows of data in the movies table */
                Uri movieQueryUri =  MoviesContract.MoviesEntry.CONTENT_URI;
                /* Sort order: Ascending by movie */
                String sortOrder = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "";

                return new CursorLoader(this,
                        movieQueryUri,
                        MOVIES_DETAILS,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /* @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Call movieAdapter's swapCursor method and pass in the new Cursor
        movieAdapter.swapCursor(data);
        // If moviePosition equals RecyclerView.NO_POSITION, set it to 0
        if (moviePosition == RecyclerView.NO_POSITION) moviePosition = 0;
        // Smooth scroll the RecyclerView to moviePosition
        moviesRecyclerView.smoothScrollToPosition(moviePosition);

        // If the Cursor's size is not equal to 0, call showMoviesDataView
        if (data.getCount() != 0) showMoviesDataView();
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Call moviesAdapter's swapCursor method and pass in null
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        movieAdapter.swapCursor(null);

    }

    /**
     * This method will make the View for the movies data visible and hide the error message and
     * loading indicator.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showMoviesDataView() {
        /* First, hide the loading indicator */
        moviesLoadingIndicator.setVisibility( View.INVISIBLE);
        /* Finally, make sure the movies data is visible */
        moviesRecyclerView.setVisibility( View.VISIBLE);
    }

    // A method called showLoading that shows the loading indicator and hides the data
    /**
     * This method will make the loading indicator visible and hide the movies View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        moviesLoadingIndicator.setVisibility(View.VISIBLE);
    }


    public void setOnClick(List movies) {
        movieAdapter.setOnClick( (MovieAdapter.OnItemClicked) movies );
        restoreLayoutManagerPosition();
    }

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            moviesRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }


    @Override
    public void onItemClick(int position) {
        // The onClick implementation of the RecyclerView item click
        Movie selectedMovie = moviesList.get(position);
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("movie", selectedMovie);
        startActivity(intent);

    }

    private class MoviesAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<Movie> result = Utils.fetchMoviesData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            if (movies != null && !movies.isEmpty()) {
                moviesList = movies;
                movieAdapter.addAll(movies);
                restoreLayoutManagerPosition();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.main_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        item.setChecked(!item.isChecked());
        CharSequence message;
        switch(item.getItemId()){
            case R.id.menu_sort_by_most_popular:
                message = "Most Popular selected";
                sortOrder = "popular";
                new MoviesAsyncTask().execute(sortOrder);
                break;
            case R.id.menu_sort_by_top_rated:
                message = "Top Rated selected";
                sortOrder = "top_rated";
                new MoviesAsyncTask().execute(sortOrder);
                break;
            case R.id.menu_sort_by_favorites:
                message = "Favorites selected";
                sortOrder = "favorites";
                new MoviesAsyncTask().execute(sortOrder);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }
}


