
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My first JSP</title>
</head>
<body>
	<form action="HelloServlet">
		Please enter Query Number: <br> <input type="radio" name="query"
			value="Query1">Query 1<br> <input type="radio"
			name="query" value="Query2">Query 2<br> <input
			type="radio" name="query" value="Query3">Query 3<br> <input
			type="radio" name="query" value="Query4">Query 4<br> <input
			type="radio" name="query" value="Query5">Query 5<br> <input
			type="submit" value="fetchValues">
	</form>
</body>
</html>