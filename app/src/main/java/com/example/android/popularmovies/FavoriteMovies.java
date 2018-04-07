package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 31.03.2018.
 */

public class FavoriteMovies implements Parcelable {

    /** Property movieImage */
    String movieImage;

    /** Property movieName */
    String movieTitle;

    String MOVIE_IMAGE_BASE_URL = "http://img.youtube.com/vi/";

    public FavoriteMovies(String movieImage, String movieTitle){

        this.movieImage = movieImage;
        this.movieTitle = movieTitle;
    }

    /**
     * Constructor
     * @param position
     */
    public FavoriteMovies(int position) {
    }

    public FavoriteMovies(Parcel in) {

        movieImage = in.readString();
        movieTitle = in.readString();

    }

    /**
     * Gets the movieTitle
     */
    public String getMovieTitle() {
        return this.movieTitle;
    }

    /**
     * Sets the movieTitle
     */
    public void setMovieTitle() {
        this.movieTitle = movieTitle;
    }

    /**
     * Gets the movieImage
     */
    public String getMovieImage() {
        return MOVIE_IMAGE_BASE_URL + this.movieImage;
    }

    /**
     * Sets the movieImage
     */
    public void setMovieImage() {
        this.movieImage = movieImage;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(movieTitle);
        dest.writeString(movieImage);

    }

    public static final Parcelable.Creator<FavoriteMovies> CREATOR = new Parcelable.Creator<FavoriteMovies>()
    {
        public FavoriteMovies createFromParcel(Parcel in)
        {
            return new FavoriteMovies(in);
        }
        public FavoriteMovies[] newArray(int size)
        {
            return new FavoriteMovies[size];
        }
    };
}
