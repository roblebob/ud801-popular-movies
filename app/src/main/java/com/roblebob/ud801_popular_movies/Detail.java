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



@Entity(tableName = "Detail", indices = {@Index(value = {"parent","order","index"}, unique = true)})
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
            /* 11 */     "imdb",
            /* 12 */     "video",       // movie trailer
            /* 13 */     "review"
    ));

    @PrimaryKey(autoGenerate = true) private int _id;
    @ColumnInfo(name = "parent")   private int parent;
    @ColumnInfo(name = "order")    private int order;
    @ColumnInfo(name = "index")    private int index;
    @ColumnInfo(name = "content")  private String content;


    public Detail(int _id, int parent, int order, int index, String content) {
        this._id = _id;  this.parent = parent;
        this.order = order;  this.index = index;
        this.content = content;
    }

    @Ignore public Detail( int parent, String order, int index, String content) {
        this.parent = parent;
        this.order = ORDER.indexOf(order);  this.index = index;
        this.content = content;
    }

    public int get_id()        { return this._id; }
    public int getParent()     { return this.parent; }
    public int getOrder()      { return this.order; }
    public int getIndex()      { return this.index; }
    public String getContent() { return this.content; }

    public void set_id(int _id)            { this._id = _id; }
    public void setParent(int parent)      { this.parent = parent; }
    public void setOrder(int order)        { this.order = order; }
    public void setIndex(int index)        { this.index = index; }
    public void setContent(String content) { this.content = content; }

    @Override public boolean equals(@Nullable Object obj) {
        if ( obj == null)  return false;
        return  this.toString() .equals( obj.toString());
    }
    @Override public int hashCode() { return Objects.hash( _id, parent, order, index, content); }
}
