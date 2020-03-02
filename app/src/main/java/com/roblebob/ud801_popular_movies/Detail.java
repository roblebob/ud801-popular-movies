package com.roblebob.ud801_popular_movies;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;



@Entity(tableName = "Detail", indices = {@Index(value = {"movieID","order"}, unique = false)})
public class Detail {

    @Ignore public static final List< String> ORDER = new ArrayList<>( Arrays.asList(
            /*  0 */     "title",
            /*  1 */     "original_title",
            /*  2 */     "original_language",
            /*  3 */     "release_date",
            /*  4 */     "runtime",
            /*  5 */     "tagline",
            /*  6 */     "overview",
            /*  7 */     "genres",
            /*  8 */     "budget",
            /*  9 */     "revenue",
            /* 10 */     "homepage",
            /* 11 */     "imdb_id",
            /* 12 */     "videos",       // ... a single movie trailer, each
            /* 13 */     "reviews"       // ... a single review, each
    ));

    @PrimaryKey(autoGenerate = true)  private int     _ID;
    @ColumnInfo(name = "movieID")     private int     movieID;
    @ColumnInfo(name = "order")       private String  order;
    @ColumnInfo(name = "content")     private String  content;
    @ColumnInfo(name = "link")        private String  link;


    public Detail( int _ID, int movieID, String order, String content, String link) {
        this._ID = _ID;
        this.movieID = movieID;
        this.order = order;
        this.content = content;
        this.link = link;
    }

    @Ignore public Detail( /* int _ID, */ int movieID, String order, String content, String link) {
        this.movieID = movieID;
        this.order = order;
        this.content = content;
        this.link = link;
    }

    public int    get_ID()     { return this._ID; }
    public int    getMovieID() { return this.movieID; }
    public String getOrder()   { return this.order; }
    public String getContent() { return this.content; }
    public String getLink()    { return this.link; }
    @Ignore public String getUrl() {
        if (     getOrder() .equals("homepage")) return getLink();
        else if (getOrder() .equals("imdb_id"))  return "https://www.imdb.com/title/" + getLink();
        else if (getOrder() .equals("videos"))   return "https://www.youtube.com/watch?v=" + getLink();
        else if (getOrder() .equals("reviews"))  return "https://www.themoviedb.org/review/" + getLink();
        return null;
    }

    public void set_ID    ( int    _ID    )  { this._ID     = _ID;     }
    public void setMovieID( int    movieID)  { this.movieID = movieID; }
    public void setOrder  ( String order  )  { this.order   = order;   }
    public void setContent( String content)  { this.content = content; }
    public void setLink   ( String link   )  { this.link    = link;    }

    @Override public boolean equals(@Nullable Object obj) { return (obj != null) && this.toString().equals(obj.toString()); }
    @Override public int hashCode() { return Objects.hash( movieID, order, content, link); }
}
