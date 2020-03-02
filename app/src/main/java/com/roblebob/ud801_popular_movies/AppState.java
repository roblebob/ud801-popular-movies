package com.roblebob.ud801_popular_movies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "AppState" /*, indices = {@Index(value = {"key"}, unique = true)}*/)
public class AppState {

    @PrimaryKey  @NonNull               private String key;
    @ColumnInfo(name = "value")         private String value;

    public AppState( @NonNull String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey()   { return key; }
    public String getValue() { return value; }

    public void setKey( String key)     { this.key = key; }
    public void setValue( String value) { this.value = value; }
}
