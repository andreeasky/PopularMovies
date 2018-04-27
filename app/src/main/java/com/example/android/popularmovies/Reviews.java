package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Reviews implements Parcelable{

        /** Property movieReviews ID */
        String movieReviewsId;

        /** Property movieReviews Author */
        String movieReviewsAuthor;

        /** Property movieReviews Content */
        String movieReviewsContent;

        /** Property movieReviews URL */
        String movieReviewsUrl;

        /**
         * Constructor
         */
        public Reviews (String movieReviewsId, String movieReviewsAuthor, String movieReviewsContent, String movieReviewsUrl) {

            this.movieReviewsId = movieReviewsId;
            this.movieReviewsAuthor = movieReviewsAuthor;
            this.movieReviewsContent = movieReviewsContent;
            this.movieReviewsUrl = movieReviewsUrl;
        }

        /**
         * Constructor
         * @param position
         */
        public Reviews(int position) {
        }

        public Reviews(Parcel in) {
            movieReviewsId = in.readString();
            movieReviewsAuthor = in.readString();
            movieReviewsAuthor = in.readString();
            movieReviewsAuthor = in.readString();
        }

        /**
         * Gets the movieReviews ID
         */
        public String getMovieReviewsId() {
            return this.movieReviewsId;
        }

        /**
         * Sets the movieReviews ID
         */
        public void setMovieReviewsId() {
            this.movieReviewsId = movieReviewsId;
        }

        /**
         * Gets the movieReviews Author
         */
        public String getMovieReviewsAuthor() {
            return this.movieReviewsAuthor;
        }

        /**
         * Sets the movieReviews Author
         */
        public void setMovieReviewsAuthor() {
            this.movieReviewsAuthor = movieReviewsAuthor;
        }

        /**
        * Gets the movieReviews Content
        */
        public String getMovieReviewsContent() {
        return this.movieReviewsContent;
    }

        /**
        * Sets the movieReviews Content
        */
        public void setMovieReviewsContent() {
        this.movieReviewsContent = movieReviewsContent;
    }

        /**
        * Gets the movieReviews URL
        */
        public String getMovieReviewsUrl() {
        return this.movieReviewsUrl;
    }

        /**
        * Sets the movieReviews URL
        */
        public void setMovieReviewsUrl() {
        this.movieReviewsUrl = movieReviewsUrl;
    }

    public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(movieReviewsId);
            dest.writeString(movieReviewsAuthor);
            dest.writeString(movieReviewsContent);
            dest.writeString(movieReviewsUrl);

        }

        public static final Parcelable.Creator<com.example.android.popularmovies.Reviews> CREATOR = new Parcelable.Creator<Reviews>()
        {
            public Reviews createFromParcel(Parcel in)
            {
                return new Reviews(in);
            }
            public Reviews[] newArray(int size)
            {
                return new Reviews[size];
            }
        };
    }


