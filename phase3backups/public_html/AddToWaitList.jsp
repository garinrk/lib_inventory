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



<body>

	<%

	String usernameattr = request.getParameter("username");

	if(usernameattr == null){


	%>

	<div class="jumbotron">
	<h1 class="text-center">Add user to waitlist</h1>
</div>
	<form name="AddToWaitList" method=get action="AddToWaitList.jsp">
		Username:<BR>
		<input type=hidden class="form-control" name="username">
		<input type=text class="form-control" name="userValue" onFocus="value=''">
		
		<br>ISBN:<br>
		
	<!-- 	<input type=hidden class="form-control" name="isbn"> -->
		<input type="text" class="form-control" name="isbnValue" onFocus="value=''">
		<br>
		<br>
		<input type=submit class="btn btn-info" value="Add User To WaitList">
	</form>

	<%
	} else {

	String usernameval = request.getParameter("userValue");
	String isbnval = request.getParameter("isbnValue");

	cs5530.Connector conn = new Connector();
	cs5530.Database d = new Database();

	Date today = new Date();

	out.println(d.AddToWaitListWeb(usernameval, isbnval, today, conn.con));

	conn.closeStatement();
	conn.closeConnection();
	}
	%>

	<BR><BR><a href="AddToWaitList.jsp" class="btn btn-primary" role="button">Add another user to a waitlist</a></p>
	</body>
	</html>