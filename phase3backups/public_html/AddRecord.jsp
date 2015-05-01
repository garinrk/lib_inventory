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
<h1 class="text-center">Add Book Record</h1>
</div>




<body>


	<% String titleattr = request.getParameter("title");

	if(titleattr == null){


	%>

	<form role="form" name="AddBookRecord" method=get action="AddRecord.jsp">

		Title:
		<br>
		<input type=hidden class="form-control" name="title">	
		<input type="text" class="form-control" name="titleValue">
		<br>
		ISBN:
		<br>
		<input type=hidden class="form-control" name="isbn">
		<input type="text" class="form-control" name="isbnValue">
		<br>
		Author(s):<br>
		<textarea name="authorsValue" class="form-control" cols="50" rows="2" onFocus="value=''">Comma Separated Example: Author1,Author2,Author3
		</textarea>
		<br>
		Summary:<br>
		<textarea name="summaryValue" class="form-control" cols="50" rows="2">
		</textarea>
		<br>
		Publisher:<br>
		<input type=hidden class="form-control" name="publisher">
		<input type="text" class="form-control" name="publisherValue">
		<br>
		Subject:<br>
		<input type=hidden class="form-control" name="subject">
		<input type="text" class="form-control" name="subjectValue">
		<br>
		Format:<br>
		<input type=hidden class="form-control" name="format">
		<input type="text" class="form-control" name="formatValue">
		<br>
		Year Of Publication:<br>
		<input type=hidden class="form-control" name="pubyear">
		<input type="text" class="form-control" name="pubyearValue">
		<br>
		<br>
		<input type=submit class="btn btn-info"  value="Add Record">

	</form>

	<% } else {

	String[] authors = request.getParameter("authorsValue").split(",");
	String title = request.getParameter("titleValue");
	String isbn = request.getParameter("isbnValue");
	String summary = request.getParameter("summaryValue");
	String publisher = request.getParameter("publisherValue");
	String subject = request.getParameter("subjectValue");
	String format = request.getParameter("formatValue");
	String pubYear = request.getParameter("pubyearValue");

	if(title != "" && isbn != "" && summary != "" && publisher != "" && subject != "" && format != "" && pubYear != ""){

	cs5530.Connector conn = new Connector();

	out.println(cs5530.Database.AddBookRecordWeb(isbn,title,publisher,pubYear, format, subject, summary, authors, conn.con));
	conn.closeStatement();
	conn.closeConnection();
}
else{
out.println("<BR><BR><h3>No empty fields, please try again</h3>");
	
}
}
%>

<a href="AddRecord.jsp" class="btn btn-primary" role="button">Add another book record</a></p>
<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>
</body>
</html>