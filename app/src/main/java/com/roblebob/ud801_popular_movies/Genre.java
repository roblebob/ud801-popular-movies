package com.roblebob.ud801_popular_movies;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Genre")
public class Genre {

    @PrimaryKey(autoGenerate = false)       private int id;
    @ColumnInfo(name = "label")             private String label;

    Genre(int id, String label) { this.id = id; this.label = label; }

    public int getId()                  { return id; }
    public String getLabel()            { return label; }

    public void setId(int id)           { this.id = id; }
    public void setLabel(String label)  { this.label = label; }


}
