package com.roblebob.ud801_popular_movies;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Movie")
public class Movie {

    @PrimaryKey(autoGenerate = false)       private int     movieID;
    @ColumnInfo(name = "fav")               private boolean fav;
    @ColumnInfo(name = "hasDetails")        private boolean detailed;
    @ColumnInfo(name = "popularVAL")        private double  popularVAL;
    @ColumnInfo(name = "voteAVG")           private double  voteAVG;
    @ColumnInfo(name = "voteCNT")           private int     voteCNT;
    @ColumnInfo(name = "key")               private String  key;

    public Movie(
            int movieID,          boolean fav,              boolean detailed,
            double popularVAL,    double  voteAVG,          int voteCNT,
            String key
    ){
        this.movieID =    movieID;         this.fav =     fav;         this.detailed = detailed;
        this.popularVAL = popularVAL;      this.voteAVG = voteAVG;     this.voteCNT =  voteCNT;
        this.key = key;
    }

    @Override public int     hashCode() { return Objects.hash(getMovieID(), isFav(), isDetailed(), getPopularVAL(), getVoteAVG(),   getVoteCNT(),  getKey()); }
    @Override public boolean equals(@Nullable Object obj) {   if ( obj == null) return false;   return this.toString() .equals( obj.toString());    }

    public int     getMovieID()          { return this.movieID; }
    public boolean isFav()               { return this.fav; }
    @Ignore public double getFav()       { return (isFav()) ? 1.0 : 0.0;}
    public boolean isDetailed()          { return this.detailed; }
    public double  getPopularVAL()       { return this.popularVAL; }
    public double  getVoteAVG()          { return this.voteAVG; }
    public int     getVoteCNT()          { return this.voteCNT; }
    public String getKey()         { return this.key; }
    @Ignore public String getPosterURL() { return ( getKey() != null)  ?   "http://image.tmdb.org/t/p/w185/" + getKey()   :   null; }

    public void setMovieID(int movieID)             { this.movieID = movieID; }
    public void setFav(boolean fav)                 { this.fav = fav; }
    public void setDetailed( boolean detailed)      { this.detailed = detailed; }
    public void setPopularVAL(double popularVAL)    { this.popularVAL = popularVAL; }
    public void setVoteAVG(double voteAVG)          { this.voteAVG = voteAVG; }
    public void setVoteCNT(int voteCNT)             { this.voteCNT = voteCNT; }
    public void setKey(String key)                  { this.key = key; }

    @Ignore public Movie inverseFav() { setFav( !isFav()); return this; }



}
