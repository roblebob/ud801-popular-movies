# ud801: Popular Movies


&nbsp;&nbsp;


The here presented solution devides all data associated with a particular movie
(accessible using "www.moviedb.org")
into
- one __Main__
- multiple __Detail__ s

each is stored separately within its own table.
There is a third kind of data having its own table, and not related to any movie, but to the users interactions, called __AppState__, containing only simple key-value pairs.


&nbsp;&nbsp;


The __Main__ only contains:
- ___movieID___ , an INTEGER
- ___popular___ , a DOUBLE denoting the popularity
- ___voteAVG___ , a DOUBLE  denoting the vote average
- ___voteCNT___ , an INTEGER denoting the vote count
- ___posterID___ , a STRING, presuming with no more than 32 chars

as well as the following user interaction specific
- ___isFavorite___ , a BOOLEAN flag denoting the users attitude towards the movie (initially set to FALSE)
- ___hasDetails___ , a BOOLEAN flag denoting if the user has already been there, and therefore is accessible in offline case (initially set to FALSE)

For a rough estimate of the worst case scenario, presuming a granularity of 8 bytes (6 * 8 bytes =  48 bytes), and for the STRING: 32 x (2 bytes) = 64 bytes, gives __112__ bytes total.
Furthermore, presuming about __12 000__ movies gives a little over one mega bytes (1 344 000 bytes) plus some overhead -- definitely no more than __1.5 mb__ .
This is why initially, a background thread is started, downloading the Mains of all movies accessible.


&nbsp;&nbsp;


Each __Detail__ contains:
- ___ID__
- __movieID__
- __context__ , a STRING indexed by __CONTEXTs__
- __content__ , a STRING, denotation context-sensitive
- __link__ , a STRING, denoting a link fragment, whereas its counterpart is context-sensitive

the __context__ is indexed in the following sense:
0. "title",
1. "original_title",
2. "original_language",
3. "release_date",
4. "runtime",
5. "tagline",
6. "overview",
7. "genres",
8. "budget",
9. "revenue",
10. "homepage",
11. "imdb_id",
12. "videos",
13. "reviews"


&nbsp;&nbsp;




As shown in the


[overview](https://docs.google.com/presentation/d/1fsxVuD4k3UhCbKSODCukvlmV_juS_78vWPYRxkAHlM4/preview)
