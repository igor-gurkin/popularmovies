package com.udacity.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP Pavilion on 3/25/2018.
 */

public class MovieRecord implements Parcelable {
    int voteCount;
    int id;
    Boolean video;
    float voteAverage;
    String title;
    float popularity;
    String posterPath;
    String originalLanguage;
    String originalTitle;
    int[] genreIds;
    String backdropPath;
    Boolean adult;
    String overview;
    String releaseDate;

    final String POSTER_URL_BASE = "http://image.tmdb.org/t/p/";
    final String POSTER_SIZE = "w342";

    public MovieRecord(int voteCount, int id, Boolean video, float voteAverage, String title,
                       float popularity, String posterPath, String originalLanguage,
                       String originalTitle, int[] genreIds, String backdropPath, Boolean adult,
                       String overview, String releaseDate) {
        this.voteCount = voteCount;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;

        StringBuilder posterStringBuilder = new StringBuilder().append(POSTER_URL_BASE)
                .append(POSTER_SIZE).append(posterPath);
        this.posterPath = posterStringBuilder.toString();

        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    private MovieRecord(Parcel in) {
        this.voteCount = in.readInt();
        this.id = in.readInt();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readFloat();
        this.title = in.readString();
        this.popularity = in.readFloat();
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = in.createIntArray();
        this.backdropPath = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(voteCount);
        parcel.writeInt(id);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeFloat(voteAverage);
        parcel.writeString(title);
        parcel.writeFloat(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeIntArray(genreIds);
        parcel.writeString(backdropPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieRecord> CREATOR = new Parcelable.Creator<MovieRecord>() {

        @Override
        public MovieRecord createFromParcel(Parcel parcel) {
            return new MovieRecord(parcel);
        }

        @Override
        public MovieRecord[] newArray(int i) {
            return new MovieRecord[i];
        }
    };
}
