<%@ page language="java" import="cs5530.*" %>
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
<h1 class="text-center">Add Book Copy</h1>
</div>

<body>

	<% String isbnattr = request.getParameter("isbn");

	if(isbnattr	== null){

	%>
	<form name="AddBookCopy" method=get action="AddCopy.jsp">
		<br>
		ISBN:
		<br>
		<input type=hidden  class="form-control"  name="isbn">
		<input type="text"  class="form-control" name="isbnValue">
		<br>
		Number of copies:
		<br>
		<input type="text" class="form-control" name="copiesValue">
		<br>
		<br>
		<input type=submit class="btn btn-info" value="Add Copies">

	</form>

	<% } else {
	

	String isbn = request.getParameter("isbnValue");
	String amount = request.getParameter("copiesValue");

	if(isbn != "" && amount != ""){
	cs5530.Connector conn = new Connector();
	out.println(cs5530.Database.AddBookCopyWeb(isbn, amount, conn.con));



	conn.closeStatement();
	conn.closeConnection();	
	}
	else{out.println("<BR><BR><h3>No empty fields, please try again</h3>");
		}
}
	%>

<a href="AddCopy.jsp" class="btn btn-primary" role="button">Add another copy of a book</a></p>
<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>
</body>
</html>