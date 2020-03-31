package com.roblebob.ud801_popular_movies;
import android.util.Log;

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

@Entity(tableName = "Detail", indices = {@Index(value = {"movieID","context","content","link"}, unique = false)})
public class Detail {

    @Ignore public static final List< String> CONTEXTs = new ArrayList<>( Arrays.asList(
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

    @PrimaryKey(autoGenerate = false) private int    _ID;
    @ColumnInfo(name = "movieID")     private int    movieID;
    @ColumnInfo(name = "context")     private String context;
    @ColumnInfo(name = "content")     private String content;
    @ColumnInfo(name = "link")        private String link;


    public Detail( /* int _ID, */ int movieID, String context, String content, String link) {
        this.movieID = movieID;
        this.context = context;
        this.content = content;
        this.link = link;

        this._ID = hashCode();
    }

    public int    get_ID()     { return this._ID; }
    public int    getMovieID() { return this.movieID; }
    public String getContext() { return this.context; }
    public String getContent() { return this.content; }
    public String getLink()    { return this.link; }
    @Ignore public String getUrl() {
        switch (getContext()) {
            case "homepage" : return getLink();
            case "imdb_id"  : return "https://www.imdb.com/title/" + getLink();
            case "videos"   : return "https://www.youtube.com/watch?v=" + getLink();
            case "reviews"  : return "https://www.themoviedb.org/review/" + getLink();
            default: return null;
        }
    }

    public void set_ID    ( int    _ID    )  { this._ID     = _ID;     }
    public void setMovieID( int    movieID)  { this.movieID = movieID; }
    public void setContext(String context)  { this.context = context;   }
    public void setContent( String content)  { this.content = content; }
    public void setLink   ( String link   )  { this.link    = link;    }

    @Override public boolean equals(@Nullable Object obj) { return (obj != null) && this.toString().equals(obj.toString()); }
    @Override public int hashCode() { return Objects.hash( movieID, context, content, link); }
}
