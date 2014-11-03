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
		String color=  "Red";
		String query = request.getParameter("query"); 
		PrintWriter out = response.getWriter(); 
		
		String result = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
				+ "<html> \n" + "<head> \n" 
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n" 
		+ "<title> My first jsp </title> \n" + "</head> \n" + "<body> \n" + "<font size=\"12px\" color=\"" 
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
		+ "Query Results:::" + resultSet + "</font> \n" + "</body> \n" + "</html>" );		
		
		
	}
	
	private String RunQuery(int id) {
		ResultSet rs = null;
		String resultSet = "<table>";
		
		Connection c = null;
		Statement stmt = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager
	            .getConnection("jdbc:postgresql://localhost/cse532",
	            "postgres", "123456");
		     System.out.println("Opened database successfully");	         
	         
		     stmt = c.createStatement();
	         String sql = "select * from Judges;";
	         rs = stmt.executeQuery(sql);
	         
	         while ( rs.next() ) {
	        	 resultSet += "<tr>";
	             int sid = rs.getInt("id");
	             String  sname = rs.getString("name");
	             resultSet += "<th>"+sid+"</th>";
	             resultSet += "<th>"+sname+"</th>";
	             resultSet += "</tr>";	             
	          }
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