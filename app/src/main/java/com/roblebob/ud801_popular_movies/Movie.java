package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "Movie")
public class Movie {

    @PrimaryKey(autoGenerate = false)       private int id;
    @ColumnInfo(name = "is_one_of_my_favorites") private boolean oneOfMyFavorites;
    @ColumnInfo(name = "popularity")        private double popularity;
    @ColumnInfo(name = "vote_average")      private double voteAverage;
    @ColumnInfo(name = "vote_count")        private int voteCount;
    @ColumnInfo(name = "poster_url")        private String posterUrl;

    @ColumnInfo(name = "title")             private String title;
    @ColumnInfo(name = "original_title")    private String originalTitle;
    @ColumnInfo(name = "original_language") private String originalLanguage;
    @ColumnInfo(name = "release_date")      private String releaseDate;
    @ColumnInfo(name = "runtime")           private String runtime;
    @ColumnInfo(name = "tagline")           private String tagline;
    @ColumnInfo(name = "overview")          private String overview;
    @ColumnInfo(name = "genres")            private String genres;
    @ColumnInfo(name = "budget")            private String budget;
    @ColumnInfo(name = "revenue")           private String revenue;
    @ColumnInfo(name = "trailer_titles")    private String trailerTitles;
    @ColumnInfo(name = "trailer_urls")      private String trailerUrls;
    @ColumnInfo(name = "review_authors")    private String reviewAuthors;
    @ColumnInfo(name = "review_urls")       private String reviewUrls;


    public Movie(int id,
                 boolean oneOfMyFavorites,
                 double popularity,
                 double voteAverage,
                 int    voteCount,
                 String title,
                 String posterUrl,


                 String originalTitle,
                 String originalLanguage,
                 String releaseDate,
                 String runtime,
                 String tagline,
                 String overview,
                 String genres,
                 String budget,
                 String revenue,
                 String trailerTitles,
                 String trailerUrls,
                 String reviewAuthors,
                 String reviewUrls
    ){
        this.id = id;
        this.oneOfMyFavorites = oneOfMyFavorites;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.title = title;
        this.posterUrl = posterUrl;

        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.tagline = tagline;
        this.overview = overview;
        this.genres = genres;
        this.budget = budget;
        this.revenue = revenue;
        this.trailerTitles = trailerTitles;
        this.trailerUrls = trailerUrls;
        this.reviewAuthors = reviewAuthors;
        this.reviewUrls = reviewUrls;
    }


    @Ignore @NotNull
    public Movie( Movie movie)  {
        this.id = movie.id;
        this.oneOfMyFavorites = movie.oneOfMyFavorites;
        this.popularity = movie.popularity;
        this.voteAverage = movie.voteAverage;
        this.voteCount = movie.voteCount;
        this.title = movie.title;
        this.posterUrl = movie.posterUrl;

        this.originalTitle = movie.originalTitle;
        this.originalLanguage = movie.originalLanguage;
        this.releaseDate = movie.releaseDate;
        this.runtime = movie.runtime;
        this.tagline = movie.tagline;
        this.overview = movie.overview;
        this.genres = movie.genres;
        this.budget = movie.budget;
        this.revenue = movie.revenue;
        this.trailerTitles = movie.trailerTitles;
        this.trailerUrls = movie.trailerUrls;
        this.reviewAuthors = movie.reviewAuthors;
        this.reviewUrls = movie.reviewUrls;
    }



    public int              getId()                 { return this.id; }
    public boolean          isOneOfMyFavorites()    { return this.oneOfMyFavorites; }
    public double           getPopularity()         { return this.popularity; }
    public double           getVoteAverage()        { return this.voteAverage; }
    public int              getVoteCount()          { return this.voteCount; }
    public String           getTitle()              { return this.title; }
    public String           getPosterUrl()          { return this.posterUrl; }


    public String           getOriginalTitle()      { return this.originalTitle; }
    public String           getOriginalLanguage()   { return this.originalLanguage; }
    public String           getReleaseDate()        { return this.releaseDate; }
    public String           getRuntime()            { return this.runtime; }
    public String           getTagline()            { return this.tagline; }
    public String           getOverview()           { return this.overview; }
    public String           getGenres()             { return this.genres; }
    public String           getBudget()             { return this.budget; }
    public String           getRevenue()            { return this.revenue; }
    public String           getTrailerTitles()      { return this.trailerTitles; }
    public String           getTrailerUrls()        { return this.trailerUrls; }
    public String           getReviewAuthors()      { return this.reviewAuthors; }
    public String           getReviewUrls()         { return this.reviewUrls; }



    public void setId(int id)                                   { this.id = id; }
    public void setOneOfMyFavorites(boolean oneOfMyFavorites)   {  this.oneOfMyFavorites = oneOfMyFavorites; }
    public void setPopularity(double popularity)                { this.popularity = popularity; }
    public void setVoteAverage(double voteAverage)              { this.voteAverage = voteAverage; }
    public void setVoteCount(int voteCount)                     { this.voteCount = voteCount; }
    public void setPosterUrl(String posterUrl)                  { this.posterUrl = posterUrl; }

    public void setTitle(String title)                          { this.title = title; }
    public void setOriginalTitle(String originalTitle)          { this.originalTitle = originalTitle; }
    public void setOriginalLanguage(String originalLanguage)    { this.originalLanguage = originalLanguage; }
    public void setReleaseDate(String releaseDate)              { this.releaseDate = releaseDate; }
    public void setRuntime( String runtime)                     { this.runtime = runtime; }
    public void setTagline( String tagline)                     { this.tagline = tagline; }
    public void setOverview(String overview)                    { this.overview = overview; }
    public void setGenres(String genres)                        { this.genres = genres; }
    public void setBudget(String budget)                        { this.budget = budget; }
    public void setRevenue(String revenue)                      { this.revenue = revenue; }
    public void setTrailerTitles(String trailerTitles)          { this.trailerTitles = trailerTitles; }
    public void setTrailerUrls(String trailerUrls)              { this.trailerUrls = trailerUrls; }
    public void setReviewAuthors( String reviewAuthors)         { this.reviewAuthors = reviewAuthors; }
    public void setReviewUrls( String reviewUrls)               { this.reviewUrls = reviewUrls; }






    @Ignore
    @NonNull
    @Override
    public String toString() {
        return  "\t Movie(  " +
                "[id]:"           +   this.id                 + ",  " +
                "[popular]:"      +   this.popularity         + ",  " +
                "[vote_avg]:"     +   this.voteAverage        + ",  " +
                "[vote_count]:"   +   this.voteCount          + ",  " +
                "[title]:"        +   this.title              + ",  " +
                "[poster_url]:"   +   this.posterUrl          + ",  " +


                "[orig_title]:"   +   this.originalTitle      + ",  " +
                "[orig_lang]:"    +   this.originalLanguage   + ",  " +
                "[released]:"      +   this.releaseDate        + ",  " +
                "[runtime]:"      +   this.runtime            + ",  " +
                "[tagline]:"       +   this.tagline            + ",  " +
                "[overview]:"     +   this.overview           + ",  " +
                "[genres]:"       +   this.genres             + ",  " +
                "[budget]:"       +   this.budget             + ",  " +
                "[revenue]:"      +   this.revenue            +
                ") \t \t ";
    }
}
