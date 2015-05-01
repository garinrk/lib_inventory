<%@ page language="java" import="cs5530.*, java.util.*, java.text.SimpleDateFormat"%>
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
	<h1 class="text-center">Checkout a Book</h1>
</div>

<body>

	<% String usernameattr = request.getParameter("username");

	if(usernameattr	== null){

	%>
	<form role="form" name="CheckoutBook" method=get action="Checkout.jsp">
		Username:<BR>
		<input type=hidden class="form-control" name="username">
		<input type=text class="form-control" name="userValue" >

		<br>ISBN of book to checkout:<br>

		<input type=hidden name="isbn">
		<input type="text" class="form-control" name="isbnValue" >
		<br>
		<br>
		<input type=submit class="btn btn-info"  value="Checkout Book">
	</form>

	<%
} else {

String usernameval = request.getParameter("userValue");
String isbnval = request.getParameter("isbnValue");

cs5530.Connector conn = new Connector();

Date today = new Date();
SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");

if(isbnval != "" && usernameval != ""){
out.println(cs5530.Database.CheckoutBookWeb(usernameval, isbnval, today, conn.con));
}
else
{
	out.println("<BR><BR><h3>No empty fields, please try again</h3>");
}


conn.closeStatement();
conn.closeConnection();
}
%>

<a href="Checkout.jsp"  class="btn btn-primary" role="button">Checkout another book</a></p>
<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>
</body>
</html>