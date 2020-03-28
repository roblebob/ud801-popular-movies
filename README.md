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

Presuming a granularity of 8 bytes, and for the STRING: 32 x (2 bytes) = 64 bytes, gives __104__ bytes.
Furthermore, presuming about __12 000__ movies gives a little over one mega bytes plus some overhead -- definitely no more than __1.5 mega__ bytes.
This is why initially a background thread is started, downloading all Mains, accessible.


&nbsp;&nbsp;


Each __Detail__ contains:
- ___ID__
- __movieID__
- __order

&nbsp;&nbsp;




&nbsp;&nbsp;

&nbsp;&nbsp;

&nbsp;&nbsp;


As shown in the


[overview](https://docs.google.com/presentation/d/1fsxVuD4k3UhCbKSODCukvlmV_juS_78vWPYRxkAHlM4/preview)
