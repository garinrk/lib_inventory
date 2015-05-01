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

</head>


<meta name="viewport" content="width=device-width, initial-scale=1">
 

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<body>

	  <%

  String numberofbooksattr = request.getParameter("numberofbooks");

  if(numberofbooksattr == null){


  %>

  <div class="jumbotron">
	<h1 class="text-center">Library Statistics</h1>
</div>


	<form name="GetLibraryStats" method=get action="LibraryStats.jsp">
		Number of books (N) :<BR>
		<input type=hidden class="form-control" name="numberofbooks">
		<input type=text class="form-control" name="booksValue" value="2" onFocus="value=''">
		<BR>
		<BR>Stats to return: 
		<select class="form-control" name="statsSelection">
			<option value="1">N most requested books </option>
			<option value="2">N most checked out books</option>
			<option value="3">N most lost books</option>
			<option value="4">N most popular authors</option>
		</select>
		<br><BR>
		<input type=submit class="btn btn-info" value="Get Statistics">

	</form>


	  <%
} else {

String amountval = request.getParameter("booksValue");
String selection = request.getParameter("statsSelection");

cs5530.Connector conn = new Connector();

out.println(cs5530.Database.PrintLibraryStatisticsWeb(amountval, selection, conn.con));


conn.closeStatement();
conn.closeConnection();

}
%>


<BR><a href="LibraryStats.jsp" class="btn btn-primary" role="button">Get more Library Statistics</a></p>
</body>
</html>