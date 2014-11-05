
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Fetch Data from PostGRESQL</title>
</head>
<body>
	<form action="HelloServlet">
		Please Enter Query Number: <br> <br>
		<input type="radio" name="query" value="one">Query 1<br> 
		<input type="radio"	name="query" value="two">Query 2<br> 
		<input type="radio" name="query" value="three">Query 3<br> 
		<input type="radio" name="query" value="four">Query 4<br> 
		<input type="radio" name="query" value="five">Query 5<br><br> 
		
		<input type="submit" value="Fetch Results">
	</form>
</body>
</html>