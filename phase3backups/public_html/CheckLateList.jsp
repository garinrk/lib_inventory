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
<h1 class="text-center">Check Late List</h1>
</div>



<body>
	<% String monthattr = request.getParameter("month");

	if(monthattr == null){


	%>

	<form name="CheckLateList" method=get action="CheckLateList.jsp">
		Month
		<input type=hidden class="form-control" name="month">
		<input type="text" class="form-control" name="monthValue">
		Day
		<input type="text" class="form-control" name="dayValue">
		Year
		<input type="text" class="form-control" name="yearValue">
		<br>
		<br>
		<input type=submit class="btn btn-info" value="Get List of late books">
	</form>

	<% } else {

	String month = request.getParameter("monthValue");
	String day = request.getParameter("dayValue");
	String year = request.getParameter("yearValue");

	if(month != "" && day != "" && year != ""){
	cs5530.Connector conn = new Connector();

	if(month == "" || day == "" || year == "") {
		out.println("No empty fields, please try again.");
		conn.closeStatement();
		conn.closeConnection();
	} else{
	String lateDate = year + "-" + month + "-" + day;

	

	out.println(cs5530.Database.CheckLateListWeb(lateDate, conn.con));
	conn.closeStatement();
	conn.closeConnection();
	}
}
else
{
	out.println("<BR><BR><h3>No empty fields, please try again</h3>");
}
}
%>
<a href="AddRecord.jsp" class="btn btn-primary" role="button">Check another date</a></p>
<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>
</body>
</html>