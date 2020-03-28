# ud801: Popular Movies


&nbsp;&nbsp;


The here presented solution devides all data associated with a particular movie
(accessible using "www.moviedb.org")
into
- one __Main__
- multiple __Detail__ s

each is stored separately within its own table.


&nbsp;&nbsp;


The __Main__ only contains:
- ___movieID___ , an INTEGER
- ___popular___ , a DOUBLE denoting the popularity
- ___voteAVG___ , a DOUBLE  denoting the vote average
- ___voteCNT___ , an INTEGER denoting the vote count
- ___posterID___ , a STRING, presuming with no more than 32 chars

as well as the following user interaction specific
- ___isFavorite___ , a BOOLEAN flag denoting the users attitude towards the movie (initially set to FALSE)

Presuming a granularity of 8 bytes, and for the STRING: 32 x (2 bytes) = 64 bytes, gives __104__ bytes.
Furthermore, presuming about __12 000__ movies gives a little over one mega bytes plus some overhead -- definitely no more than __1.5 mega__ bytes.
(This rough estimate should also hold when presuming a granularity of 8 bytes.)
This is why initially a background thread is started, downloading all Mains, accessible.


&nbsp;&nbsp;


Each __Detail__ contains:
- __movieID__


&nbsp;&nbsp;


There is a third kind of data not related to any movie, but to the users interactions, called __AppState__.
It only contains:
- ___Key___ , a STRING
- ___Value___ , a STRING


&nbsp;&nbsp;

&nbsp;&nbsp;

&nbsp;&nbsp;


As shown in the


[overview](https://docs.google.com/presentation/d/1fsxVuD4k3UhCbKSODCukvlmV_juS_78vWPYRxkAHlM4/preview)
