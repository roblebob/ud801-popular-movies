package com.roblebob.ud801_popular_movies;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *  The Xtra class is ment to hold all the augmentations of the Movie class, and also one movie
 *  independent value (extraID=1), the API_KEY itself.
 *  In general each Movie has multiple Xtra entries, even some with variable sizes.
 *  To guarantee a unique and determined extraID, the movieID is concatenated with a particular subID
 *  (2 digits).
 *
 */
@Entity(tableName = "Xtra")
public class Xtra {


    @Ignore public static final List< String> ATTRIBUTE_list =    new ArrayList< String>( Arrays.asList(

            /* 0 */   /* 1 */           /* 2 */              /* 3 */         /* 4 */    /* 5 */    /* 6 */    /* 7 */   /* 8 */     /* 9 */
            "title", "original_title", "original_language", "release_date", "runtime", "tagline", "overview", "genres", "budget",   "revenue",
             null,    null,             null,                null,           null,      null,      null,       null,    "homepage", "imdb_key"
                                                                                                                         /* 18 */    /* 19 */
    ));

    @Ignore public static final List< String> multi_ATTRIBUTE_list =  new ArrayList< String>( Arrays.asList( "videos", "reviews"));

    @PrimaryKey(autoGenerate = false)      private int extraID;
    @ColumnInfo(name = "value")            private String value;


    /***********************************************************************************************
     * c'tor:   A P I   K E Y
     */
    @Ignore public Xtra(String API_KEY) {
        this.extraID = 1;
        this.value = API_KEY;
    }

    /***********************************************************************************************
     * c'tor:  S T A N D A R D
     */
    public Xtra(int extraID, String value) {
        this.extraID = extraID;
        this.value = value;
    }

    /***********************************************************************************************
     * c'tor:   for all single but movie dependent attributes.
     *          extraID is the concatenation of movieID  +  subID (2 digits),  cast back to an Integer.
     *          0 <= subID < 20    (... as integer of cause)
     *
     * @param movieID
     * @param ATTRIBUTE  see above: ATTRIBUTES_singles_LIST
     * @param VALUE
     */
    @Ignore public Xtra(int movieID, String ATTRIBUTE, String VALUE ) {
        final int index = ATTRIBUTE_list.indexOf( ATTRIBUTE);
        final String subID =  String.format ("%02d", index);
        this.extraID = Integer.parseInt(movieID + subID);
        this.value = VALUE;
    }


    /**
     * c'tor:   for all multiple movie dependent attributes.
     *          extraID is the concatenation of movieID  +  subID (2 digits),  cast back to an Integer.
     *          (array of unknown size, max 20 each)
     *          videos:   20 <= subID < 39
     *          reviews:  40 <= subID < 60
     *
     * @param movieID
     * @param ATTRIBUTE
     * @param i
     * @param VALUE
     */
    @Ignore public Xtra( int movieID, String ATTRIBUTE, int i, String VALUE ) {
        switch (ATTRIBUTE) {
            case ("videos"):     this.extraID = Integer.parseInt(    String.valueOf(movieID) + (20 + i));   break;
            case ("reviews"):    this.extraID = Integer.parseInt(    String.valueOf(movieID) + (40 + i));   break;
        }
        this.value = VALUE;
    }












    /**
     * Getter, Setter ...
     */
    public int getExtraID() {
        return this.extraID;
    }
    public String getValue() {
        return this.value;
    }
    public void setExtraID(int extraID) {
        this.extraID = extraID;
    }
    public void setValue(String value) {
        this.value = value;
    }




    @Ignore public static String youtubeUrl( final String youtubeKey) { return "https://www.youtube.com/watch?v=" + youtubeKey; }


}
