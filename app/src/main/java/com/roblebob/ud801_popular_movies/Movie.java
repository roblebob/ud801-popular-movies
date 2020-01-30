package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Movie")
public class Movie {

    @PrimaryKey(autoGenerate = false)       private int     movieID;
    @ColumnInfo(name = "fav")               private boolean fav;
    @ColumnInfo(name = "popularVAL")        private double  popularVAL;
    @ColumnInfo(name = "voteAVG")           private double  voteAVG;
    @ColumnInfo(name = "voteCNT")           private int     voteCNT;
    @ColumnInfo(name = "posterID")          private String  posterID;

    @ColumnInfo(name = "title")             private String  title;
    @ColumnInfo(name = "titleORIG")         private String  titleORIG;
    @ColumnInfo(name = "langORIG")          private String  langORIG;
    @ColumnInfo(name = "releasePIT")        private String  releasePIT;
    @ColumnInfo(name = "runtimeVAL")        private String  runtimeVAL;
    @ColumnInfo(name = "tagline")           private String  tagline;
    @ColumnInfo(name = "overview")          private String  overview;
    @ColumnInfo(name = "genres")            private String  genres;

    @ColumnInfo(name = "budgetVAL")         private String  budgetVAL;
    @ColumnInfo(name = "revenueVAL")        private String  revenueVAL;
    @ColumnInfo(name = "homepageURL")       private String  homepageURL;
    @ColumnInfo(name = "imdbID")            private String  imdbID;


    public Movie(int movieID,
                 boolean fav,
                 double popularVAL,
                 double voteAVG,
                 int    voteCNT,
                 String posterID,

                 String title,
                 String titleORIG,
                 String langORIG,
                 String releasePIT,
                 String runtimeVAL,
                 String tagline,
                 String overview,
                 String genres,
                 String budgetVAL,
                 String revenueVAL,
                 String homepageURL,
                 String imdbID
    ){
        this.movieID = movieID;
        this.fav = fav;
        this.popularVAL = popularVAL;
        this.voteAVG = voteAVG;
        this.voteCNT = voteCNT;

        this.title = title;
        this.posterID = posterID;
        this.titleORIG = titleORIG;
        this.langORIG = langORIG;
        this.releasePIT = releasePIT;
        this.runtimeVAL = runtimeVAL;
        this.tagline = tagline;
        this.overview = overview;
        this.genres = genres;
        this.budgetVAL = budgetVAL;
        this.revenueVAL = revenueVAL;
        this.homepageURL = homepageURL;
        this.imdbID = imdbID;
    }


    @Ignore
    public Movie( Movie movie)  {
        this.movieID    = movie.getMovieID();
        this.fav        = movie.isFav();
        this.popularVAL = movie.getPopularVAL();
        this.voteAVG    = movie.getVoteAVG();
        this.voteCNT    = movie.getVoteCNT();
        this.posterID   = movie.getPosterID();

        this.title      = movie.getTitle();
        this.titleORIG  = movie.getTitleORIG();
        this.langORIG   = movie.getLangORIG();
        this.releasePIT = movie.getReleasePIT();
        this.runtimeVAL = movie.getRuntimeVAL();
        this.tagline    = movie.getTagline();
        this.overview   = movie.getOverview();
        this.genres     = movie.getGenres();
        this.budgetVAL  = movie.getBudgetVAL();
        this.revenueVAL = movie.getRevenueVAL();
        this.homepageURL = movie.getHomepageURL();
        this.imdbID     = movie.getImdbID();
    }


    public int getMovieID()        { return this.movieID; }
    public boolean isFav()         { return this.fav; }
    public double getPopularVAL()  { return this.popularVAL; }
    public double getVoteAVG()     { return this.voteAVG; }
    public int getVoteCNT()        { return this.voteCNT; }
    public String getPosterID()    { return this.posterID; }
    @Ignore public String getPosterURL() { return ( getPosterID() != null)  ?   "http://image.tmdb.org/t/p/w185/" + getPosterID()   :   null; }

    public String getTitle()        { return this.title; }
    public String getTitleORIG()    { return this.titleORIG; }
    public String getLangORIG()     { return this.langORIG; }
    public String getReleasePIT()   { return this.releasePIT; }
    public String getRuntimeVAL()   { return this.runtimeVAL; }
    public String getTagline()      { return this.tagline; }
    public String getOverview()     { return this.overview; }
    public String getGenres()       { return this.genres; }
    public String getBudgetVAL()    { return this.budgetVAL; }
    public String getRevenueVAL()   { return this.revenueVAL; }
    public String getHomepageURL()  { return this.homepageURL;}
    public String getImdbID()      { return this.imdbID; }
    @Ignore public String getImdbURL() { return ( getImdbID() != null)  ?   "https://www.imdb.com/title/"  + getImdbID() + "/"   :   null; }


    public void setMovieID(int movieID)             { this.movieID = movieID; }
    public void setFav(boolean fav)                 { this.fav = fav; }
    public void setPopularVAL(double popularVAL)    { this.popularVAL = popularVAL; }
    public void setVoteAVG(double voteAVG)          { this.voteAVG = voteAVG; }
    public void setVoteCNT(int voteCNT)             { this.voteCNT = voteCNT; }
    public void setPosterID(String posterID)        { this.posterID = posterID; }

    public void setTitle(String title)              { this.title = title; }
    public void setTitleORIG(String titleORIG)      { this.titleORIG = titleORIG; }
    public void setLangORIG(String langORIG)        { this.langORIG = langORIG; }
    public void setReleasePIT(String releasePIT)    { this.releasePIT = releasePIT; }
    public void setRuntimeVAL(String runtimeVAL)    { this.runtimeVAL = runtimeVAL; }
    public void setTagline( String tagline)         { this.tagline = tagline; }
    public void setOverview(String overview)        { this.overview = overview; }
    public void setGenres(String genres)            { this.genres = genres; }
    public void setBudgetVAL(String budgetVAL)      { this.budgetVAL = budgetVAL; }
    public void setRevenueVAL(String revenueVAL)    { this.revenueVAL = revenueVAL; }
    public void setHomepageURL(String homepageURL)  { this.homepageURL = homepageURL; }
    public void setImdbID(String imdbID)          { this.imdbID = imdbID; }


    @Ignore public void inverseFav() { setFav( !isFav());}

    @Ignore @NonNull @Override public String toString() { return  "Movie( " + "[movieID]:" + getMovieID() + ", " + "[popularVAL]:" + getPopularVAL() + ", " + "[voteAVG]:" + getVoteAVG() + ", " + "[voteCNT]:" + getVoteCNT() + ", " + "[posterURL]:" + getPosterURL() + ", " + "\t" +  "[title]:" + getTitle() + ", " + "[orig_title]:" + getTitleORIG() + ", " + "[orig_lang]:" + getLangORIG() + ", " + "[released]:" + getReleasePIT() + ", " + "[runtimeVAL]:" + getRuntimeVAL() + ", " + "[tagline]:" + getTagline() + ", " + "[overview]:" + getOverview() + ", " + "[genres]:" + getGenres() + ", " + "[budgetVAL]:" + getBudgetVAL() + ", " + "[revenueVAL]:" + getRevenueVAL() + ", " + "[homepageURL]:" + getHomepageURL() + ", " + "[imdbURL]:" + getImdbURL() + ") \t\t"; }
}
