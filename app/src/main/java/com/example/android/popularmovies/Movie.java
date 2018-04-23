package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    /** Property movieId */
    int movieId;

    /** Property movieTitle */
    String movieTitle;

    /** Property releaseDate */
    String releaseDate;

    /** Property moviePoster */
    String moviePoster;

    /** Property voteAverage */
    double voteAverage;

    /** Property plotSynopsis */
    String plotSynopsis;

    String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    /**
     * Constructor
     */
    public Movie(int movieId, String movieTitle, String releaseDate, String moviePoster, double voteAverage, String plotSynopsis) {

        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;

    }

    /**
     * Constructor
     * @param position
     */
    public Movie(int position) {
    }

    public  Movie(Parcel in) {
        movieId = in.readInt();
        movieTitle = in.readString();
        releaseDate = in.readString();
        moviePoster = in.readString();
        voteAverage = in.readDouble();
        plotSynopsis = in.readString();

    }

    /**
     * Gets the movie Id
     *
     */
    public int getMovieId() {return this.movieId;
    }

    /**
     * Sets the movieId
     */
    public void setMovieId() {this.movieId = movieId;
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
     * Gets the releaseDate
     */
    public String getReleaseDate() {
        return this.releaseDate;
    }

    /**
     * Sets the releaseDate
     */
    public void setReleaseDate() {
        this.releaseDate = releaseDate;
    }

    /**
     * Gets the moviePoster
     */
    public String getMoviePoster() {
        return MOVIE_IMAGE_BASE_URL + this.moviePoster;
    }

    /**
     * Sets the moviePoster
     */
    public void setMoviePoster() {
        this.moviePoster = moviePoster;
    }

    public String getPosterPath() {
        return this.moviePoster;
    }

    /**
     * Gets the voteAverage
     */
    public double getVoteAverage() {
        return this.voteAverage;
    }

    /**
     * Sets the voteAverage
     */
    public void setVoteAverage() {
        this.voteAverage = voteAverage;
    }

    /**
     * Gets the plotSynopsis
     */
    public String getPlotSynopsis() {
        return this.plotSynopsis;
    }

    /**
     * Sets the plotSynopsis
     */
    public void setPlotSynopsis() {
        this.plotSynopsis = plotSynopsis;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(movieTitle);
        dest.writeString(releaseDate);
        dest.writeString(moviePoster);
        dest.writeDouble(voteAverage);
        dest.writeString(plotSynopsis);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };
}
