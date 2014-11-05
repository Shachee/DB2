import java.io.IOException; 

import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 

import java.io.PrintWriter; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloWorld extends HttpServlet { 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		// reading the user input 
		String color=  "brown";
		String query = request.getParameter("query"); 
		PrintWriter out = response.getWriter(); 
		
		String result = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
				+ "<html> \n" + "<head> \n" 
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n" 
		+ "<title> Fetched Results From Database </title> \n" + "</head> \n" + "<body> \n" + "<font size=\"3px\" color=\"" 
				+ color + "\">"; 
		
		int queryId = -1;
		
		switch(query ) {
			case "one": queryId = 1;
						break;
			case "two": queryId = 2;
						break;
			case "three": queryId = 3;
						break;
			case "four": queryId = 4;
						break;
			case "five": queryId = 5;
						break;
		}
		
		String resultSet = RunQuery(queryId);
		
		out.println ( result  
		+ "Query Results ::" + resultSet + "</font> \n" + "</body> \n" + "</html>" );
	}
	
	private String RunQuery(int id) {
		ResultSet rs = null;
		String resultSet = "<table align=\"center\" border=\"1\">";
		
		Connection c = null;
		Statement stmt = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://localhost/cse532",
	            "postgres", "123456");
		     System.out.println("Opened database successfully");	         
	         
		     stmt = c.createStatement();
	         String sql = "select Id as name1, Name as name2 from Judges;";
	         
	         
	         switch(id) {
	         
	         case 1:
	        	 sql = "Select distinct C1.name as name1, C2.name as name2 from Scores Sc, Scores Sc1 , Contestants C1, Contestants C2 where Sc.Contestant > Sc1.Contestant and Sc.Piece = Sc1.Piece and Sc.marks = Sc1.marks and (select judgeId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select judgeId from ShowJ SJ where SJ.sjId = Sc1.sjId) and (select showId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select showId from ShowJ SJ where SJ.sjId = Sc1.sjId) and Sc.Contestant = C1.id and Sc1.Contestant = C2.id; ";
	        	 break;
	         case 2:
	        	 sql = "SELECT s1.cont as name1, s2.cont as name2 FROM (SELECT avg(marks) as av, p.PName as artPiece, c.name as cont FROM Scores Sc3, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = Sc3.Piece  AND Sc3.Contestant = c.id AND Sc3.sjId = sj.sjId AND sj.showId = dt.Sid Group By p.PName, c.name, dt.Sid ORDER BY dt.Sid ) s1, (SELECT avg(marks) as av, p.PName as artPiece, c.name as cont FROM Scores Sc3, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = Sc3.Piece  AND Sc3.Contestant = c.id AND Sc3.sjId = sj.sjId AND sj.showId = dt.Sid Group By p.PName, c.name, dt.Sid ORDER BY dt.Sid ) s2  WHERE s1.av = s2.av  AND s1.artPiece = s2.artPiece AND s1.cont < s2.cont ORDER BY s2.cont ";
	        	 break;
	         case 3:
	        	 sql = "SELECT s1.cont as name1, s2.cont as name2 FROM (SELECT max(marks) as max_score, p.PName as artPiece, c.name as cont FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid Group By p.PName, c.name, dt.Sid HAVING COUNT(sj.judgeId) >= 3 ORDER BY dt.Sid, cont ) s1, (SELECT max(marks) as max_score, p.PName as artPiece, c.name as cont FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid Group By p.PName, c.name, dt.Sid HAVING COUNT(sj.judgeId) >= 3 ORDER BY dt.Sid, cont ) s2 WHERE s1.max_score = s2.max_score AND s1.artPiece = s2.artPiece AND s1.cont < s2.cont ORDER BY s2.cont";
	        	 break;
	         case 4:
	        	 sql = "SELECT DISTINCT record1.cont as name1, record2.cont as name2 FROM (SELECT DISTINCT c.name as cont, p.PName as form FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid ORDER BY cont ) record1, (SELECT DISTINCT c.name as cont, p.PName as form FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid ORDER BY cont ) record2 WHERE record1.cont != record2.cont AND ( (SELECT COUNT(*) as c_intersect FROM( (SELECT DISTINCT form from (SELECT DISTINCT c.name as cont, p.PName as form FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid ORDER BY cont ) r1 where r1.cont = record1.cont) INTERSECT (SELECT DISTINCT form from (SELECT DISTINCT c.name as cont, p.PName as form FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid ORDER BY cont ) r2 where r2.cont = record2.cont) ) r) >= (SELECT count(*) as c_r2 FROM (SELECT DISTINCT form from (SELECT DISTINCT c.name as cont, p.PName as form FROM Scores s, Pieces p, Contestants c, ShowJ sj, ShowDet dt WHERE p.Pid = s.Piece AND s.Contestant = c.id AND s.sjId = sj.sjId AND sj.showId = dt.Sid ORDER BY cont ) r2 where r2.cont = record2.cont) r) ) ORDER BY record1.cont ";	        	 
	        	 break;
	         case 5:
	        	 sql = "create or replace recursive view IndirectChain(Cont1,Cont2) As Select distinct C1.name , C2.name from Scores Sc, Scores Sc1 , Contestants C1, Contestants C2 where Sc.Contestant > Sc1.Contestant and Sc.Piece = Sc1.Piece and Sc.marks = Sc1.marks and (select judgeId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select judgeId from ShowJ SJ where SJ.sjId = Sc1.sjId) and (select showId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select showId from ShowJ SJ where SJ.sjId = Sc1.sjId) and Sc.Contestant = C1.id and Sc1.Contestant = C2.id Union Select D.C1name, I.Cont2 from (Select distinct C1.name as C1name, C2.name as C2name from Scores Sc, Scores Sc1 , Contestants C1, Contestants C2 where Sc.Contestant > Sc1.Contestant and Sc.Piece = Sc1.Piece and Sc.marks = Sc1.marks and (select judgeId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select judgeId from ShowJ SJ where SJ.sjId = Sc1.sjId) and (select showId from ShowJ SJ where SJ.sjId = Sc.sjId ) = (select showId from ShowJ SJ where SJ.sjId = Sc1.sjId) and Sc.Contestant = C1.id and Sc1.Contestant = C2.id ) D, IndirectChain I where D.C2name = I.Cont1;";
	        	 stmt.executeUpdate(sql);
	        	 sql = "Select cont1 as name1, cont2 as name2 from IndirectChain; ";
	         }
	         System.out.println("Query Given is "+id);
	         rs = stmt.executeQuery(sql);
	         
	         resultSet += "<tr>";
	         resultSet += "<th>Contestant Name</th>";
	         resultSet += "<th>Contestant Name</th>";
	         resultSet += "</tr>";
	         while ( rs.next() ) {
	        	 resultSet += "<tr>";
	             String sid = rs.getString("name1");
	             String  sname = rs.getString("name2");
	             resultSet += "<td>"+sid+"</td>";
	             resultSet += "<td>"+sname+"</td>";
	             resultSet += "</tr>";	             
	          }
	         /*
	          * 
	          * if (id == 5) {
	        	 sql = "drop view IndirectChain";
	        	 stmt.executeUpdate(sql);
	         *}
	          */
	          rs.close();
	          stmt.close();
	          c.close();	         
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	      System.out.println("Operation done successfully");
	      
	      resultSet += " </table>";

	    return resultSet;
	}
}