package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MovieExtra")
public class MovieExtra {

    @PrimaryKey(autoGenerate = true)        private int _ID;
    @ColumnInfo(name = "MID")               private int MID;
    @ColumnInfo(name = "type")              private String type;
    @ColumnInfo(name = "name")              private String name;
    @ColumnInfo(name = "url")               private String url;
    @ColumnInfo(name = "additions")         private String additions;


    public MovieExtra(int _ID, int MID, String type, String name, String url, String additions) { this._ID = _ID; this.MID = MID; this.type = type; this.name = name; this.url = url; this.additions = additions; }
    @Ignore
    public MovieExtra(         int MID, String type, String name, String url, String additions) {                 this.MID = MID; this.type = type; this.name = name; this.url = url; this.additions = additions; }


    public int get_ID()             { return this._ID; }
    public int getMID()             { return this.MID; }
    public String getType()         { return this.type; }
    public String getName()         { return this.name; }
    public String getUrl()          { return this.url; }
    public String getAdditions()    { return this.additions; }

    public void set_ID( int _ID)                { this._ID = _ID; }
    public void setMID( int MID)                { this.MID = MID; }
    public void setType( String type)           { this.type = type; }
    public void setName( String name)           { this.name = name; }
    public void setUrl( String url)             { this.url = url; }
    public void setAdditions( String additions) { this.additions = additions; }


    @Ignore
    @NonNull
    @Override
    public String toString() {
        return  "MovieExtra(  " +
                "[_ID]:"        +   get_ID()        + ",  " +
                "[MID]:"        +   getMID()        + ",  " +
                "[type]:"       +   getType()       + ",  " +
                "[name]:"       +   getName()       + ",  " +
                "[url]:"        +   getUrl()        + ",  " +
                "[additions]:"  +   getAdditions()  + "   " +
                ") \t \t ";
    }
}
