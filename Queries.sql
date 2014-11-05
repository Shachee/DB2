
/***************************************************************************
CSE532 -- Project 2
File name: DB_Design
Author(s): Shachee Mishra (109915951 )
Phani Krishna Penumarthi (SBU Id )


****************************************************************************/

/*Query 1 - Find all pairs of contestants who happened to audition the same piece during the same show
and got the same score from at least one judge */

Select distinct C1.name , C2.name
from Scores Sc, Scores Sc1 , Contestants C1, Contestants C2
where Sc.Contestant > Sc1.Contestant 
	and 	Sc.Piece = Sc1.Piece 
	and 	Sc.marks = Sc1.marks 
	and 
	(	select judgeId from ShowJ SJ where SJ.sjId = Sc.sjId )
		= 
		(	select judgeId from ShowJ SJ where SJ.sjId = Sc1.sjId) 
		and 	
		(select showId from ShowJ SJ where SJ.sjId = Sc.sjId ) 
		= 
		(select showId from ShowJ SJ where SJ.sjId = Sc1.sjId) 
	and Sc.Contestant = C1.id and Sc1.Contestant = C2.id 






/*Query2 - Find all pairs of contestants who happened to audition the same piece (in possibly different
shows) and got the same average score for that piece.*/


SELECT s1.cont, s2.cont
FROM
(SELECT avg(marks) as av, p.PName as artPiece, c.name as cont
FROM Scores Sc3, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = Sc3.Piece 
AND Sc3.Contestant = c.id
AND Sc3.sjId = sj.sjId
AND sj.showId = dt.Sid
Group By p.PName, c.name, dt.Sid
ORDER BY dt.Sid ) s1,
(SELECT avg(marks) as av, p.PName as artPiece, c.name as cont
FROM Scores Sc3, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = Sc3.Piece 
AND Sc3.Contestant = c.id
AND Sc3.sjId = sj.sjId
AND sj.showId = dt.Sid
Group By p.PName, c.name, dt.Sid
ORDER BY dt.Sid ) s2 
WHERE s1.av = s2.av 
AND s1.artPiece = s2.artPiece
AND s1.cont < s2.cont
ORDER BY s2.cont


/*Query3 - Find all pairs of contestants who auditioned the same piece in (possibly different) shows that
had at least 3 judges and the two contestants got the same highest score */

	SELECT s1.cont, s2.cont
	FROM
	(SELECT max(marks) as max_score, p.PName as artPiece, c.name as cont
	FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
	WHERE p.Pid = s.Piece 
	AND s.Contestant = c.id
	AND s.sjId = sj.sjId
	AND sj.showId = dt.Sid
	Group By p.PName, c.name, dt.Sid
	HAVING COUNT(sj.judgeId) >= 3
	ORDER BY dt.Sid, cont ) s1,
	(SELECT max(marks) as max_score, p.PName as artPiece, c.name as cont
	FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
	WHERE p.Pid = s.Piece 
	AND s.Contestant = c.id
	AND s.sjId = sj.sjId
	AND sj.showId = dt.Sid
	Group By p.PName, c.name, dt.Sid
	HAVING COUNT(sj.judgeId) >= 3
	ORDER BY dt.Sid, cont ) s2 
	WHERE s1.max_score = s2.max_score 
	AND s1.artPiece = s2.artPiece
	AND s1.cont < s2.cont
	ORDER BY s2.cont



/*Query4 - Find all pairs of contestants such that the first contestants has performed all the pieces of the
second contestant (possibly in different shows) */

SELECT DISTINCT record1.cont, record2.cont
FROM
(SELECT DISTINCT c.name as cont, p.PName as form
FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = s.Piece 
AND s.Contestant = c.id
AND s.sjId = sj.sjId
AND sj.showId = dt.Sid
ORDER BY cont
) record1,
(SELECT DISTINCT c.name as cont, p.PName as form
FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = s.Piece 
AND s.Contestant = c.id
AND s.sjId = sj.sjId
AND sj.showId = dt.Sid
ORDER BY cont
) record2 
WHERE record1.cont != record2.cont
AND 
(
(SELECT COUNT(*) as c_intersect FROM(
(SELECT DISTINCT form from (SELECT DISTINCT c.name as cont, p.PName as form
FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = s.Piece 
AND s.Contestant = c.id
AND s.sjId = sj.sjId
AND sj.showId = dt.Sid
ORDER BY cont
) r1
where r1.cont = record1.cont)
INTERSECT 
(SELECT DISTINCT form from (SELECT DISTINCT c.name as cont, p.PName as form
FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = s.Piece 
AND s.Contestant = c.id
AND s.sjId = sj.sjId
AND sj.showId = dt.Sid
ORDER BY cont
) r2 
where r2.cont = record2.cont)
) r)
>=
(SELECT count(*) as c_r2 FROM 
(SELECT DISTINCT form from (SELECT DISTINCT c.name as cont, p.PName as form
FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt
WHERE p.Pid = s.Piece 
AND s.Contestant = c.id
AND s.sjId = sj.sjId
AND sj.showId = dt.Sid
ORDER BY cont
) r2 
where r2.cont = record2.cont) r)
)
ORDER BY record1.cont



/*Query5 - Find all chained co-auditions. A chained co-auditions is the transitive closure of the following
binary relation: X and Y (directly) co-auditioned iff they both performed the same piece in the
same show and got the same score from at least one (same) judge. Thus, a chained co-audition
can be either a direct or an indirect co-audition.*/

create or replace recursive view IndirectChain(Cont1,Cont2) As
Select distinct C1.name , C2.name
from Scores Sc, Scores Sc1 , Contestants C1, Contestants C2
where Sc.Contestant > Sc1.Contestant and Sc.Piece = Sc1.Piece and Sc.marks = Sc1.marks and 
(select judgeId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select judgeId from ShowJ SJ where SJ.sjId = Sc1.sjId) 
and (select showId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select showId from ShowJ SJ where SJ.sjId = Sc1.sjId) 
and Sc.Contestant = C1.id and Sc1.Contestant = C2.id 
Union
Select D.C1name, I.Cont2 
from (Select distinct C1.name as C1name, C2.name as C2name
from Scores Sc, Scores Sc1 , Contestants C1, Contestants C2
where Sc.Contestant > Sc1.Contestant and Sc.Piece = Sc1.Piece and Sc.marks = Sc1.marks and 
(select judgeId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select judgeId from ShowJ SJ where SJ.sjId = Sc1.sjId) 
and (select showId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select showId from ShowJ SJ where SJ.sjId = Sc1.sjId) 
and Sc.Contestant = C1.id and Sc1.Contestant = C2.id 
) D, IndirectChain I 
where D.C2name = I.Cont1

Select * from IndirectChain;
