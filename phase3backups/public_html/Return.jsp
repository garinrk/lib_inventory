<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>

<!-- 

Author: Garin Richards
For Phase 3 of Semester Project
CS 5530 - Database Systems - University of Utah
Spring 2015

-->

<head>
	<link rel="stylesheet" type="text/css" href="bootstrap.css" />

	<meta name="viewport" content="width=device-width, initial-scale=1">

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>


</head>

<div class="jumbotron">
	<h1 class="text-center">Return a Book</h1>
</div>


<body>

	<%

	String usernameattr = request.getParameter("username");

	if(usernameattr == null){


	%>
	<form role="form" name="ReturnBook" method=get action="Return.jsp">
		Username:<BR>
		<input type=hidden  class="form-control" name="username">
		<input type=text  class="form-control" name="userValue" onFocus="value=''">

		<br>ISBN:<br>

		<input type=hidden  class="form-control" name="isbn">
		<input type="text"  class="form-control" name="isbnValue" onFocus="value=''">

		<br>Month<br>
		<input type="text"  class="form-control" name="monthValue" onFocus="value=''">
		Day
		<input type="text"  class="form-control" name="dayValue"  onFocus="value=''"> Year
		<input type="text"  class="form-control" name="yearValue" onFocus="value=''">
		<br>Mark book as:
		<input type="radio"   name=lostvalue value="0" checked/>Returned
		<input type="radio"  name=lostvalue value="1"/>Lost


		<br>
		<input type=submit class="btn btn-info" value="Return Book">
	</form>

	<%
} else {

String usernameval = request.getParameter("userValue");
String isbnval = request.getParameter("isbnValue");
String lost = request.getParameter("lostvalue");
String year = request.getParameter("yearValue");
String month = request.getParameter("monthValue");
String day = request.getParameter("dayValue");
String returnDate = year + "-" + month + "-" + day;
boolean isLost = false;

cs5530.Connector conn = new Connector();


if(usernameval != "" && isbnval != "" && lost != "" && year != "" && month != "" && day != "" ){

if(lost.equals("1"))
	isLost = true;
else
	isLost = false;

out.println(cs5530.Database.ReturnBookWeb(usernameval, isbnval, isLost, returnDate, conn.con));

if(isLost)
{
	out.println("Book marked as lost");
}

conn.closeStatement();
conn.closeConnection();
}
else
{
	out.println("<BR><BR><h3>No empty fields, please try again</h3>");

	conn.closeStatement();
	conn.closeConnection();
}
}
%>

<a class="btn btn-primary" href="Return.jsp">Return another book</a></p>

<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>



</body>
</html>