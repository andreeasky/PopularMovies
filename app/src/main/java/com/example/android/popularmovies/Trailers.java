package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailers implements Parcelable{

        /** Property movieTrailersId */
        String movieTrailersId;

        /** Property movieTrailersKey */
        String movieTrailersKey;

        /** Property movieTrailers Image*/
        String movieTrailersImage;

        /** Property movieTrailers Name*/
        String movieTrailersName;

        /** Property movieTrailers Size*/
        String movieTrailersSize;

        /** Property movieTrailersType */
        String movieTrailersType;

        String MOVIE_TRAILER_BASE_URL = "https://img.youtube.com/vi/" + "id" + "/0.jpg";

        /**
         * Constructor
         */
        public Trailers (String movieTrailersId, String movieTrailersKey, String  movieTrailersImage, String movieTrailersName, String movieTrailersSize, String movieTrailersType) {

            this.movieTrailersId = movieTrailersId;
            this.movieTrailersKey = movieTrailersKey;
            this.movieTrailersImage = movieTrailersImage;
            this.movieTrailersName = movieTrailersName;
            this.movieTrailersSize = movieTrailersSize;
            this.movieTrailersType = movieTrailersType;
        }

        /**
         * Constructor
         * @param position
         */
        public Trailers(int position) {
        }

        public Trailers(Parcel in) {

            movieTrailersId = in.readString();
            movieTrailersKey = in.readString();
            movieTrailersImage = in.readString();
            movieTrailersName = in.readString();
            movieTrailersSize = in.readString();
            movieTrailersType = in.readString();
        }

        /**
        * Gets the movieTrailersId
        */
        public String getMovieTrailersId() {return this.movieTrailersId;}

        /**
        * Sets the movieTrailersId
        */
        public void setMovieTrailersId() {
        this.movieTrailersId = movieTrailersId;
    }

        /**
        * Gets the movieTrailersKey
        */
        public String getMovieTrailersKey() {return this.movieTrailersKey;}

        /**
        * Sets the movieTrailersKey
        */
        public void setMovieTrailersKey() {
        this.movieTrailersKey = movieTrailersKey;
    }


        /**
        * Gets the movieTrailersImage
        */
        public String getMovieTrailersImage() {return MOVIE_TRAILER_BASE_URL + this.movieTrailersImage;}

        /**
        * Sets the movieTrailersImage
        */
        public void setMovieTrailersImage() {
        this.movieTrailersImage = movieTrailersImage;
    }

        /**
         * Gets the movieTrailersName
         */
        public String getMovieTrailersName() {
            return this.movieTrailersName;
        }

        /**
         * Sets the movieTrailersName
         */
        public void setMovieTrailersName() {
            this.movieTrailersName = movieTrailersName;
        }

        /**
        * Gets the movieTrailersName
        */
        public String getMovieTrailersSize() {
        return this.movieTrailersSize;
    }

        /**
        * Sets the movieTrailersName
        */
        public void setMovieTrailerSize() {
        this.movieTrailersSize = movieTrailersSize;
    }

        /**
         * Gets the movieTrailersType
         */
        public String getMovieTrailersType() {
        return this.movieTrailersType;
    }

        /**
         * Sets the movieTrailersType
         */
        public void setMovieTrailersType() {
        this.movieTrailersType = movieTrailersType;
    }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(movieTrailersId);
            dest.writeString(movieTrailersKey);
            dest.writeString(movieTrailersImage);
            dest.writeString(movieTrailersName);
            dest.writeString(movieTrailersSize);
            dest.writeString(movieTrailersType);
        }

        public static final Parcelable.Creator<Trailers> CREATOR = new Parcelable.Creator<Trailers>()
        {
            public Trailers createFromParcel(Parcel in)
            {
                return new Trailers(in);
            }
            public Trailers[] newArray(int size)
            {
                return new Trailers[size];
            }
        };
    }



