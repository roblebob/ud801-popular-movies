package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "AppState", indices = {@Index(value = {"key"}, unique = true)})
public class AppState {

    @PrimaryKey @NonNull        private String _id;
    @ColumnInfo(name = "key")   private String key;
    @ColumnInfo(name = "value") private String value;

    public AppState(String _id, String key, String value) {
        this._id = _id;
        this.key = key;
        this.value = value;
    }

    @Ignore public AppState(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String get_id()   { return _id; }
    public String getKey()   { return key; }
    public String getValue() { return value; }

    public void set_id(String _id)      { this._id = _id; }
    public void setKey( String key)     { this.key = key; }
    public void setValue( String value) { this.value = value; }
}
