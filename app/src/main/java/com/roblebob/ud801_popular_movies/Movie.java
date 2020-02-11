package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
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
    @ColumnInfo(name = "posterID")          private String  posterID;

    public Movie(
            int    movieID,          boolean fav,              boolean detailed,
            double popularVAL,       double  voteAVG,          int voteCNT,
            String posterID
    ){
        this.movieID =    movieID;         this.fav =     fav;         this.detailed = detailed;
        this.popularVAL = popularVAL;      this.voteAVG = voteAVG;     this.voteCNT =  voteCNT;
        this.posterID =   posterID;
    }

    @Override public int     hashCode() { return Objects.hash(getMovieID(), isFav(), isDetailed(), getPopularVAL(), getVoteAVG(), getVoteCNT(), getPosterID()); }
    @Override public boolean equals(@Nullable Object obj) {   if ( obj == null) return false;   return this.toString() .equals( obj.toString());    }

    //    @Ignore
//    public Movie( Movie movieBasics) {
//        this.movieID = movieBasics.getMovieID();
//        this.fav= movieBasics.isFav();
//        this.detailed = movieBasics.isDetailed() ;
//        this.popularVAL = movieBasics.getPopularVAL();
//        this.voteAVG = movieBasics.getVoteAVG();
//        this.voteCNT = movieBasics.getVoteCNT();
//        this.posterID = movieBasics.getPosterID();
//    }


    public int     getMovieID()          { return this.movieID; }
    public boolean isFav()               { return this.fav; }
    @Ignore public double getFav()       { return (isFav()) ? 1.0 : 0.0;}
    public boolean isDetailed()          { return this.detailed; }
    public double  getPopularVAL()       { return this.popularVAL; }
    public double  getVoteAVG()          { return this.voteAVG; }
    public int     getVoteCNT()          { return this.voteCNT; }
    public String  getPosterID()         { return this.posterID; }
    @Ignore public String getPosterURL() { return ( getPosterID() != null)  ?   "http://image.tmdb.org/t/p/w185/" + getPosterID()   :   null; }


    public void setMovieID(int movieID)             { this.movieID = movieID; }
    public void setFav(boolean fav)                 { this.fav = fav; }
    public void setDetailed( boolean detailed)      { this.detailed = detailed; }
    public void setPopularVAL(double popularVAL)    { this.popularVAL = popularVAL; }
    public void setVoteAVG(double voteAVG)          { this.voteAVG = voteAVG; }
    public void setVoteCNT(int voteCNT)             { this.voteCNT = voteCNT; }
    public void setPosterID(String posterID)        { this.posterID = posterID; }





    @Ignore public Movie inverseFav() { setFav( !isFav()); return this; }
    @NonNull public String toStringExpanded() { return  "Movie( " + "[movieID]:" + getMovieID() + ", " + "[isFav]:" + isFav() + ", " + "[isDetailed]:" + isDetailed() + ", " + "[popularVAL]:" + getPopularVAL() + ", " + "[voteAVG]:" + getVoteAVG() + ", " + "[voteCNT]:" + getVoteCNT() + ", " + "[posterURL]:" + getPosterURL() + ") \t\t"; }
}
