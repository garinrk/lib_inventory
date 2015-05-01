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
<h1 class="text-center">Checking out a book...</h1>
</div>

<body>

	<% String usernameattr = request.getParameter("username");

	if(usernameattr	== null){

	%>
	<form role="form" name="CheckoutBook" method=get action="Checkout.jsp">
		Username:<BR>
		<input type=hidden class="form-control" name="username">
		<input type=text class="form-control" name="userValue" value="simp123" onFocus="value=''">
		
		<br>ISBN of book to checkout:<br>
		
		<input type=hidden name="isbn">
		<input type="text" class="form-control" name="isbnValue" value="1122334455667788" onFocus="value=''">
		<br>
		<br>
		<input type=submit class="btn btn-info"  value="Checkout Book">
	</form>

	<%
	} else {

	String usernameval = request.getParameter("userValue");
	String isbnval = request.getParameter("isbnValue");

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	Date today = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");

	if(isbnval != "" || usernameval != ""){
		out.println(d.CheckoutBookWeb(usernameval, isbnval, today, conn.con));
	}
	else
	{
		out.println("No empty fields, please try again");
	}
	

	conn.closeStatement();
	conn.closeConnection();
	}
	%>
<br>
<a href="Checkout.jsp"  class="btn btn-primary" role="button">Checkout another book</a></p>
</body>
</html>