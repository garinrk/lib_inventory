<%@ page language="java" import="cs5530.*" %>
<html>

<!-- 

Author: Garin Richards
For Phase 3 of Semester Project
CS 5530 - Database Systems - University of Utah
Spring 2015

-->


<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<link rel="stylesheet" type="text/css" href="bootstrap.css" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>


<body>

	<%

	String usernameattr = request.getParameter("username");

	if(usernameattr == null)
	{

	%>


	<div class="jumbotron">
		<h1 class="text-center">Get User Record</h1>
	</div>

	<form name="UserLookup" method=get  action="PrintUserRecord.jsp">
		Please enter a username:<br>

		<input type=hidden name="username">
		<input class="form-control" type=text name="userValue" onFocus="value=''">

		<br><br>
		<input type=submit class="btn btn-info" value="Lookup User Record">
	</form>

	<%
	} else {

String usernameval = request.getParameter("userValue");

cs5530.Connector conn = new Connector();

if(usernameval != ""){
//call function
out.println(cs5530.Database.PrintUserRecordWeb(usernameval, conn.con));


conn.closeStatement();
conn.closeConnection();
}
else
{
	out.println("<BR><BR><h3>No empty fields, please try again</h3>");
}
}
%>

<a href="PrintUserRecord.jsp" class="btn btn-primary" role="button">Lookup Another User</a>
<div class="text-center"> 
		<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
		</div>
</body>
</html>