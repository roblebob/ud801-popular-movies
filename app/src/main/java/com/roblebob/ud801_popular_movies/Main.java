package com.roblebob.ud801_popular_movies;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Objects;

@Entity(tableName = "Main")
public class Main {

    @PrimaryKey(autoGenerate = false) private int movieID;
    @ColumnInfo(name = "favorite")    private boolean favorite;
    @ColumnInfo(name = "detailed")    private boolean detailed;
    @ColumnInfo(name = "popularVAL")  private double  popularVAL;
    @ColumnInfo(name = "voteAVG")     private double  voteAVG;
    @ColumnInfo(name = "voteCNT")     private int     voteCNT;
    @ColumnInfo(name = "posterKey")   private String  posterKey;

    public Main( int movieID,
            boolean favorite,  boolean detailed,
            double popularVAL,  double voteAVG,  int voteCNT,
            String posterKey){
        this.movieID = movieID;
        this.favorite = favorite;  this.detailed = detailed;
        this.popularVAL = popularVAL;  this.voteAVG = voteAVG;  this.voteCNT =  voteCNT;
        this.posterKey = posterKey;
    }

    @Override public int     hashCode() { return Objects.hash(  getMovieID(), isFavorite(), isDetailed(), getPopularVAL(), getVoteAVG(),   getVoteCNT(),  getPosterKey()); }
    @Override public boolean equals(@Nullable Object obj) {   if ( obj == null) return false;   return this.toString() .equals( obj.toString());    }


    public int getMovieID()              { return this.movieID; }
    public boolean isFavorite()          { return this.favorite; }
    @ Ignore public int getFavorite()    { return (isFavorite())  ? 1 : 0;}
    public boolean isDetailed()          { return this.detailed; }
    public double  getPopularVAL()       { return this.popularVAL; }
    public double  getVoteAVG()          { return this.voteAVG; }
    public int     getVoteCNT()          { return this.voteCNT; }
    public String getPosterKey()         { return this.posterKey; }
    @Ignore public String getPosterURL() { return ( getPosterKey() != null)  ?   "http://image.tmdb.org/t/p/w185/" + getPosterKey()   :   null; }


    public void setMovieID(int movieID)          { this.movieID = movieID; }
    public void setFavorite(boolean favorite)    { this.favorite = favorite; }
    public void setDetailed(boolean detailed)    { this.detailed = detailed; }
    public void setPopularVAL(double popularVAL) { this.popularVAL = popularVAL; }
    public void setVoteAVG(double voteAVG)       { this.voteAVG = voteAVG; }
    public void setVoteCNT(int voteCNT)          { this.voteCNT = voteCNT; }
    public void setPosterKey(String posterKey)   { this.posterKey = posterKey; }


    @Ignore public Main inverseFavorite()        { this.setFavorite( ! this.isFavorite() ); return this; }
}
