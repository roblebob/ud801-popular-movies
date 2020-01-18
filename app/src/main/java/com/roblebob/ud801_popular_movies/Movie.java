package com.roblebob.ud801_popular_movies;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "Movie")
public class Movie {



    @PrimaryKey(autoGenerate = false)       private long id;
    @ColumnInfo(name = "popularity")        private double popularity;
    @ColumnInfo(name = "vote_count")        private long voteCount;
    @ColumnInfo(name = "video")             private boolean video;
    @ColumnInfo(name = "poster_path")       private String posterPath;
    @ColumnInfo(name = "adult")             private boolean adlult;
    @ColumnInfo(name = "backdrop_path")     private String backdropPath;
    @ColumnInfo(name = "original_language") private String originalLanguage;
    @ColumnInfo(name = "original_title")    private String originalTitle;
    @ColumnInfo(name = "genre_ids")         private String genreIds;
    @ColumnInfo(name = "title")             private String title;
    @ColumnInfo(name = "vote_average")      private double voteAverage;
    @ColumnInfo(name = "overview")          private String overview;
    @ColumnInfo(name = "release_date")      private String releaseDate;

    public Movie(long id, double popularity, long voteCount, boolean video, String posterPath,
                 boolean adlult, String backdropPath, String originalLanguage, String originalTitle,
                 String genreIds, String title, double voteAverage, String overview,
                 String releaseDate) {
        this.id = id;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.posterPath = posterPath;
        this.adlult = adlult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public long     getId()                 { return id; }
    public double   getPopularity()         { return popularity; }
    public long     getVoteCount()          { return voteCount; }
    public boolean  isVideo()               { return video; }
    public String   getPosterPath()         { return posterPath; }
    public boolean  isAdlult()              { return adlult; }
    public String   getBackdropPath()       { return backdropPath; }
    public String   getOriginalLanguage()   { return originalLanguage; }
    public String   getOriginalTitle()      { return originalTitle; }
    public String   getGenreIds()           { return genreIds; }
    public String   getTitle()              { return title; }
    public double   getVoteAverage()        { return voteAverage; }
    public String   getOverview()           { return overview; }
    public String   getReleaseDate()        { return releaseDate; }

    public void setId(long id)                                  { this.id = id; }
    public void setPopularity(double popularity)                { this.popularity = popularity; }
    public void setVoteCount(long voteCount)                    { this.voteCount = voteCount; }
    public void setVideo(boolean video)                         { this.video = video; }
    public void setPosterPath(String posterPath)                { this.posterPath = posterPath; }
    public void setAdlult(boolean adlult)                       { this.adlult = adlult; }
    public void setBackdropPath(String backdropPath)            { this.backdropPath = backdropPath; }
    public void setOriginalLanguage(String originalLanguage)    { this.originalLanguage = originalLanguage; }
    public void setOriginalTitle(String originalTitle)          { this.originalTitle = originalTitle; }
    public void setGenreIds(String genreIds)                    { this.genreIds = genreIds; }
    public void setTitle(String title)                          { this.title = title; }
    public void setVoteAverage(double voteAverage)              { this.voteAverage = voteAverage; }
    public void setOverview(String overview)                    { this.overview = overview; }
    public void setReleaseDate(String releaseDate)              { this.releaseDate = releaseDate; }

    @Ignore
    public List< Integer> getGenreIdList() {

        List< String> stringList = new ArrayList< String>(Arrays.asList(
                getGenreIds()
                .substring(1, getGenreIds().length() - 2)
                .split(",")
        ));
        List< Integer> integerList = new ArrayList< Integer>();
        stringList.forEach((String string) -> { integerList .add( Integer .valueOf( string)); });
        return integerList;
    }
}
