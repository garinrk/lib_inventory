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
<h1 class="text-center">Get Book Record</h1>
</div>

<body>

	<%

	String isbnattr = request.getParameter("isbn");

	if(isbnattr == null){


	%>



	<form role ="form" name="GetRecord" method=get action="BookRecord.jsp">
		<br>ISBN:<br>
		<input type=hidden class="form-control" name="isbn">
		<input type="text" name="isbnValue" class="form-control">

		<BR><input type=submit class="btn btn-info" value="Get Book Record">
	</form>


	<%
} else {


String isbnval = request.getParameter("isbnValue");

if(isbnval != ""){

cs5530.Connector conn = new Connector();


out.println(cs5530.Database.PrintBookRecordWeb(isbnval, conn.con));

conn.closeStatement();
conn.closeConnection();
}
else{
	out.println("<BR><BR><h3>No empty fields, please try again</h3>");
}


}
%>

<a href="BookRecord.jsp" class="btn btn-primary" role="button">Print Another Book Record</a></p>
<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>

</body>
</html>