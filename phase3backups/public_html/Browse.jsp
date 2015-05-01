<%@ page language="java" import="cs5530.*, java.util.ArrayList" %>
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
	<h1 class="text-center">Browse Library</h1>
</div>

<body>

	<% 

	String publisherattr = request.getParameter("publisher");
	String subjectattr = request.getParameter("subject");


	if(publisherattr == null){

	%>

	<form role = "form">
		<form role="form">
			<div class="form-group">


				<div class="form-group">
					<label for="Author(s):">Title Word(s):</label>
					<textarea class="form-control" onFocus="value=''" rows="1" name="TitleWords">Comma Separated Example: Word1,Word2,Word3</textarea>
				</div>

				<label for="Author(s):">Author(s):</label>
				<textarea class="form-control" onFocus="value=''" rows="1" name="authors">Comma Separated Example: Author1,Author2,Author3</textarea>
			</div>


			<br>Publisher
			<input class="form-control" type=hidden name="publisher">
			<input class="form-control" type=text name="pubValue">
			<br>Subject <br>
			<input class="form-control" type=hidden name="subject">
			<input class="form-control" type=text name="subValue" >
			<br>


			Only list...<br>
			<div class="radio">
				<label><input type="radio" name="list" value="availablebooks" checked="">Books that are available for checkout</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="list" value="allbooks">All books in the library record system</label>
			</div>
			<br>
			Sort by...<br>

			<div class="radio">
				<label><input type="radio" name="sort" value="pubyear" checked="">Year Published</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="sort" value="avgreview">Average Review Score</label>
			</div>
			<div class="radio">
				<label><input type="radio" name="sort" value="popularity">Popularity</label>
			</div>
			<br><br>
			<input type=submit class="btn btn-info" value="Send Query">




		</form>
		<%
	} else {
	String[] authors = request.getParameter("authors").split(",");
	String publishervalue = request.getParameter("pubValue");
	String subjectval = request.getParameter("subValue");
	String[] titlewords = request.getParameter("TitleWords").split(",");
	String listval = request.getParameter("list");
	String sortval = request.getParameter("sort");
	int authorCount = authors.length;
	int titleCount = titlewords.length;
	boolean allBooks = listval.matches("allbooks");

	if(publishervalue != "" && subjectval != "" && authors.length != 0 && titlewords.length != 0)
	{

	ArrayList<String> params = new ArrayList<String>();

	for(int i = 0; i < titlewords.length; i++)
	{
	params.add(titlewords[i]);
}

//add parameters to arraylist
for(int i = 0; i < authors.length; i++)
{
	params.add(authors[i]);

}



//add publsiher
params.add(publishervalue);

//add subject
params.add(subjectval);



cs5530.Connector conn = new Connector();

%>
<h1> Results: </h1>


<%

out.println(cs5530.Database.BrowseLibraryWeb(titleCount, authorCount, params, allBooks, sortval, conn.con ));

conn.closeStatement();
conn.closeConnection();
}
else{
out.println("<BR><BR><h3>No empty fields, please try again</h3>");
}
}
%>

<a href="Browse.jsp" class="btn btn-primary" role="button">Browse Again</a>
<div class="text-center"> 
	<BR><a class="btn btn-success" href="index.html">Library Home</a></p>
	</div>
</body>
</html>